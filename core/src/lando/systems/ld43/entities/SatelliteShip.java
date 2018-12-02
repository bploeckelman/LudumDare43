package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SatelliteShip {
    public enum EShipTypes {
        TRIPLE_SHOT,
        STRAIGHT_SHOT,
        QUICK_SHOT
    }

    public float xPosOffset;
    public float yPosOffset;
    public TextureRegion texture;
    public EShipTypes shipType;
    public Vector2 position;
    public float width;
    public float height;

    public SatelliteShip(TextureRegion texture, Vector2 playerPosition, float xPosOffset, float yPosOffset, EShipTypes shipType) {
        this.texture = texture;
        this.xPosOffset = xPosOffset;
        this.yPosOffset = yPosOffset;
        width = height = 25;
        this.position = new Vector2(playerPosition.x + xPosOffset, playerPosition.y + yPosOffset);
        this.shipType = shipType;
    }

    public void update(float dt) {
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width, height);
    }

    public void updatePosition(Vector2 playerPosition) {
        position.x = playerPosition.x + xPosOffset;
        position.y = playerPosition.y + yPosOffset;
    }
}
