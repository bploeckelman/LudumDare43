package lando.systems.ld43.ui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld43.accessors.RectangleAccessor;
import lando.systems.ld43.entities.Pilot;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.screens.TitleScreen;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Audio;

public class PilotSelectUI extends UserInterface {

    private enum Selected { none, cat, dog }

    private final float margin = 10f;

    private TitleScreen screen;
    private NinePatch border;
    private Selected selected;
    private Pilot.Type selectedPilotType;
    private Vector3 mousePos;
    private MutableFloat pulser;
    private MutableFloat alpha;
    private boolean launchButtonActive;
    private boolean launchButtonHidden;
    private boolean showPilots;
    private boolean transitionComplete;
    private boolean hiding;

    private TextureRegion texturePointer;
    private TextureRegion textureCat;
    private TextureRegion textureDog;
    private Rectangle boundsCat;
    private Rectangle boundsDog;
    private Rectangle boundsLaunchButton;


    public PilotSelectUI(TitleScreen screen) {
        super(screen.assets);

        this.selected = Selected.none;
        this.selectedPilotType = null;
        this.border = assets.ninePatch;
        this.mousePos = new Vector3();
        this.pulser = new MutableFloat(1f);
        this.alpha = new MutableFloat(0f);
        this.launchButtonActive = false;
        this.launchButtonHidden = true;
        this.showPilots = false;
        this.transitionComplete = false;
        this.hiding = false;

        this.texturePointer = assets.pointer;
        this.textureCat = assets.atlas.findRegion("spacesuit-cat");
        this.textureDog = assets.atlas.findRegion("spacesuit-dog");
        if (textureCat == null) throw new GdxRuntimeException("Couldn't find sprite: 'spacesuit-cat'");
        if (textureDog == null) throw new GdxRuntimeException("Couldn't find sprite: 'spacesuit-dog'");

        this.boundsCat = new Rectangle();
        this.boundsDog = new Rectangle();
        this.boundsLaunchButton = new Rectangle();
    }

    public PilotSelectUI reset(TitleScreen screen) {
        this.screen = screen;
        this.selected = Selected.none;
        this.selectedPilotType = null;
        this.launchButtonActive = false;
        this.touchPos.set(-1f, -1f, 0f);
        this.bounds.set(screen.hudCamera.viewportWidth / 2f, screen.hudCamera.viewportHeight / 2f, 0f, 0f);
        this.boundsCat.set((1f / 4f) * screen.hudCamera.viewportWidth, screen.hudCamera.viewportHeight / 2f, 0f, 0f);
        this.boundsDog.set((3f / 4f) * screen.hudCamera.viewportWidth, screen.hudCamera.viewportHeight / 2f, 0f, 0f);
        this.boundsLaunchButton.set(screen.hudCamera.viewportWidth / 2f,
                                    screen.hudCamera.viewportHeight - 2f * margin,
                                    0f, 0f);
        this.alpha.setValue(0f);
        this.pulser.setValue(1f);
        Tween.to(pulser, -1, 0.33f)
             .target(0.2f).repeatYoyo(-1, 0f)
             .start(screen.tween);
        return this;
    }

