package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld43.utils.Assets;

public class SatelliteShip extends GameObject {
    public float xPosOffset;
    public float yPosOffset;
    public Array<Bullet> bullets;
    public Assets assets;

    public SatelliteShip(Assets assets, Vector2 playerPosition, float xPosOffset, float yPosOffset) {
        super(assets);
        this.texture = assets.whitePixel;
        this.xPosOffset = xPosOffset;
        this.yPosOffset = yPosOffset;
        width = height = 15;
        bullets = new Array<Bullet>();
        this.assets = assets;
        this.position = new Vector2(playerPosition.x + xPosOffset, playerPosition.y + yPosOffset);
    }

    public void update(float dt) {
        for (Bullet bullet: bullets) {
            bullet.update(dt);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width, height);
        for (Bullet bullet: bullets) {
            bullet.render(batch);
        }
    }

    public void updatePosition(Vector2 playerPosition) {
        position = new Vector2(playerPosition.x + xPosOffset, playerPosition.y + yPosOffset);
    }

    public void shoot() {
        bullets.add(new Bullet(assets, position));
    }
}
