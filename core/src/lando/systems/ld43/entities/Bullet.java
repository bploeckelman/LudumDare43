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
    public float damage;
    public TextureRegion texture;
    public float width;
    public float height;
    public float collisionRadius;
    public boolean isAlive;

    public Bullet() {
        width = height = 10;
        damage = 1;
        collisionBounds = new Rectangle(0,0, width, height);
        collisionRadius = 10;
        this.position = new Vector2();
        this.velocity = new Vector2();
    }

    public void init(TextureRegion texture, float positionX, float positionY, float velocityX, float velocityY, boolean isFriendlyBullet, float width, float height, float collisionRadius, float damage) {
        this.position.set(positionX, positionY);
        this.velocity.set(velocityX, velocityY);
        this.texture = texture;
        this.isFriendlyBullet = isFriendlyBullet;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.collisionRadius = collisionRadius;
        this.collisionBounds.set(position.x - collisionRadius/2, position.y - collisionRadius/2, collisionRadius, collisionRadius);
        isAlive = true;
    }

    public void update(float dt) {
        position.add(velocity.x * dt, velocity.y * dt);
        collisionBounds.set(position.x - collisionRadius/2, position.y - collisionRadius/2, collisionRadius, collisionRadius);
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
