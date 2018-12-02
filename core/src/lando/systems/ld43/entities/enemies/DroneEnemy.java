package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld43.entities.Bullet;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Assets;

public class DroneEnemy extends Enemy {

    private float shootDelay;

    public DroneEnemy(GameScreen gameScreen, float x, float y) {
        super(gameScreen);
        position.set(x, y);
        shootDelay = 2f;
    }

    @Override
    public void update(float dt){
        shootDelay -= dt;
        if (shootDelay <= 0){
            Bullet b = gameScreen.bulletPool.obtain();
            b.init(assets.redBullet, position.x, position.y, -150, 0, false, 10, 10, 10, 1);
            gameScreen.aliveBullets.add(b);
            shootDelay += 2f;
        }
        position.x -= 50 * dt;
        if (position.x < -width) alive = false;
        super.update(dt);
    }

    @Override
    public void render(SpriteBatch batch){
        batch.draw(assets.whitePixel, position.x - width/2, position.y - height/2, width, height);
    }
}
