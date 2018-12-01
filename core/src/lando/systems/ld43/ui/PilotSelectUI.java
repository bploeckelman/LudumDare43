package lando.systems.ld43.ui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld43.accessors.RectangleAccessor;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.screens.TitleScreen;
import lando.systems.ld43.utils.Assets;

public class PilotSelectUI extends UserInterface {

    private enum Selected { none, cat, dog }

    private TitleScreen screen;
    private NinePatch border;
    private Selected selected;
    private Vector3 touchPos;
    private boolean launchButtonActive;
    private boolean launchButtonHidden;
    private boolean showPilots;

    private TextureRegion textureCat;
    private TextureRegion textureDog;
    private Rectangle boundsCat;
    private Rectangle boundsDog;

    // TODO: extract button shit to class
    private Rectangle boundsLaunchButton;

    public PilotSelectUI(Assets assets) {
        super(assets);

        this.selected = Selected.none;
        this.border = assets.ninePatch;
        this.touchPos = new Vector3();
        this.launchButtonActive = false;
        this.launchButtonHidden = true;
        this.showPilots = false;

        this.textureCat = assets.atlas.findRegion("cat-full");
        this.textureDog = assets.atlas.findRegion("dog-full");
        if (textureCat == null) throw new GdxRuntimeException("Couldn't find sprite: 'cat-full'");
        if (textureDog == null) throw new GdxRuntimeException("Couldn't find sprite: 'dog-full'");

        this.boundsCat = new Rectangle();
        this.boundsDog = new Rectangle();
        this.boundsLaunchButton = new Rectangle();
    }

    public PilotSelectUI reset(TitleScreen screen) {
        this.screen = screen;
        this.selected = Selected.none;
        this.launchButtonActive = false;
        this.bounds.set(screen.hudCamera.viewportWidth / 2f, screen.hudCamera.viewportHeight / 2f, 0f, 0f);
        this.boundsCat.set((1f / 4f) * screen.hudCamera.viewportWidth, screen.hudCamera.viewportHeight / 2f, 0f, 0f);
        this.boundsDog.set((3f / 4f) * screen.hudCamera.viewportWidth, screen.hudCamera.viewportHeight / 2f, 0f, 0f);

        this.boundsLaunchButton.set(screen.hudCamera.viewportWidth / 2f,
                                    screen.hudCamera.viewportHeight - 10f - 10f,
                                    0f, 0f);

        return this;
    }

    @Override
    public void show() {
        super.show();

        float margin = 10f;

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

        showPilots = false;
        Timeline.createSequence()
                .push(
                        Tween.to(bounds, RectangleAccessor.XYWH, 1f)
                             .target(finalBorderX, finalBorderY, finalBorderW, finalBorderH)
                             .ease(Bounce.OUT)
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
                    }
                })
                .start(screen.game.tween);
    }

    @Override
    public void hide() {
        float centerX = screen.hudCamera.viewportWidth / 2f;
        float centerY = screen.hudCamera.viewportHeight / 2f;

        showPilots = true;
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
                        Tween.to(bounds, RectangleAccessor.XYWH, 0.5f)
                             .target(centerX, centerX, 0f, 0f)
                             .ease(Quad.OUT)
                )
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        PilotSelectUI.super.hide();

                        // TODO: pass selected pilot type
                        screen.game.setScreen(new GameScreen(screen.game, assets));
                    }
                })
                .start(screen.game.tween);
    }

    @Override
    public void update(float dt) {
        if (screen == null) return;

        if (Gdx.input.justTouched()) {
            // Update touch position
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
            screen.hudCamera.unproject(touchPos);

            // Check for start button click first
            if (launchButtonActive && boundsLaunchButton.contains(touchPos.x, touchPos.y)) {
                hide();
            }

            // Check for pilot selection click
            if (boundsCat.contains(touchPos.x, touchPos.y)) {
                selected = Selected.cat;
            } else if (boundsDog.contains(touchPos.x, touchPos.y)) {
                selected = Selected.dog;
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
        batch.setColor(Color.LIGHT_GRAY);
        batch.draw(assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
        border.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);

        if (showPilots) {
            // cat
            batch.setColor((selected == Selected.cat) ? Color.YELLOW : Color.DARK_GRAY);
            batch.draw(assets.whitePixel, boundsCat.x, boundsCat.y, boundsCat.width, boundsCat.height);
            batch.setColor(Color.WHITE);
            border.draw(batch, boundsCat.x, boundsCat.y, boundsCat.width, boundsCat.height);
            batch.draw(textureCat, boundsCat.x, boundsCat.y, boundsCat.width, boundsCat.height);
            // TODO: draw flavor text

            batch.setColor((selected == Selected.dog) ? Color.YELLOW : Color.DARK_GRAY);
            batch.draw(assets.whitePixel, boundsDog.x, boundsDog.y, boundsDog.width, boundsDog.height);
            batch.setColor(Color.WHITE);
            border.draw(batch, boundsDog.x, boundsDog.y, boundsDog.width, boundsDog.height);
            batch.draw(textureDog, boundsDog.x, boundsDog.y, boundsDog.width, boundsDog.height);
            // TODO: draw flavor text
        }

        // draw start button
        if (!launchButtonHidden) {
            if (launchButtonActive) batch.setColor(Color.GREEN);
            else                    batch.setColor(Color.GRAY);
            batch.draw(assets.whitePixel, boundsLaunchButton.x, boundsLaunchButton.y, boundsLaunchButton.width, boundsLaunchButton.height);
            batch.setColor(Color.WHITE);
            border.draw(batch, boundsLaunchButton.x, boundsLaunchButton.y, boundsLaunchButton.width, boundsLaunchButton.height);

            if (showPilots) {
                String buttonText = "Choose your pilot...";
                if (launchButtonActive) {
                    buttonText = "Pilot: ";
                    switch (selected) {
                        case cat: buttonText += Selected.cat.name() ; break;
                        case dog: buttonText += Selected.dog.name() ; break;
                        default:  buttonText += "[UNKNOWN]";
                    }
                    buttonText += ", launch!";
                }
                layout.setText(assets.fontPixel32, buttonText, Color.DARK_GRAY, boundsLaunchButton.width, Align.center, false);
                assets.fontPixel32.draw(batch, layout, boundsLaunchButton.x, boundsLaunchButton.y + boundsLaunchButton.height / 2f + layout.height / 2f);
            }
        }
    }

}
