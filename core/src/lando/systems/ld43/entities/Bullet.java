package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.utils.Assets;

public class Bullet extends GameObject {
    public Vector2 velocity;

    public Bullet(Assets assets, Vector2 position) {
        super(assets);
        this.position = position;
        velocity = new Vector2(10f, 0f);
    }

    public void update(float dt) {
        position.add(velocity);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width, height);
    }
}
