package lando.systems.ld43.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {

    // Initialize descriptors for all assets
    private final AssetDescriptor<TextureAtlas> atlasAsset = new AssetDescriptor<TextureAtlas>("images/sprites.atlas", TextureAtlas.class);
    private final AssetDescriptor<Texture> titleTextureAsset = new AssetDescriptor<Texture>("images/title.png", Texture.class);
    private final AssetDescriptor<Texture> pixelTextureAsset = new AssetDescriptor<Texture>("images/pixel.png", Texture.class);

    private final AssetDescriptor<BitmapFont> distanceFieldFontAsset = new AssetDescriptor<BitmapFont>("fonts/ubuntu.fnt", BitmapFont.class,
            new BitmapFontLoader.BitmapFontParameter() {{
                genMipMaps = true;
                minFilter = Texture.TextureFilter.MipMapLinearLinear;
                magFilter = Texture.TextureFilter.Linear;
            }}
    );
    private final AssetDescriptor<BitmapFont> pixelFont8Asset = new AssetDescriptor<BitmapFont>("fonts/emulogic-8pt.fnt", BitmapFont.class);
    private final AssetDescriptor<BitmapFont> pixelFont16Asset = new AssetDescriptor<BitmapFont>("fonts/emulogic-16pt.fnt", BitmapFont.class);
    private final AssetDescriptor<BitmapFont> pixelFont32Asset = new AssetDescriptor<BitmapFont>("fonts/emulogic-32pt.fnt", BitmapFont.class);

    private final ShaderProgramLoader.ShaderProgramParameter defaultVertParam = new ShaderProgramLoader.ShaderProgramParameter() {{ vertexFile = "shaders/default.vert"; }};
    private final AssetDescriptor<ShaderProgram> distanceFieldShaderAsset = new AssetDescriptor<ShaderProgram>("shaders/dist.frag", ShaderProgram.class);
    private final AssetDescriptor<ShaderProgram> shaderBlindsAsset        = new AssetDescriptor<ShaderProgram>("shaders/blinds.frag", ShaderProgram.class, defaultVertParam);
    private final AssetDescriptor<ShaderProgram> shaderFadeAsset          = new AssetDescriptor<ShaderProgram>("shaders/dissolve.frag", ShaderProgram.class, defaultVertParam);
    private final AssetDescriptor<ShaderProgram> shaderRadialAsset        = new AssetDescriptor<ShaderProgram>("shaders/radial.frag", ShaderProgram.class, defaultVertParam);
    private final AssetDescriptor<ShaderProgram> shaderDoomAsset          = new AssetDescriptor<ShaderProgram>("shaders/doomdrip.frag", ShaderProgram.class, defaultVertParam);
    private final AssetDescriptor<ShaderProgram> shaderPixelizeAsset      = new AssetDescriptor<ShaderProgram>("shaders/pixelize.frag", ShaderProgram.class, defaultVertParam);
    private final AssetDescriptor<ShaderProgram> shaderDoorwayAsset       = new AssetDescriptor<ShaderProgram>("shaders/doorway.frag", ShaderProgram.class, defaultVertParam);
    private final AssetDescriptor<ShaderProgram> shaderCrosshatchAsset    = new AssetDescriptor<ShaderProgram>("shaders/crosshatch.frag", ShaderProgram.class, defaultVertParam);
    private final AssetDescriptor<ShaderProgram> shaderRippleAsset        = new AssetDescriptor<ShaderProgram>("shaders/ripple.frag", ShaderProgram.class, defaultVertParam);
    private final AssetDescriptor<ShaderProgram> shaderHeartAsset         = new AssetDescriptor<ShaderProgram>("shaders/heart.frag", ShaderProgram.class, defaultVertParam);
    private final AssetDescriptor<ShaderProgram> shaderCircleCropAsset    = new AssetDescriptor<ShaderProgram>("shaders/circlecrop.frag", ShaderProgram.class, defaultVertParam);
    private final AssetDescriptor<ShaderProgram> shaderWaterAsset         = new AssetDescriptor<ShaderProgram>("shaders/water.frag", ShaderProgram.class, defaultVertParam);

    public enum Loading { SYNC, ASYNC }

    public SpriteBatch batch;
    public ShapeRenderer shapes;
    public GlyphLayout layout;

    public AssetManager mgr;

    public TextureAtlas atlas;
    public Texture titleTexture;
    public Texture pixelTexture;

    public TextureRegion testTexture;
    public TextureRegion whitePixel;
    public TextureRegion whiteCircle;
    public TextureRegion redBullet;
    public TextureRegion satelliteShip;
    public TextureRegion spreadBullet;

//    public Animation<TextureRegion> animation;

    public NinePatch ninePatch;

    public BitmapFont font;
    public BitmapFont fontPixel8;
    public BitmapFont fontPixel16;
    public BitmapFont fontPixel32;
    public ShaderProgram fontShader;

    public Array<ShaderProgram> randomTransitions;
    public ShaderProgram blindsShader;
    public ShaderProgram fadeShader;
    public ShaderProgram radialShader;
    public ShaderProgram doomShader;
    public ShaderProgram crosshatchShader;
    public ShaderProgram pixelizeShader;
    public ShaderProgram doorwayShader;
    public ShaderProgram rippleShader;
    public ShaderProgram heartShader;
    public ShaderProgram circleCropShader;
    public ShaderProgram waterShader;

    public boolean initialized;

    public Assets() {
        this(Loading.SYNC);
    }

    public Assets(Loading loading) {
        // Let us write shitty shader programs
        ShaderProgram.pedantic = false;

        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        layout = new GlyphLayout();

        initialized = false;

        mgr = new AssetManager();
        mgr.load(atlasAsset);
        mgr.load(titleTextureAsset);
        mgr.load(pixelTextureAsset);
        mgr.load(distanceFieldFontAsset);
        mgr.load(distanceFieldShaderAsset);
        mgr.load(pixelFont8Asset);
        mgr.load(pixelFont16Asset);
        mgr.load(pixelFont32Asset);
        mgr.load(shaderBlindsAsset);
        mgr.load(shaderFadeAsset);
        mgr.load(shaderRadialAsset);
        mgr.load(shaderDoomAsset);
        mgr.load(shaderPixelizeAsset);
        mgr.load(shaderDoorwayAsset);
        mgr.load(shaderCrosshatchAsset);
        mgr.load(shaderRippleAsset);
        mgr.load(shaderHeartAsset);
        mgr.load(shaderCircleCropAsset);
        mgr.load(shaderWaterAsset);

        if (loading == Loading.SYNC) {
            mgr.finishLoading();
            updateLoading();
        }
    }

    public float updateLoading() {
        if (!mgr.update()) return mgr.getProgress();
        if (initialized) return 1f;
        initialized = true;

        // Cache TextureRegions from TextureAtlas in fields for quicker access
        atlas = mgr.get(atlasAsset);
        testTexture = atlas.findRegion("badlogic");
        whitePixel = atlas.findRegion("white-pixel");
        whiteCircle = atlas.findRegion("white-circle");
        redBullet = atlas.findRegion("regular-shot");
        satelliteShip = atlas.findRegion("satellite");
        spreadBullet = atlas.findRegion("spread_bullet");

        titleTexture = mgr.get(titleTextureAsset);
        pixelTexture = mgr.get(pixelTextureAsset);

//        Array<TextureAtlas.AtlasRegion> animationTextures = atlas.findRegions("animation...");
//        animation = new Animation<TextureRegion>(0.1f, animationTextures, Animation.PlayMode.LOOP_PINGPONG);

        ninePatch = new NinePatch(atlas.findRegion("ninepatch-screws"), 6, 6, 6, 6);

        // Initialize distance field font
        font = mgr.get(distanceFieldFontAsset);
        font.getData().setScale(.3f);
        font.setUseIntegerPositions(false);
        fontShader = mgr.get(distanceFieldShaderAsset);

        fontPixel8 = mgr.get(pixelFont8Asset);
        fontPixel16 = mgr.get(pixelFont16Asset);
        fontPixel32 = mgr.get(pixelFont32Asset);

        blindsShader     = mgr.get(shaderBlindsAsset);
        fadeShader       = mgr.get(shaderFadeAsset);
        radialShader     = mgr.get(shaderRadialAsset);
        doomShader       = mgr.get(shaderDoomAsset);
        pixelizeShader = mgr.get(shaderPixelizeAsset);
        doorwayShader    = mgr.get(shaderDoorwayAsset);
        crosshatchShader = mgr.get(shaderCrosshatchAsset);
        rippleShader     = mgr.get(shaderRippleAsset);
        heartShader      = mgr.get(shaderHeartAsset);
        circleCropShader = mgr.get(shaderCircleCropAsset);
        waterShader      = mgr.get(shaderWaterAsset);

        randomTransitions = new Array<ShaderProgram>();
        randomTransitions.addAll(
                blindsShader,
                fadeShader,
                radialShader,
                doomShader,
                pixelizeShader,
                doorwayShader,
                crosshatchShader,
                rippleShader,
                heartShader,
                circleCropShader
        );

        return 1f;
    }

    @Override
    public void dispose() {
        mgr.clear();
        font.dispose();
        shapes.dispose();
        batch.dispose();
    }

}
