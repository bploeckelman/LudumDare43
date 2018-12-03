package lando.systems.ld43.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Expo;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lando.systems.ld43.LudumDare43;
import lando.systems.ld43.accessors.Vector2Accessor;
import lando.systems.ld43.entities.*;
import lando.systems.ld43.entities.enemies.Enemy;
import lando.systems.ld43.entities.enemies.TargetPoint;
import lando.systems.ld43.entities.powerups.PowerUp;
import lando.systems.ld43.level.Level;
import lando.systems.ld43.particles.ParticleSystem;
import lando.systems.ld43.ui.*;
import lando.systems.ld43.utils.*;
import lando.systems.ld43.utils.screenshake.ScreenShakeCameraController;

import java.util.ArrayList;

public class GameScreen extends BaseScreen {

    public final int NUM_LEVELS = 5;
    private enum FinalStage {dialog, movingShips, fireLaser, firingLaser, nextBoss, finalCharge, finalExplosion, transition}

    public ScreenShakeCameraController shaker;
    public ParticleSystem particleSystem;
    public Background background;
    public PlayerShip player;
    public ArrayList<Enemy> enemies;
    public Array<PowerUp> powerUps;
    public Enemy boss;
    public Array<Bullet> aliveBullets;
    public Pool<Bullet> bulletPool;
    public QuadTree bulletTree;
    public Array<QuadTreeable> collisionEntities;
    public Array<Asteroid> asteroids;
    public int levelIndex;
    public Level level;
    private Vector2 tempVec2;
    private Vector3 mousePos;
    private SatelliteShip sacrificedShip;
    public ScoreUI scoreUI;
    public DialogUI dialogUI;
    public ProgressUI progressUI;
    public EquipmentUI equipmentUI;
    public HealthMeter healthMeter;
    public CooldownMeter cooldownMeter;
    private FinalStage finalStage;
    private MutableFloat finalBossDir;
    private MutableFloat finalBossRadius;
    private Vector2 mouseIndicator;

    public GameScreen(LudumDare43 game, Assets assets, Pilot.Type pilotType) {
        super(game, assets);
        levelIndex = 0;
        tempVec2 = new Vector2();
        mousePos = new Vector3();
        Vector2 startPosition = new Vector2(40, worldCamera.viewportHeight/2);
        player = new PlayerShip(this, startPosition, pilotType);
        enemies = new ArrayList<Enemy>();
        powerUps = new Array<PowerUp>();
        background = new StarfieldBackground(assets);
        shaker = new ScreenShakeCameraController(worldCamera);
        aliveBullets = new Array<Bullet>();
        bulletPool = Pools.get(Bullet.class);
        bulletTree = new QuadTree(assets,0, new Rectangle(0,0, worldCamera.viewportWidth, worldCamera.viewportHeight));
        collisionEntities = new Array<QuadTreeable>();
        this.asteroids = new Array<Asteroid>();
        this.mouseIndicator = new Vector2();

        this.particleSystem = new ParticleSystem(assets);
        this.equipmentUI = new EquipmentUI(assets);
        this.dialogUI = new DialogUI(assets);
        this.scoreUI = new ScoreUI(assets, this);
        this.progressUI = new ProgressUI(assets);
        this.progressUI.reset(this, player).show();
        this.healthMeter = new HealthMeter(assets, this);
        this.cooldownMeter = new CooldownMeter(assets, this);

        this.boss = null;
        nextLevel();
        game.audio.playMusic(Audio.Musics.RockHardyWithMaster);
    }

