package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.utils.Assets;

import javax.swing.text.Position;

public class SatelliteShip extends GameObject {
    public float xPosOffset;
    public float yPosOffset;

    public SatelliteShip(Assets assets, Vector2 playerPosition, float xPosOffset, float yPosOffset) {
        super(assets);
        this.texture = assets.whitePixel;
        this.xPosOffset = xPosOffset;
        this.yPosOffset = yPosOffset;
        this.position = new Vector2(playerPosition.x + xPosOffset, playerPosition.y + yPosOffset);
    }

    public void update(float dt) {
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width, height);
    }

    public void updatePosition(Vector2 playerPosition) {
        position = new Vector2(playerPosition.x + xPosOffset, playerPosition.y + yPosOffset);
    }
}
