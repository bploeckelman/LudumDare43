package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld43.entities.Bullet;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Config;

public class VerticalEnemy extends Enemy {

    public float direction = -1;
    public float viewportHeight;
    public float shootDelay;

    public VerticalEnemy(GameScreen gameScreen, float x, float y) {
        super(gameScreen);
        position.set(x, y);
        this.viewportHeight = Config.window_height;
        shootDelay = 2f;
        this.pointWorth = 2000;
    }

    @Override
    public void update(float dt){

        shootDelay -= dt;
        if (shootDelay <= 0){
            int spreadCount = 5;
            for (int i = 0; i < spreadCount; i++) {
                float dir = 135 + ((float)i / (spreadCount-1)) * 90;
                Bullet b = gameScreen.bulletPool.obtain();
                b.init(assets.redBullet, position.x, position.y, 100 * MathUtils.cosDeg(dir), 100 * MathUtils.sinDeg(dir), false, 10, 10, 10, 1);
                gameScreen.aliveBullets.add(b);
            }
            shootDelay += 4f;
        }
        position.x -= 50 * dt;
        if (position.y < 1) {
            direction = 1;
        } else if (position.y > viewportHeight) {
            direction = -1;
        }
        position.y += direction * 100 * dt;
        if (position.x < -width) alive = false;
        super.update(dt);
    }

    @Override
    public void render(SpriteBatch batch){
        batch.setColor(damageColor);
        batch.draw(assets.shipEnemy, position.x - width/2, position.y - height/2, width, height);
        batch.setColor(Color.WHITE);
    }
}
