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
    public Assets assets;
    public EShipTypes shipType;

    public SatelliteShip(Assets assets, Vector2 playerPosition, float xPosOffset, float yPosOffset, EShipTypes shipType) {
        super(assets);
        this.texture = assets.satelliteShip;
        this.xPosOffset = xPosOffset;
        this.yPosOffset = yPosOffset;
        width = height = 25;
        this.assets = assets;
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
