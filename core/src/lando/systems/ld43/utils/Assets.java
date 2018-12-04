package lando.systems.ld43.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
    public TextureRegion smoke;
    public TextureRegion shotRed;
    public TextureRegion shotMagenta;
    public TextureRegion shotCyan;
    public TextureRegion satelliteShip;
    public TextureRegion spreadBullet;
    public TextureRegion satelliteLaserBullet;
    public TextureRegion satelliteSpreadShip;
    public TextureRegion laser;
    public TextureRegion laserContinue;
    public TextureRegion droneFire;
    public TextureRegion droneLaser;
    public TextureRegion droneMissile;
    public TextureRegion droneSpread;
    public TextureRegion shotFire;
    public TextureRegion shotLaser;
    public TextureRegion shotMissile;
    public TextureRegion shotSpread;
    public TextureRegion powerUpHealth;
    public TextureRegion powerUpCooldown;
    public TextureRegion powerUpShield;
    public TextureRegion powerUpSpeed;
    public TextureRegion shipEnemyPlane;
    public TextureRegion shipEnemyUFO;
    public TextureRegion shipEnemyStar;
    public TextureRegion shipEnemyCube;
    public TextureRegion specialBoss;
    public TextureRegion progressCatLeft;
    public TextureRegion progressCatRight;
    public TextureRegion progressCatCenter;
    public TextureRegion progressDogLeft;
    public TextureRegion progressDogRight;
    public TextureRegion progressDogCenter;
    public TextureRegion iconX;
    public TextureRegion iconHeart;
    public TextureRegion iconBeam;
    public TextureRegion iconLaser;
    public TextureRegion iconMinibossCat;
    public TextureRegion iconMinibossDog;
    public TextureRegion iconFinalboss;
    public TextureRegion asteroid;
    public TextureRegion shotYellow;
    public TextureRegion pointer;
    public TextureRegion finalBoss;

    public Animation<TextureRegion> animationFinalBoss;
    public Animation<TextureRegion> animationShield;
    public Animation<TextureRegion> explosionAnimation;
    public Animation<TextureRegion> animationPortraitCat;
    public Animation<TextureRegion> animationPortraitDog;
    public Animation<TextureRegion> animationComputer;
    public Animation<TextureRegion> animationRaptor;
    public Animation<TextureRegion> animationOwl;
    public Animation<TextureRegion> animationRacoon;
    public Animation<TextureRegion> badLogicAnimation;

    public NinePatch ninePatch;

    public Sound laserSound;
    public Sound explosionSmall;
    public Sound explosionBig;
    public Sound warning;
    public Sound shot;
    public Sound hitSound;

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

        mgr.load("audio/laser-shot.mp3", Sound.class);
        mgr.load("audio/explosion-small.mp3", Sound.class);
        mgr.load("audio/explosion-final.mp3", Sound.class);
        mgr.load("audio/warning.mp3", Sound.class);
        mgr.load("audio/hit-sound.mp3", Sound.class);
        mgr.load("audio/shot.mp3", Sound.class);


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
        smoke = atlas.findRegion("smoke");
        shotRed = atlas.findRegion("regular-shot");
        satelliteShip = atlas.findRegion("satellite");
        spreadBullet = atlas.findRegion("spread_bullet");
        satelliteLaserBullet = atlas.findRegion("satellite_laser");
        satelliteSpreadShip = atlas.findRegion("satellite_spread");
        laser = atlas.findRegion("laser");
        laserContinue = atlas.findRegion("laser-continue");
        droneFire = atlas.findRegion("drone-fire");
        droneLaser = atlas.findRegion("drone-laser");
        droneMissile = atlas.findRegion("drone-missile");
        droneSpread = atlas.findRegion("drone-spread");
        shotFire = atlas.findRegion("shot-fire");
        shotLaser = atlas.findRegion("shot-laser");
        shotMissile = atlas.findRegion("shot-missile");
        shotSpread = atlas.findRegion("shot-spread");
        powerUpHealth = atlas.findRegion("powerup-health");
        powerUpCooldown = atlas.findRegion("powerup-cooldown");
        powerUpShield = atlas.findRegion("powerup-shield");
        powerUpSpeed = atlas.findRegion("powerup-speed");
        shipEnemyPlane = atlas.findRegion("ship-enemy-plane");
        shipEnemyUFO = atlas.findRegion("ship-enemy-ufo");
        shipEnemyStar = atlas.findRegion("ship-enemy-star");
        shipEnemyCube = atlas.findRegion("ship-enemy-cube");
        progressCatLeft = atlas.findRegion("progress-cat-left");
        progressCatRight = atlas.findRegion("progress-cat-right");
        progressCatCenter = atlas.findRegion("progress-cat-center");
        progressDogLeft = atlas.findRegion("progress-dog-left");
        progressDogRight = atlas.findRegion("progress-dog-right");
        progressDogCenter = atlas.findRegion("progress-dog-center");
        iconX = atlas.findRegion("icon-x");
        iconHeart = atlas.findRegion("icon-heart");
        iconBeam = atlas.findRegion("icon-beam");
        iconLaser = atlas.findRegion("icon-laser");
        iconMinibossCat = atlas.findRegion("icon-miniboss-cat");
        iconMinibossDog = atlas.findRegion("icon-miniboss-dog");
        iconFinalboss = atlas.findRegion("icon-finalboss");
        asteroid = atlas.findRegion("asteroid");
        pointer = atlas.findRegion("pointer");
        shotYellow = atlas.findRegion("shot-yellow");
        shotMagenta = atlas.findRegion("shot-magenta");
        shotCyan = atlas.findRegion("shot-cyan");
        specialBoss = atlas.findRegion("boss-seal");
        finalBoss = atlas.findRegion("final-boss");

        titleTexture = mgr.get(titleTextureAsset);
        pixelTexture = mgr.get(pixelTextureAsset);

        Array<TextureAtlas.AtlasRegion> animationbossTextures = atlas.findRegions("final-boss");
        animationFinalBoss = new Animation<TextureRegion>(0.1f, animationbossTextures, Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> animationShieldTextures = atlas.findRegions("shield");
        animationShield = new Animation<TextureRegion>(0.1f, animationShieldTextures, Animation.PlayMode.LOOP);
        Array<TextureAtlas.AtlasRegion> explosion = atlas.findRegions("explosion");
        explosionAnimation = new Animation<TextureRegion>(.1f, explosion, Animation.PlayMode.REVERSED);

        Array<TextureAtlas.AtlasRegion> talkingCat = atlas.findRegions("portrait-cat");
        animationPortraitCat = new Animation<TextureRegion>(0.1f, talkingCat, Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> talkingDog = atlas.findRegions("portrait-dog");
        animationPortraitDog = new Animation<TextureRegion>(0.1f, talkingDog, Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> talkingComputer = atlas.findRegions("portrait-computer");
        animationComputer = new Animation<TextureRegion>(0.1f, talkingComputer, Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> animationRaptorTextures = atlas.findRegions("portrait-raptor");
        animationRaptor = new Animation<TextureRegion>(0.1f, animationRaptorTextures, Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> animationOwlTextures = atlas.findRegions("portrait-owl");
        animationOwl = new Animation<TextureRegion>(0.1f, animationOwlTextures, Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> animationRacoonTextures = atlas.findRegions("portrait-racoon");
        animationRacoon = new Animation<TextureRegion>(0.1f, animationRacoonTextures, Animation.PlayMode.LOOP);

        // TODO: remove me
        Array<TextureAtlas.AtlasRegion> talkingBadLogic = atlas.findRegions("badlogic");
        badLogicAnimation = new Animation<TextureRegion>(0.1f, talkingBadLogic, Animation.PlayMode.LOOP);

        ninePatch = new NinePatch(atlas.findRegion("ninepatch-screws"), 6, 6, 6, 6);

        laserSound = mgr.get("audio/laser-shot.mp3", Sound.class);
        explosionSmall = mgr.get("audio/explosion-small.mp3", Sound.class);
        explosionBig = mgr.get("audio/explosion-final.mp3", Sound.class);
        warning = mgr.get("audio/warning.mp3", Sound.class);
        hitSound = mgr.get("audio/hit-sound.mp3", Sound.class);
        shot = mgr.get("audio/shot.mp3", Sound.class);



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

    // ------------------------------------------------------------------------
    // Static helpers methods
    // ------------------------------------------------------------------------

    public void drawString(SpriteBatch batch, String text,
                                  float x, float y, Color c, float scale, BitmapFont font) {
        batch.setShader(fontShader);
        fontShader.setUniformf("u_scale", scale);
        font.getData().setScale(scale);
        font.setColor(c);
        font.draw(batch, text, x, y);
        font.getData().setScale(1f);
        fontShader.setUniformf("u_scale", 1f);
        font.getData().setScale(scale);
        batch.setShader(null);
    }

    public void drawString(SpriteBatch batch, String text,
                                  float x, float y, Color c, float scale,
                                  BitmapFont font, float targetWidth, int halign) {
        batch.setShader(fontShader);
        fontShader.setUniformf("u_scale", scale);
        font.getData().setScale(scale);
        font.setColor(c);
        font.draw(batch, text, x, y, targetWidth, halign, true);
        font.getData().setScale(1f);
        fontShader.setUniformf("u_scale", 1f);
        font.getData().setScale(scale);
        batch.setShader(null);
    }


}
