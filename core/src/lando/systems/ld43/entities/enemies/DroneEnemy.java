package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld43.utils.Assets;

public class DroneEnemy extends Enemy {
    public DroneEnemy(Assets assets, float x, float y) {
        super(assets);
        position.set(x, y);
    }

    @Override
    public void update(float dt){
        position.x -= 50 * dt;
        if (position.x < -width) alive = false;
    }

    @Override
    public void render(SpriteBatch batch){
        batch.draw(assets.whitePixel, position.x - width/2, position.y - height/2, width, height);
    }
}