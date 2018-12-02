package lando.systems.ld43.screens;

import aurelienribon.tweenengine.Tween;
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
import lando.systems.ld43.LudumDare43;
import lando.systems.ld43.entities.Bullet;
import lando.systems.ld43.entities.Pilot;
import lando.systems.ld43.entities.PlayerShip;
import lando.systems.ld43.entities.SatelliteShip;
import lando.systems.ld43.entities.enemies.Enemy;
import lando.systems.ld43.entities.enemies.TargetPoint;
import lando.systems.ld43.level.Level;
import lando.systems.ld43.ui.*;
import lando.systems.ld43.utils.*;
import lando.systems.ld43.utils.screenshake.ScreenShakeCameraController;

import java.util.ArrayList;

public class GameScreen extends BaseScreen {

    public ScreenShakeCameraController shaker;
    public Background background;
    public PlayerShip player;
    public ArrayList<Enemy> enemies;
    public Array<Bullet> aliveBullets;
    public Pool<Bullet> bulletPool;
    public QuadTree bulletTree;

    public Array<QuadTreeable> collisionEntities;


    private Level level;
    public DialogUI dialogUI;
    private EquipmentUI equipmentUI;
    private Vector2 tempVec2;
    private Vector3 mousePos;
    public ScoreUI scoreUI;

    public GameScreen(LudumDare43 game, Assets assets, Pilot.Type pilotType) {
        super(game, assets);
        tempVec2 = new Vector2();
        mousePos = new Vector3();
        Vector2 startPosition = new Vector2(40, worldCamera.viewportHeight/2);
        player = new PlayerShip(this, startPosition, pilotType);
        enemies = new ArrayList<Enemy>();
        background = new StarfieldBackground(assets);
        shaker = new ScreenShakeCameraController(worldCamera);
        aliveBullets = new Array<Bullet>();
        bulletPool = new Pool<Bullet>() {
            @Override
            protected Bullet newObject() {
                return new Bullet();
            }
        };
        Tween.to(background.speed, 0, 2f)
                .target(100f)
                .start(game.tween);

        level = new Level(this, 1);
        bulletTree = new QuadTree(assets,0, new Rectangle(0,0, worldCamera.viewportWidth, worldCamera.viewportHeight));
        collisionEntities = new Array<QuadTreeable>();
        this.equipmentUI = new EquipmentUI(assets);
        this.dialogUI = new DialogUI(assets);
        this.scoreUI = new ScoreUI(assets, game);
        game.audio.playMusic(Audio.Musics.RockHardyWithMaster);
    }

    @Override
    public void update(float dt) {
        scoreUI.update(dt);
        dialogUI.update(dt);
        equipmentUI.update(dt);
        if (equipmentUI.isVisible() || dialogUI.isVisible()) {
            return;
        }

        audio.update(dt);

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldCamera.unproject(mousePos);

        // TODO: remove me, just testing for now
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && dialogUI.isHidden()) {
//            equipmentUI.reset(this).show();
            dialogUI.reset(this, "dialog.json").show();
        }

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

        tempVec2.set(MathUtils.clamp(mousePos.x, player.width, worldCamera.viewportWidth/2),
                     MathUtils.clamp(mousePos.y, player.height, worldCamera.viewportHeight - player.height));
        player.update(dt, tempVec2);

        if (player.laserOn){
            player.laserLength = Config.window_width - player.position.x;
            TargetPoint enemyHit = null;
            for (Enemy e : enemies){
                for (TargetPoint target : e.targetPoints){
                    float targetCenter = target.collisionBounds.y + target.collisionBounds.height/2f;
                    if (Math.abs(targetCenter - player.position.y) < target.diameter + player.laserWidth && e.position.x > player.position.x){
                        float dist = target.collisionBounds.x + target.diameter/2f - player.position.x - player.width/2f;
                        if (dist < player.laserLength){
                            enemyHit = target;
                            player.laserLength = dist;
                        }
                    }
                }
            }
            if (enemyHit != null){
                enemyHit.health -= 10 * dt;
                enemyHit.damageIndicator = .3f;
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

        for (int i = enemies.size()-1; i >= 0; i--){
            Enemy e = enemies.get(i);
            e.update(dt);
            for (int j = e.targetPoints.size() -1; j >= 0; j--) {
                TargetPoint target = e.targetPoints.get(j);
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
                scoreUI.addScore(1);
                // TODO: explosion
                enemies.remove(i);
            }
        }

        background.update(dt);
        shaker.update(dt);
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
                enemy.renderTarget(batch);
            }
            for (Bullet bullet: aliveBullets) {
                bullet.render(batch);
            }
            player.renderLaser(batch);
        }
        batch.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        {
            batch.setColor(Color.WHITE);
            batch.draw(player.pilot.textureHead, 5f, 5f, 64f, 64f);

            scoreUI.render(batch);
            dialogUI.render(batch);
            equipmentUI.render(batch);
        }
        batch.end();
    }

    public void clearAllBullets(){
        bulletPool.freeAll(aliveBullets);
        aliveBullets.clear();
    }

}
