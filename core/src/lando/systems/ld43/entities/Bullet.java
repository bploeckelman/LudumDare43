package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.QuadTreeable;

public class Bullet extends QuadTreeable implements Pool.Poolable {
    public Vector2 velocity;
    public Vector2 position;
    public boolean isFriendlyBullet;
    public TextureRegion texture;
    public float width;
    public float height;
    public boolean isAlive;

    public Bullet() {
        width = height = 10;
        collisionBounds = new Rectangle(0,0, width, height);
    }

    public void init(TextureRegion texture, Vector2 position, Vector2 velocity, boolean isFriendlyBullet, float width, float height) {
        this.position = position;
        this.velocity = velocity;
        this.texture = texture;
        this.isFriendlyBullet = isFriendlyBullet;
        this.width = width;
        this.height = height;
        this.collisionBounds.set(position.x - width/2, position.y - height/2, width, height);
        isAlive = true;
    }

    public void update(float dt) {
        position.add(velocity.x * dt * .1f, velocity.y * dt * .1f);
        collisionBounds.set(position.x - width/2, position.y - height/2, width, height);
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
