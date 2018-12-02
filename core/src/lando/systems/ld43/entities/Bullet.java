package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import lando.systems.ld43.utils.Assets;

public class Bullet implements Pool.Poolable {
    public Vector2 velocity;
    public Vector2 position;
    public boolean isFriendlyBullet;
    public TextureRegion texture;
    public float width;
    public float height;
    public boolean isAlive;

    public Bullet() {
    }

    public void init(TextureRegion texture, Vector2 position, Vector2 velocity, boolean isFriendlyBullet, float width, float height) {
        this.position = position;
        this.velocity = velocity;
        this.texture = texture;
        this.isFriendlyBullet = isFriendlyBullet;
        this.width = width;
        this.height = height;
        isAlive = true;
    }

    public void update(float dt) {
        position.add(velocity.x * dt, velocity.y * dt);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width, height);
    }

    @Override
    public void reset() {
        position.set(0, 0);
        velocity.set(0, 0);
        texture = null;
        isFriendlyBullet = false;
        isAlive = false;
    }
}
