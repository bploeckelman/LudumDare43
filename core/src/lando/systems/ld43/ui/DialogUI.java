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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld43.accessors.RectangleAccessor;
import lando.systems.ld43.entities.Pilot;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Assets;

public class DialogUI extends UserInterface {

    private final float margin = 10f;
    private final float threshold = 0.05f;

    public enum Speaker { player, enemy1, enemy2 }
    public class Dialog {
        public Speaker speaker;
        public String text;
        public Dialog(Speaker speaker, String text) {
            this.speaker = speaker;
            this.text = text;
        }
    }

    private GameScreen screen;
    private NinePatch border;
    private Pilot pilot;
    // TODO: enemy(s)?
    private Array<Dialog> dialogs;
    private int currentDialog;
    private int typingIndex;
    private float typingTimer;
    private boolean typing;
    private boolean transitioning;

    public DialogUI(Assets assets) {
        super(assets);

        this.border = assets.ninePatch;
        this.dialogs = new Array<Dialog>();
        this.currentDialog = -1;
        this.typingIndex = -1;
        this.typingTimer = 0f;
        this.typing = false;
        this.transitioning = false;
    }

    public DialogUI reset(GameScreen screen, String dialogFile) {
        this.screen = screen;
        this.pilot = screen.player.pilot;
        this.bounds.set(screen.hudCamera.viewportWidth / 2f, screen.hudCamera.viewportHeight / 2f, 0f, 0f);
        this.typing = false;
        this.transitioning = false;

        dialogs.clear();
        // TODO: open and read dialog file
        dialogs.add(new Dialog(Speaker.player, "I am doggo"));
        dialogs.add(new Dialog(Speaker.enemy1, "You must die now"));
        dialogs.add(new Dialog(Speaker.player, "but... I am doggo!"));
        dialogs.add(new Dialog(Speaker.enemy2, "Yeah, whatever, this is just for testing dialog anyway, don't take it too seriously"));
        currentDialog = 0;
        typingIndex = 0;
        typingTimer = 0f;

        return this;
    }

    @Override
    public void show() {
        super.show();

        transitioning = true;

        float finalBorderW = screen.hudCamera.viewportWidth - 2f * margin;
        float finalBorderH = screen.hudCamera.viewportHeight / 4f;
        float finalBorderX = margin;
        float finalBorderY = screen.hudCamera.viewportHeight / 2f - finalBorderH / 2f;

        Timeline.createSequence()
                .push(
                        Tween.to(bounds, RectangleAccessor.XYWH, 0.33f)
                             .target(finalBorderX, finalBorderY, finalBorderW, finalBorderH)
                             .ease(Bounce.OUT)
                )
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        transitioning = false;
                        typing = true;
                    }
                })
                .start(screen.tween);
    }

    @Override
    public void hide() {
        float centerX = screen.hudCamera.viewportWidth / 2f;
        float centerY = screen.hudCamera.viewportHeight / 2f;

        typing = false;
        transitioning = true;

        Timeline.createSequence()
                .push(
                        Tween.to(bounds, RectangleAccessor.XYWH, 0.2f)
                             .target(centerX, centerY, 0f, 0f)
                             .ease(Quad.OUT)
                )
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        DialogUI.super.hide();
                    }
                })
                .start(screen.tween);
    }

    @Override
    public void update(float dt) {
        if (screen == null) return;

        if (transitioning) return;

        // update type timer
        int lastLetter = dialogs.get(currentDialog).text.length();
        if (typing) {
            typingTimer += dt;
            if (typingTimer > threshold) {
                typingTimer -= threshold;
                typingIndex++;

                if (typingIndex > lastLetter) {
                    typingIndex = lastLetter ;
                    typing = false;
                }
            }
        }

        if (Gdx.input.justTouched()) {
            if (typing) {
                // show full text if still printing currentDialog
                typing = false;
                typingIndex = lastLetter;
            } else {
                // move to next dialog or hide if complete
                typing = true;
                typingIndex = 0;
                currentDialog++;
                if (currentDialog >= dialogs.size) {
                    hide();
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isVisible()) return;

        // background
        batch.setColor(Color.DARK_GRAY);
        batch.draw(assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
        border.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);

        if (!transitioning) {
            Dialog dialog = dialogs.get(currentDialog);
            float iconW = 64f;
            float iconH = 64f;
            if (dialog.speaker == Speaker.player) {
                batch.draw(pilot.textureHead, bounds.x + margin, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);

                float textWidth = bounds.width - iconW - 3f * margin;
                layout.setText(assets.fontPixel16, dialog.text.substring(0, typingIndex), Color.LIGHT_GRAY, textWidth, Align.left, true);
                assets.fontPixel16.draw(batch, layout,
                                        bounds.x + iconW + 2f * margin,
                                        bounds.y + bounds.height / 2f + layout.height / 2f);
            } else {
                batch.draw(pilot.textureHead, bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);

                float textWidth = bounds.width - iconW - 3f * margin;
                layout.setText(assets.fontPixel16, dialog.text.substring(0, typingIndex), Color.LIGHT_GRAY, textWidth, Align.right, true);
                assets.fontPixel16.draw(batch, layout,
                                        bounds.x + bounds.width - 3f * margin - iconW - textWidth,
                                        bounds.y + bounds.height / 2f + layout.height / 2f);
            }
        }
    }

}
