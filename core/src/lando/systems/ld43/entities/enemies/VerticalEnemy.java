package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Config;

public class VerticalEnemy extends Enemy {

    public float direction = -1;
    public float viewportHeight;
    public VerticalEnemy(Assets assets, float x, float y) {
        super(assets);
        position.set(x, y);
        this.viewportHeight = Config.window_height;
    }

    @Override
    public void update(float dt){
        position.x -= 50 * dt;
        if (position.y < 1) {
            direction = 1;
        } else if (position.y > viewportHeight) {
            direction = -1;
        }
        position.y += direction * 100 * dt;
        if (position.x < -width) alive = false;
    }

    @Override
    public void render(SpriteBatch batch){
        batch.draw(assets.whitePixel, position.x - width/2, position.y - height/2, width, height);
    }
}
