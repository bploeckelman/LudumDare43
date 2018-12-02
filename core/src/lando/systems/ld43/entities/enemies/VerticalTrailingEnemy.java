package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.utils.Assets;

public class VerticalTrailingEnemy extends Enemy {

    public Vector2 playerPosition;
    private float direction = 1;
    public VerticalTrailingEnemy(Assets assets, float x, float y, Vector2 playerPosition) {
        super(assets);
        position.set(x, y);
        this.playerPosition = playerPosition;
    }

    @Override
    public void update(float dt){
        position.x -= 50 * dt;
        if (position.y < playerPosition.y) {
            direction = 1;
        }
        else if (playerPosition.y < position.y) {
            direction = -1;
        }
        if (Math.abs(playerPosition.y - position.y) > 25) {
            position.y += direction * 25 * dt;
        }
        if (position.x < -width) alive = false;
    }

    @Override
    public void render(SpriteBatch batch){
        batch.draw(assets.whitePixel, position.x - width/2, position.y - height/2, width, height);
    }
}
