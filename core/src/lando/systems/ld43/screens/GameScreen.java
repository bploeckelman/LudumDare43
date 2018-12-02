package lando.systems.ld43.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Back;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import lando.systems.ld43.LudumDare43;
import lando.systems.ld43.entities.Bullet;
import lando.systems.ld43.entities.PlayerShip;
import lando.systems.ld43.entities.SatelliteShip;
import lando.systems.ld43.entities.enemies.DroneEnemy;
import lando.systems.ld43.entities.enemies.Enemy;
import lando.systems.ld43.entities.enemies.VerticalEnemy;
import lando.systems.ld43.ui.Background;
import lando.systems.ld43.ui.StarfieldBackground;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Config;
import lando.systems.ld43.utils.screenshake.ScreenShakeCameraController;

import java.util.ArrayList;

public class GameScreen extends BaseScreen {

    public ScreenShakeCameraController shaker;
    public Background background;
    public PlayerShip player;
    public ArrayList<Enemy> enemies;
    public Array<Bullet> aliveBullets;
    public Pool<Bullet> bulletPool;


    private Vector2 tempVec2;
    private Vector3 mousePos;

    public GameScreen(LudumDare43 game, Assets assets) {
        super(game, assets);
        tempVec2 = new Vector2();
        mousePos = new Vector3();
        Vector2 startPosition = new Vector2(40, worldCamera.viewportHeight/2);
        player = new PlayerShip(assets, startPosition);
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

    }

    @Override
    public void update(float dt) {
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

        if (MathUtils.random(1000) > 990){
            enemies.add(new DroneEnemy(assets, worldCamera.viewportWidth, MathUtils.random(worldCamera.viewportHeight)));
        }
        if (MathUtils.random(1000) > 990){
            enemies.add(new VerticalEnemy(assets, worldCamera.viewportWidth, MathUtils.random(worldCamera.viewportHeight), worldCamera.viewportHeight));
        }

        for (int i = 0; i < aliveBullets.size; i++) {
            Bullet bullet = aliveBullets.get(i);
            if (bullet.position.x > Config.window_width) {
                aliveBullets.removeIndex(i);
                bullet.reset();
                bulletPool.free(bullet);
                i--;
            } else {
                bullet.update(dt);
            }
        }

        for (int i = enemies.size()-1; i >= 0; i--){
            Enemy e = enemies.get(i);
            e.update(dt);
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
            player.render(batch);
            for (Bullet bullet: aliveBullets) {
                bullet.render(batch);
            }
            for (Enemy enemy : enemies){
                enemy.render(batch);
                enemy.renderTarget(batch);
            }
        }
        batch.end();
    }

    public void shoot(SatelliteShip.EShipTypes shipType, Vector2 position) {
        Bullet bullet = bulletPool.obtain();
        switch (shipType) {
            case TRIPLE_SHOT:
                Bullet bullet1 = bulletPool.obtain();
                Bullet bullet2 = bulletPool.obtain();

                bullet1.init(assets, new Vector2(position.x, position.y), new Vector2(600f, 600f), true);
                bullet2.init(assets, new Vector2(position.x, position.y), new Vector2(600f, 0f), true);
                bullet.init(assets, new Vector2(position.x, position.y), new Vector2(600f, -600f), true);

                aliveBullets.add(bullet1);
                aliveBullets.add(bullet2);
                aliveBullets.add(bullet);
                break;
            case STRAIGHT_SHOT:
                bullet.init(assets, new Vector2(position.x, position.y), new Vector2(600f, 0f), true);
                aliveBullets.add(bullet);
                break;
            case QUICK_SHOT:
                bullet.init(assets, new Vector2(position.x, position.y), new Vector2(900f, 0f), true);
                aliveBullets.add(bullet);
                break;
        }
    }
}
