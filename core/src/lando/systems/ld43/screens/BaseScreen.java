package lando.systems.ld43.screens;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld43.LudumDare43;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Config;

/**
 * Created by Brian on 7/25/2017
 */
public abstract class BaseScreen extends InputAdapter {

    public final LudumDare43 game;
    public final Assets assets;

    public MutableFloat alpha;
    public OrthographicCamera worldCamera;
    public OrthographicCamera hudCamera;
    public boolean allowInput;

    protected static final float MAX_ZOOM = 2f;
    protected static final float MIN_ZOOM = 1.1f;
    private static final float ZOOM_LERP = .02f;
    private static final float PAN_LERP = .02f;

    public Vector3 cameraTargetPos = new Vector3();
    public MutableFloat targetZoom = new MutableFloat(1f);

    public BaseScreen(LudumDare43 game, Assets assets) {
        super();
        this.game = game;
        this.assets = assets;

        this.allowInput = false;
        this.alpha = new MutableFloat(0f);

        float aspect = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        this.worldCamera = new OrthographicCamera();
        this.worldCamera.setToOrtho(false, Config.window_width, Config.window_height);
//        this.worldCamera.translate(this.worldCamera.viewportWidth / 2f, this.worldCamera.viewportHeight / 2f, 0f);
        this.worldCamera.update();

        this.hudCamera = new OrthographicCamera();
        this.hudCamera.setToOrtho(false, Config.window_width, Config.window_height);
//        this.hudCamera.translate(this.hudCamera.viewportWidth / 2f, this.hudCamera.viewportHeight / 2f, 0f);
        this.hudCamera.update();
    }

    public void setInputProcessors() {
        Gdx.input.setInputProcessor(this);
    }

    public void clearInputProcessors() {
        Gdx.input.setInputProcessor(null);
    }

    public abstract void update(float dt);
    public abstract void render(SpriteBatch batch);

    protected void updateCamera() {
        worldCamera.zoom = MathUtils.lerp(worldCamera.zoom, targetZoom.floatValue(), ZOOM_LERP);
        worldCamera.zoom = MathUtils.clamp(worldCamera.zoom, MIN_ZOOM, MAX_ZOOM);

        worldCamera.position.x = MathUtils.lerp(worldCamera.position.x, cameraTargetPos.x, PAN_LERP);
        worldCamera.position.y = MathUtils.lerp(worldCamera.position.y, cameraTargetPos.y, PAN_LERP);
        worldCamera.update();
    }

}
