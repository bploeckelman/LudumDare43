package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.utils.Assets;

public abstract class GameObject {

    private final Assets assets;

    public Vector2 position; // This should be the center of the object
    public float width;
    public float height;
    public TextureRegion texture;

    public GameObject(Assets assets){
        this.assets = assets;
        this.position = new Vector2();
        this.width = this.height = 10;
        this.texture = assets.whitePixel;
    }

    public abstract void update(float dt);

    public void render(SpriteBatch batch) {}

}
