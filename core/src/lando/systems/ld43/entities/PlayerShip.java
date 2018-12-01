package lando.systems.ld43.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld43.utils.Assets;

public class PlayerShip {
    public Vector2 position;
    Assets assets;
    public float width;
    public float height;

    private Vector2 tempVec2;
    private Vector3 tempVec3;

    public PlayerShip(Assets assets, Vector2 position) {
        this.assets = assets;
        this.position = position;
        this.width = this.height = 20;
        tempVec2 = new Vector2();
        tempVec3 = new Vector3();
    }


    public void update(float dt, Vector2 mousePos) {
        position.lerp(mousePos, .1f);


    }


    public void render(SpriteBatch batch) {
        batch.draw(assets.whitePixel, position.x - width/2, position.y - height/2, width, height);
    }
}
