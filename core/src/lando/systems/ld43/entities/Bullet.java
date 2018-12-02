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
    public Assets assets;
    public TextureRegion texture;
    public int width;
    public int height;
    public boolean isAlive;

    public Bullet() {
        width = height = 10;
    }

    public void init(Assets assets, Vector2 position, Vector2 velocity, boolean isFriendlyBullet) {
        this.position = position;
        this.velocity = velocity;
        this.assets = assets;
        texture = assets.redBullet;
        this.isFriendlyBullet = isFriendlyBullet;
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
        isFriendlyBullet = false;
        isAlive = false;
    }
}
