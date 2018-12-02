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
import lando.systems.ld43.ui.Background;
import lando.systems.ld43.ui.EquipmentUI;
import lando.systems.ld43.ui.StarfieldBackground;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Config;
import lando.systems.ld43.utils.QuadTree;
import lando.systems.ld43.utils.QuadTreeable;
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
    private Array<QuadTreeable> collisionEntities;


    private Level level;
    private EquipmentUI equipmentUI;

    private Vector2 tempVec2;
    private Vector3 mousePos;

    public GameScreen(LudumDare43 game, Assets assets, Pilot.Type pilotType) {
        super(game, assets);
        tempVec2 = new Vector2();
        mousePos = new Vector3();
        Vector2 startPosition = new Vector2(40, worldCamera.viewportHeight/2);
        player = new PlayerShip(this, assets, startPosition, pilotType);
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
    }

    @Override
    public void update(float dt) {
        equipmentUI.update(dt);
        if (equipmentUI.isVisible()) {
            return;
        }

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldCamera.unproject(mousePos);

        tempVec2.set(MathUtils.clamp(mousePos.x, player.width, worldCamera.viewportWidth/2),
                     MathUtils.clamp(mousePos.y, player.height, worldCamera.viewportHeight - player.height));
        player.update(dt, tempVec2);

        if (Gdx.input.justTouched()) {
            int rand = MathUtils.random(0, player.playerShips.size - 1);
            SatelliteShip satShip = player.playerShips.get(rand);
            shoot(satShip.shipType, satShip.position);
        }

        // TODO: remove me, just testing for now
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            equipmentUI.reset(this).show();
        }

        level.update(dt);

        bulletTree.clear();
        for (int i = 0; i < aliveBullets.size; i++) {
            Bullet bullet = aliveBullets.get(i);
            if (bullet.position.x > Config.window_width || bullet.position.x < 0 ||
                bullet.position.y > Config.window_height || bullet.position.y < 0 ||
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

        collisionEntities.clear();
        for (int i = enemies.size()-1; i >= 0; i--){
            Enemy e = enemies.get(i);
            e.update(dt);
            for (int j = e.targetPoints.size() -1; j >= 0; j--) {
                TargetPoint target = e.targetPoints.get(j);
                bulletTree.retrieve(collisionEntities, target);
                for (QuadTreeable entity : collisionEntities) {
                    if (entity instanceof Bullet) {
                        Bullet b = (Bullet) entity;
                        if (b.isFriendlyBullet && b.isAlive) e.checkBulletCollision(b, target);
                    }
                }
            }

            if (!e.alive){
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
        }
        batch.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        {
            batch.setColor(Color.WHITE);
            batch.draw(player.pilot.textureHead, 5f, 5f, 64f, 64f);

            equipmentUI.render(batch);
        }
        batch.end();
    }

    public void shoot(SatelliteShip.EShipTypes shipType, Vector2 position) {
//        Bullet bullet = bulletPool.obtain();
//        switch (shipType) {
//            case SPREAD_SHOT:
//                Bullet bullet1 = bulletPool.obtain();
//                Bullet bullet2 = bulletPool.obtain();
//
//                bullet1.init(assets.spreadBullet, new Vector2(position.x, position.y), new Vector2(BULLET_BASE_X_VELOCITY, BULLET_BASE_Y_VELOCITY), true, BULLET_BASE_WIDTH, BULLET_BASE_HEIGHT, BULLET_BASE_WIDTH, 1f);
//                bullet2.init(assets.spreadBullet, new Vector2(position.x, position.y), new Vector2(BULLET_BASE_X_VELOCITY, 0f), true, BULLET_BASE_WIDTH, BULLET_BASE_HEIGHT, BULLET_BASE_WIDTH, 1f);
//                bullet.init(assets.spreadBullet, new Vector2(position.x, position.y), new Vector2(BULLET_BASE_X_VELOCITY, -1f * BULLET_BASE_Y_VELOCITY), true, BULLET_BASE_WIDTH, BULLET_BASE_HEIGHT, BULLET_BASE_WIDTH, 1f);
//
//                aliveBullets.add(bullet1);
//                aliveBullets.add(bullet2);
//                aliveBullets.add(bullet);
//                break;
//            case STRAIGHT_SHOT:
//                bullet.init(assets.redBullet, new Vector2(position.x, position.y), new Vector2(BULLET_BASE_X_VELOCITY, 0f), true, 1.5f * BULLET_BASE_WIDTH, 1.5f * BULLET_BASE_HEIGHT, 1.5f * BULLET_BASE_WIDTH, 2f);
//                aliveBullets.add(bullet);
//                break;
//            case QUICK_SHOT:
//                bullet.init(assets.satelliteLaserBullet, new Vector2(position.x, position.y), new Vector2(1.5f * BULLET_BASE_X_VELOCITY, 0f), true, 5f * BULLET_BASE_WIDTH, 1.5f * BULLET_BASE_HEIGHT, 1.5f * BULLET_BASE_HEIGHT, 1f);
//                aliveBullets.add(bullet);
//                break;
//        }
    }

    public void enemyShoot(Vector2 position) {
//        Bullet bullet = bulletPool.obtain();
//        bullet.init(assets.redBullet, new Vector2(position.x, position.y), new Vector2(-1f * BULLET_BASE_X_VELOCITY, 0f), false, BULLET_BASE_WIDTH, BULLET_BASE_HEIGHT, BULLET_BASE_WIDTH, 1f);
//        aliveBullets.add(bullet);
    }
}
