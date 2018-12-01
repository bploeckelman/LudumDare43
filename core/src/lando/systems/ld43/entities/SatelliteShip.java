package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Config;

public class SatelliteShip extends GameObject {
    public enum EShipTypes {
        TRIPLE_SHOT,
        STRAIGHT_SHOT,
        QUICK_SHOT
    }

    public float xPosOffset;
    public float yPosOffset;
    public Array<Bullet> bullets;
    public Assets assets;
    public EShipTypes shipType;

    public SatelliteShip(Assets assets, Vector2 playerPosition, float xPosOffset, float yPosOffset, EShipTypes shipType) {
        super(assets);
        this.texture = assets.whitePixel;
        this.xPosOffset = xPosOffset;
        this.yPosOffset = yPosOffset;
        width = height = 15;
        bullets = new Array<Bullet>();
        this.assets = assets;
        this.position = new Vector2(playerPosition.x + xPosOffset, playerPosition.y + yPosOffset);
        this.shipType = shipType;
    }

    public void update(float dt) {
        for (int i = 0; i < bullets.size; i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.position.x > Config.window_width) {
                bullets.removeIndex(i);
                i--;
            } else {
                bullet.update(dt);
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width, height);
        for (Bullet bullet: bullets) {
            bullet.render(batch);
        }
    }

    public void updatePosition(Vector2 playerPosition) {
        position.x = playerPosition.x + xPosOffset;
        position.y = playerPosition.y + yPosOffset;
    }

    public void shoot() {
        switch (shipType) {
            case TRIPLE_SHOT:
                bullets.add(new Bullet(assets, new Vector2(position.x, position.y), new Vector2(600f, 600f)));
                bullets.add(new Bullet(assets, new Vector2(position.x, position.y), new Vector2(600f, 0f)));
                bullets.add(new Bullet(assets, new Vector2(position.x, position.y), new Vector2(600f, -600f)));
                break;
            case STRAIGHT_SHOT:
                bullets.add(new Bullet(assets, new Vector2(position.x, position.y), new Vector2(600f, 0f)));
                break;
            case QUICK_SHOT:
                bullets.add(new Bullet(assets, new Vector2(position.x, position.y), new Vector2(900f, 0f)));
                break;
        }
    }
}