    @Override
    public void update(float dt) {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop
         && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        Gdx.input.setCursorPosition(
                (int) MathUtils.clamp(Gdx.input.getX(), 0, hudCamera.viewportWidth),
                (int) MathUtils.clamp(Gdx.input.getY(), 0, hudCamera.viewportHeight));

        audio.update(dt);
        particleSystem.update(dt);
        background.update(dt);
        shaker.update(dt);
        scoreUI.update(dt);
        healthMeter.update(dt);
        cooldownMeter.update(dt);
        dialogUI.update(dt);
        equipmentUI.update(dt);
        progressUI.update(dt);
        if (boss != null && !boss.alive){
            if (levelIndex == 5){
                gameEnding(dt);
            } else {
                handleEndLevel(dt);
            }
            return;
        }
        if (equipmentUI.isVisible() || dialogUI.isVisible()) {
            return;
        }

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldCamera.unproject(mousePos);
        mouseIndicator.set(mousePos.x, mousePos.y);

        level.update(dt);

        bulletTree.clear();
        for (int i = 0; i < aliveBullets.size; i++) {
            Bullet bullet = aliveBullets.get(i);
            if (bullet.position.x > Config.window_width + 30 || bullet.position.x < -30 ||
                    bullet.position.y > Config.window_height + 30 || bullet.position.y < -30 ||
                    !bullet.isAlive) {
                aliveBullets.removeIndex(i);
                bullet.reset();
                bulletPool.free(bullet);
                i--;
            } else {
                bullet.update(dt);
                bulletTree.insert(bullet);
            }
        }

        tempVec2.set(MathUtils.clamp(mousePos.x, player.width, (3f / 4f) * worldCamera.viewportWidth),
                     MathUtils.clamp(mousePos.y, player.height, worldCamera.viewportHeight - player.height));
        player.setTargetPosition(tempVec2);
        player.update(dt, true);

        if (player.laserOn){
            player.laserLength = Config.window_width - player.position.x;
            TargetPoint enemyHit = null;
            Enemy targetEnemy = null;
            for (Enemy e : enemies){
                for (TargetPoint target : e.targetPoints){
                    if (target.health <= 0) continue;
                    float targetCenter = target.collisionBounds.y + target.collisionBounds.height/2f;
                    if (Math.abs(targetCenter - player.position.y) < target.diameter + player.laserWidth && e.position.x > player.position.x){
                        float dist = target.collisionBounds.x + target.diameter/2f - player.position.x - player.width/2f;
                        if (dist < player.laserLength){
                            enemyHit = target;
                            targetEnemy = e;
                            player.laserLength = dist;
                        }
                    }
                }
            }
            for (Asteroid asteroid : asteroids){
                if (Math.abs(asteroid.position.y - player.position.y) < player.laserWidth + asteroid.size && asteroid.position.x > player.position.x){
                    float dist = asteroid.position.x - asteroid.size /2f - player.position.x - player.width/2f;
                    if (dist < player.laserLength){
                        player.laserLength = dist;
                    }
                }
            }
            if (enemyHit != null){
                enemyHit.health -= 10 * dt;
                enemyHit.damageIndicator = .3f;
                if (enemyHit.health <= 0) particleSystem.addExplosion(targetEnemy.position.x + enemyHit.positionOffset.x, targetEnemy.position.y + enemyHit.positionOffset.y, enemyHit.diameter * 5f, enemyHit.diameter* 5f);
                particleSystem.addLaserHit(player.position.x + player.width/2f + player.laserLength, player.position.y);
                // TODO: laser particles on hits
            }
        }

        // Check if bullets hit player ship
        collisionEntities.clear();
        bulletTree.retrieve(collisionEntities, player.targetPoint);
        for (QuadTreeable entity : collisionEntities) {
            if (entity instanceof Bullet) {
                Bullet b = (Bullet) entity;
                if (!b.isFriendlyBullet && b.isAlive) player.checkBulletCollision(b);
            }
        }

        // Check if bullets hit player satellites
        for (SatelliteShip satellite : player.playerShips){
            collisionEntities.clear();
            bulletTree.retrieve(collisionEntities, satellite.targetPoint);
            for (QuadTreeable entity : collisionEntities){
                if (entity instanceof Bullet) {
                    Bullet b = (Bullet) entity;
                    if (!b.isFriendlyBullet && b.isAlive) satellite.checkBulletCollision(b);
                }
            }
        }

        level.update(dt);

        // Update enemies & check for collisions
        for (int i = enemies.size()-1; i >= 0; i--){
            Enemy e = enemies.get(i);
            e.update(dt);

            for (int j = e.targetPoints.size() -1; j >= 0; j--) {
                TargetPoint target = e.targetPoints.get(j);
                if (target.health <= 0) continue;
                collisionEntities.clear();
                bulletTree.retrieve(collisionEntities, target);
                for (QuadTreeable entity : collisionEntities) {
                    if (entity instanceof Bullet) {
                        Bullet b = (Bullet) entity;
                        if (b.isFriendlyBullet && b.isAlive) e.checkBulletCollision(b, target);
                    }
                }
            }

            if (!e.alive){
                enemies.remove(i);

                scoreUI.addScore(e.pointWorth);

                // Chance to spawn powerup
                if (MathUtils.randomBoolean(0.2f)) {
                    powerUps.add(PowerUp.createRandom(assets, e.position.x, e.position.y, -100f, 0f));
                }
            }
        }

        if (levelIndex == 5) {
            if (boss == null && asteroids.size < 20){
                asteroids.add(new Asteroid(this));
            }
            for (int i = asteroids.size -1; i >= 0; i--) {
                Asteroid asteroid = asteroids.get(i);
                asteroid.update(dt);
                if (player.position.dst(asteroid.position) < (player.width/2 + asteroid.size/2)){
                    asteroid.alive = false;
                    player.targetPoint.health--;
                    if (player.targetPoint.health <= 0){
                        dialogUI.reset(this, "youdied.json").show();
                        player.replenishHealth();
                    }
                }

                if (!asteroid.alive){
                    asteroids.removeIndex(i);
                }
            }
        }

        // Update power ups and check for pickup
        for (int i = powerUps.size - 1; i >= 0; --i) {
            PowerUp powerUp = powerUps.get(i);
            powerUp.update(dt);
            if (powerUp.gotCollected(player)) {
                powerUp.apply(player);
                powerUps.removeIndex(i);
            }
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(shaker.getCombinedMatrix());
        batch.begin();
        {
            background.render(batch);
//            bulletTree.renderDebug(batch);
            player.render(batch);

            for (Enemy enemy : enemies){
                enemy.render(batch);
            }

            for (Asteroid asteroid : asteroids){
                asteroid.render(batch);
            }

            player.renderLaser(batch);

            for (Bullet bullet: aliveBullets) {
                bullet.render(batch);
            }
            for (PowerUp powerUp : powerUps) {
                powerUp.render(batch);
            }

            if (boss != null && !boss.alive && !boss.destroyed){
                boss.render(batch);
            }
            if (sacrificedShip != null){
                sacrificedShip.render(batch);
            }

            if (finalBossRadius != null){
                float finalBossSize = 200;
                float x = 600 + MathUtils.cosDeg(finalBossDir.floatValue()) * finalBossRadius.floatValue();
                float y = 300 + MathUtils.sinDeg(finalBossDir.floatValue()) * finalBossRadius.floatValue();
                batch.draw(assets.specialBoss, x - finalBossSize/2, y - finalBossSize/2, finalBossSize, finalBossSize);
            }

            particleSystem.render(batch);
        }
        batch.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        {
            batch.setColor(Color.WHITE);
            batch.draw(player.pilot.textureHead, 5f, 5f, 64f, 64f);

            dialogUI.render(batch);
            equipmentUI.render(batch);
            if (equipmentUI.isHidden()) {
                scoreUI.render(batch);
                healthMeter.render(batch);
                cooldownMeter.render(batch);
                progressUI.render(batch);

                if (mouseIndicator.dst(player.position) > player.width / 2f && dialogUI.isHidden() && ((boss == null) || (boss != null && boss.alive != false))) {
                    float mouseSize = 5f;
                    batch.setColor(Color.MAGENTA);
                    batch.draw(assets.whitePixel, mousePos.x - mouseSize / 2f, mousePos.y - mouseSize / 2f, mouseSize, mouseSize);
                    batch.setColor(Color.WHITE);
                }
            }
        }
        batch.end();
    }

    private boolean showingEndTween;
    public void nextLevel() {
        levelIndex++;
        PlayerShip.MAX_SPEED = 300;
        finalStage = FinalStage.dialog;
        if (levelIndex == 5){
            for (int i = 0; i < 10; i++){
                asteroids.add(new Asteroid(this));
            }
        }
        boss = null;
        equipmentUI.reset(this);
        sacrificedShip = null;
        showingEndTween = false;
        enemies.clear();
        player.resetAllPositions(tempVec2.set(-100, worldCamera.viewportHeight/2));
        clearAllBullets();
        level = new Level(this, levelIndex);
        background.speed.setValue(0);
        Tween.to(background.speed, 0, 2f)
                .target(100f)
                .start(game.tween);
    }

    public void handleEndLevel(float dt){
        player.update(dt, false);
        powerUps.clear();
        if (boss != null) {
            boss.damageIndicator = 0;
            boss.damageColor.set(Color.WHITE);
            if (!boss.destroyed) {
                boss.updateWhileDisabled(dt);
            }
        }
        enemies.clear();
        clearAllBullets();

        if (dialogUI.isVisible()){
            return;
        }
        if (equipmentUI.selectedEquipmentType == null && equipmentUI.isHidden()){
            equipmentUI.reset(this);
            equipmentUI.show();
            return;
        }
        if (equipmentUI.selectedEquipmentType == null && equipmentUI.isVisibleAndTransitionComplete()){
            boss.position.set(600, worldCamera.viewportHeight/2f);
            player.setTargetPosition(tempVec2.set(150, worldCamera.viewportHeight/2f));
        }
        if (equipmentUI.selectedEquipmentType != null && equipmentUI.isHidden() && !showingEndTween){
            showingEndTween = true;
            for (int i = player.playerShips.size -1; i >= 0; i--){
                SatelliteShip ship = player.playerShips.get(i);
                if (ship.equipmentType != equipmentUI.selectedEquipmentType) continue;
                sacrificedShip = player.playerShips.removeIndex(i);
                player.resetSatelliteLayout();
                Timeline.createSequence()
                        .push(Tween.to(sacrificedShip.position, Vector2Accessor.XY, .2f)
                            .target(50, worldCamera.viewportHeight/2f))
                        .push(Tween.to(sacrificedShip.position, Vector2Accessor.XY, 2.5f)
                            .target(600, worldCamera.viewportHeight/2f)
                            .waypoint(50, 500)
                            .waypoint(300, 500)
                            .ease(Expo.IN))
                        .push(Tween.call(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                particleSystem.addExplosion(boss.position.x, boss.position.y, boss.width, boss.height);
                                sacrificedShip = null;
                                boss.destroyed = true;
                                PlayerShip.MAX_SPEED = 1000;
                                shaker.addDamage(1f);
                            }
                        }))
                        .pushPause(.5f)
                        .push(Tween.call(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                shaker.addDamage(1f);
                            }
                        }))
                        .pushPause(2f)
                        .push(Timeline.createParallel()
                                .push(Tween.to(background.speed, 0, 1f)
                                        .target(1000))
                                .push(Tween.to(player.targetPosition, Vector2Accessor.X, 1f)
                                        .target(1000)
                                        .delay(.5f)))
                        .pushPause(2f)
                        .push(Tween.call(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                nextLevel();
                            }
                        }))
                        .start(tween);

            }
        }
    }

    public void gameEnding(float dt){
        if (finalStage != FinalStage.finalCharge) {
            player.update(dt, false);
        }
        powerUps.clear();
        if (boss != null) {
            boss.damageIndicator = 0;
            boss.damageColor.set(Color.WHITE);
            if (!boss.destroyed) {
                boss.updateWhileDisabled(dt);
            }
        }
        enemies.clear();
        asteroids.clear();
        clearAllBullets();
        if (dialogUI.isVisible()){
            return;
        }
        switch (finalStage){
            case dialog:
                finalStage = FinalStage.movingShips;
                PlayerShip.MAX_SPEED = 1000;
                Tween.to(boss.position, Vector2Accessor.XY, 2f)
                        .target(600, worldCamera.viewportHeight/2f)
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                finalStage = FinalStage.fireLaser;
                            }
                        })
                        .start(tween);
                player.setTargetPosition(tempVec2.set(150, worldCamera.viewportHeight/2f));
                break;
            case movingShips:
                // NOOP
                break;
            case fireLaser:
                finalStage = FinalStage.firingLaser;
                player.fireFinalLaser = true;
                Tween.call(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            player.fireFinalLaser = false;
                            boss.destroyed = true;
                            shaker.addDamage(1f);
                            finalStage = FinalStage.nextBoss;
                            dialogUI.reset(GameScreen.this, "ending.json").show();

                        }
                    })
                     .delay( 4f)
                     .start(tween);
                break;
            case firingLaser:
                boss.explodingAnimations();
                shaker.addDamage(1f);
                break;
            case nextBoss:
                finalBossDir = new MutableFloat(90);
                finalBossRadius = new MutableFloat(400);
                finalStage = FinalStage.finalCharge;
                Timeline.createSequence()
                        .push(Timeline.createParallel()
                            .push(Tween.to(finalBossDir, 0, 4f)
                                .target(360))
                            .push(Tween.to(finalBossRadius, 0, 4f)
                                .target(0))
                        )
                        .pushPause(1f)
                        .push(Tween.to(player.position, Vector2Accessor.XY, 1f)
                            .target(600, worldCamera.viewportHeight/2f)
                            .ease(Expo.IN))
                        .push(Tween.call(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                finalBossRadius = null;
                                finalBossDir = null;
                                player.hide = true;
                                shaker.addDamage(1f);
                                finalStage = FinalStage.finalExplosion;
                                Tween.call(new TweenCallback() {
                                        @Override
                                        public void onEvent(int i, BaseTween<?> baseTween) {
                                            finalStage = FinalStage.transition;

                                            Tween.call(new TweenCallback() {
                                                @Override
                                                public void onEvent(int i, BaseTween<?> baseTween) {
                                                    game.setScreen(new EndScreen(game, assets));
                                                }
                                            }).delay(2f).start(tween);
                                        }
                                    }).delay(2f)
                                        .start(tween);
                            }
                        }))
                        .start(tween);
                break;
            case finalCharge:
                // NOOP
                break;
            case finalExplosion:
                particleSystem.addExplosion(
                        MathUtils.random(500, 700),
                        MathUtils.random(200, 400),
                        80, 80);
                break;
        }
    }

    public void clearAllBullets(){
        bulletPool.freeAll(aliveBullets);
        aliveBullets.clear();
    }

}
