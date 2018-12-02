package lando.systems.ld43.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld43.utils.Assets;

public abstract class UserInterface {

    protected final Assets assets;
    protected final GlyphLayout layout;
    protected final Rectangle bounds;
    protected final Vector3 touchPos;

    private boolean visible;

    public UserInterface(Assets assets) {
        this.assets = assets;
        this.layout = new GlyphLayout();
        this.bounds = new Rectangle();
        this.touchPos = new Vector3(-1f, -1f, 0f);
        this.visible = false;
    }

    public abstract void update(float dt);
    public abstract void render(SpriteBatch batch);

    public void show() {
        visible = true;
    }

    public void hide() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isHidden() {
        return !visible;
    }

    static void drawText(Assets assets, SpriteBatch batch, String text,
                         float x, float y, Color c, float scale) {
        batch.setShader(assets.fontShader);
        assets.fontShader.setUniformf("u_scale", scale);
        assets.font.getData().setScale(scale);
        assets.font.setColor(c);
        {
            assets.font.draw(batch, text, x, y);
        }
        assets.font.setColor(1f, 1f, 1f, 1f);
        assets.font.getData().setScale(1f);
        assets.fontShader.setUniformf("u_scale", 1f);
        assets.font.getData().setScale(scale);
        batch.setShader(null);
    }

}