    @Override
    public void show() {
        super.show();

        float finalBorderW = screen.hudCamera.viewportWidth - 2f * margin;
        float finalBorderH = screen.hudCamera.viewportHeight - 2f * margin;
        float finalBorderX = margin;
        float finalBorderY = margin;

        float finalCatW = finalBorderW / 2f - 4f * margin;
        float finalCatH = finalBorderH - 8f * margin;
        float finalCatX = finalBorderX + 2f * margin;
        float finalCatY = finalBorderY + 6f * margin;

        float finalDogW = finalBorderW / 2f - 4f * margin;
        float finalDogH = finalBorderH - 8f * margin;
        float finalDogX = finalBorderX + finalBorderW - finalDogW - 2f * margin;
        float finalDogY = finalBorderY + 6f * margin;

        float finalLaunchW = finalBorderW - 4f * margin;
        float finalLaunchH = 4f * margin;
        float finalLaunchX = finalBorderX + 2f * margin;
        float finalLaunchY = finalBorderY + margin;

        alpha.setValue(0f);

        showPilots = false;
        transitionComplete = false;
        Timeline.createSequence()
                .push(
                        Timeline.createParallel()
                                .push(
                                        Tween.to(alpha, -1, 0.4f).target(1f)
                                )
                                .push(
                                        Tween.to(bounds, RectangleAccessor.XYWH, 0.5f)
                                             .target(finalBorderX, finalBorderY, finalBorderW, finalBorderH)
                                             .ease(Bounce.OUT)
                                )
                )
                .push(
                        Tween.call(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                showPilots = true;
                            }
                        })
                )
                .push(
                       Timeline.createParallel()
                               .push(
                                       Tween.to(boundsCat, RectangleAccessor.XYWH, 0.33f)
                                            .target(finalCatX, finalCatY, finalCatW, finalCatH)
                                            .ease(Quad.OUT)
                               )
                               .push(
                                       Tween.to(boundsDog, RectangleAccessor.XYWH, 0.33f)
                                            .target(finalDogX, finalDogY, finalDogW, finalDogH)
                                            .ease(Quad.OUT)
                               )
                )
                .push(
                        Timeline.createSequence()
                                .push(Tween.call(new TweenCallback() {
                                    @Override
                                    public void onEvent(int i, BaseTween<?> baseTween) {
                                        launchButtonHidden = false;
                                    }
                                }))
                                .push(
                                        Tween.to(boundsLaunchButton, RectangleAccessor.XYWH, 0.2f)
                                             .target(finalLaunchX, finalLaunchY, finalLaunchW, finalLaunchH)
                                             .ease(Quad.OUT)
                                )
                )
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        selected = Selected.none;
                        transitionComplete = true;
                    }
                })
                .start(screen.tween);
    }

    @Override
    public void hide() {
        float centerX = screen.hudCamera.viewportWidth / 2f;
        float centerY = screen.hudCamera.viewportHeight / 2f;

        alpha.setValue(1f);

        hiding = true;
        showPilots = true;
        transitionComplete = false;
        Timeline.createSequence()
                .push(
                        Tween.to(boundsLaunchButton, RectangleAccessor.H, 0.1f).target(0f)
                             .setCallback(new TweenCallback() {
                                 @Override
                                 public void onEvent(int i, BaseTween<?> baseTween) {
                                     launchButtonHidden = true;
                                 }
                             })
                )
                .push(
                        Timeline.createParallel()
                                .push(
                                        Tween.to(boundsCat, RectangleAccessor.XYWH, 0.2f)
                                             .target(centerX, centerY, 0f, 0f)
                                             .ease(Quad.OUT)
                                )
                                .push(
                                        Tween.to(boundsDog, RectangleAccessor.XYWH, 0.2f)
                                             .target(centerX, centerY, 0f, 0f)
                                             .ease(Quad.OUT)
                                )
                )
                .push(
                        Tween.call(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                showPilots = false;
                            }
                        })
                )
                .push(
                        Timeline.createParallel()
                                .push(
                                        Tween.to(alpha, -1, 0.4f).target(0f)
                                )
                                .push(
                                        Tween.to(bounds, RectangleAccessor.XYWH, 0.5f)
                                             .target(centerX, centerY, 0f, 0f)
                                             .ease(Quad.OUT)
                                )
                )
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        PilotSelectUI.super.hide();
                        screen.game.setScreen(new GameScreen(screen.game, assets, selectedPilotType));
                        hiding = false;
                    }
                })
                .start(screen.tween);
    }

    @Override
    public void update(float dt) {
        if (screen == null) return;

        Gdx.input.setCursorPosition(
                (int) MathUtils.clamp(Gdx.input.getX(), 0, screen.hudCamera.viewportWidth - texturePointer.getRegionWidth()),
                (int) MathUtils.clamp(Gdx.input.getY(), 0, screen.hudCamera.viewportHeight - texturePointer.getRegionHeight()));

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
        screen.hudCamera.unproject(mousePos);

        // don't allow clicks until show() tweens are fully completed
        if (!transitionComplete) return;

        if (Gdx.input.justTouched()) {
            // Update touch position
            touchPos.set(mousePos);

            // Check for start button click first
            if (launchButtonActive && boundsLaunchButton.contains(touchPos.x, touchPos.y)) {
                if      (selected == Selected.cat) selectedPilotType = Pilot.Type.cat;
                else if (selected == Selected.dog) selectedPilotType = Pilot.Type.dog;
                if (selectedPilotType == null) {
                    throw new GdxRuntimeException("Invalid pilot type selected (shouldn't be able to get here)");
                }

                hide();
            }

            // Check for pilot selection click
            if (boundsCat.contains(touchPos.x, touchPos.y)) {
                selected = Selected.cat;
                screen.audio.playSound(Audio.Sounds.cat_meow);
            } else if (boundsDog.contains(touchPos.x, touchPos.y)) {
                selected = Selected.dog;
                screen.audio.playSound(Audio.Sounds.dog_bork);
            } else {
                selected = Selected.none;
            }
            launchButtonActive = (selected != Selected.none);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isVisible()) return;

        // background
        batch.setColor(0xbf / 255f, 0xbf / 255f, 0xbf / 255f, alpha.floatValue());
        batch.draw(assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(1f, 1f, 1f, alpha.floatValue());
        border.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);

        if (showPilots) {
            // cat
            if (selected == Selected.cat) batch.setColor(1f, 1f, 0f, pulser.floatValue());
            else                          batch.setColor(Color.DARK_GRAY);
            batch.draw(assets.whitePixel, boundsCat.x, boundsCat.y, boundsCat.width, boundsCat.height);
            batch.setColor(Color.WHITE);
            border.draw(batch, boundsCat.x, boundsCat.y, boundsCat.width, boundsCat.height);
            batch.draw(textureCat, boundsCat.x, boundsCat.y, boundsCat.width, boundsCat.height);
            // TODO: draw flavor text

            if (selected == Selected.dog) batch.setColor(1f, 1f, 0f, pulser.floatValue());
            else                          batch.setColor(Color.DARK_GRAY);
            batch.draw(assets.whitePixel, boundsDog.x, boundsDog.y, boundsDog.width, boundsDog.height);
            batch.setColor(Color.WHITE);
            border.draw(batch, boundsDog.x, boundsDog.y, boundsDog.width, boundsDog.height);
            batch.draw(textureDog, boundsDog.x, boundsDog.y, boundsDog.width, boundsDog.height);
            // TODO: draw flavor text
        }

        // draw start button
        if (!launchButtonHidden) {
            if (launchButtonActive) batch.setColor(0f, 1f, 0f, pulser.floatValue());
            else                    batch.setColor(Color.GRAY);
            batch.draw(assets.whitePixel, boundsLaunchButton.x, boundsLaunchButton.y, boundsLaunchButton.width, boundsLaunchButton.height);
            batch.setColor(Color.WHITE);
            border.draw(batch, boundsLaunchButton.x, boundsLaunchButton.y, boundsLaunchButton.width, boundsLaunchButton.height);

            if (showPilots && !hiding) {
                String buttonText = "Choose your pilot...";
                if (launchButtonActive) {
                    buttonText = "Pilot: ";
                    switch (selected) {
                        case cat: buttonText += Selected.cat.name() ; break;
                        case dog: buttonText += Selected.dog.name() ; break;
                        // TODO: this can happen if player clicks on cat/dog before show() tween is done, don't allow clicks until tweens are fully done
                        default:  buttonText += "[UNKNOWN]";
                    }
                    buttonText += ", launch!";
                }
                layout.setText(assets.fontPixel32, buttonText, Color.DARK_GRAY, boundsLaunchButton.width, Align.center, false);
                assets.fontPixel32.draw(batch, layout, boundsLaunchButton.x, boundsLaunchButton.y + boundsLaunchButton.height / 2f + layout.height / 2f);
            }
        }

        batch.draw(texturePointer, mousePos.x, mousePos.y - texturePointer.getRegionHeight());
    }

}
