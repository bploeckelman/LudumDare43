package lando.systems.ld43.ui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lando.systems.ld43.accessors.RectangleAccessor;
import lando.systems.ld43.entities.Discourser;
import lando.systems.ld43.entities.Pilot;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Assets;

import java.util.ArrayList;

public class DialogUI extends UserInterface {

    private final float margin = 10f;
    private final float threshold = 0.05f;
    private final float minScale = 0.9f;
    private final float maxScale = 1.1f;

    private GameScreen screen;
    private NinePatch border;
    private Json json;
    private Pilot pilot;
    // TODO: enemy(s)?
    private TextureRegion keyFrame;
    private Discourser system;
    private Discourser boss1;
    private Discourser boss2;
    private Discourser boss3;
    private Discourser boss4;
    private Discourser finalBoss;
    private Discourser specialBoss;
    private TextureRegion leftMouse;
    private MutableFloat scale;
    private ArrayList<Dialog> dialogs;
    private int currentDialog;
    private int typingIndex;
    private float typingTimer;
    private float animationTimer;
    private boolean typing;
    private boolean transitioning;

    public DialogUI(Assets assets, GameScreen screen) {
        super(assets);

        this.border = assets.ninePatch;
        this.leftMouse = assets.atlas.findRegion("mouse-left");
        this.scale = new MutableFloat(1f);
        this.dialogs = new ArrayList<Dialog>();
        this.currentDialog = -1;
        this.typingIndex = -1;
        this.typingTimer = 0f;
        this.animationTimer = 0f;
        this.typing = false;
        this.transitioning = false;
        this.json = new Json();
        this.json.addClassTag("Dialog", Dialog.class);
        this.screen = screen;
        scale.setValue(minScale);
        Tween.to(scale, -1, 0.33f)
                .target(maxScale).repeatYoyo(-1, 0f)
                .start(screen.tween);
    }

    public DialogUI reset(GameScreen screen, String dialogFile) {
        this.screen = screen;
        this.pilot = screen.player.pilot;
        this.system = new Discourser(assets, Discourser.Type.system);
        this.boss1 = new Discourser(assets, Discourser.Type.boss1);
        this.boss2 = new Discourser(assets, Discourser.Type.boss2);
        this.boss3 = new Discourser(assets, Discourser.Type.boss3);
        this.boss4 = new Discourser(assets, Discourser.Type.boss4);
        this.finalBoss = new Discourser(assets, Discourser.Type.finalBoss);
        this.specialBoss = new Discourser(assets, Discourser.Type.specialBoss);
        this.bounds.set(screen.hudCamera.viewportWidth / 2f, screen.hudCamera.viewportHeight / 2f, 0f, 0f);
        this.typing = false;
        this.transitioning = false;

        dialogs.clear();
        dialogs = json.fromJson(ArrayList.class, Gdx.files.internal("dialog/" + dialogFile));

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

        if (currentDialog == -1) return;

        // update type timer
        int lastLetter = dialogs.get(currentDialog).text.length();
        if (typing) {
            typingTimer += dt;
            animationTimer += dt;
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
                if (currentDialog >= dialogs.size()) {
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
                if (typing) {
                    keyFrame = pilot.textureAnimation.getKeyFrame(animationTimer);
                    batch.draw(keyFrame, bounds.x + margin, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                } else {
                    batch.draw(pilot.textureAnimation.getKeyFrame(0), bounds.x + margin, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                }
                float textWidth = bounds.width - iconW - 3f * margin;
                layout.setText(assets.fontPixel16, dialog.text.substring(0, typingIndex), Color.LIGHT_GRAY, textWidth, Align.left, true);
                assets.fontPixel16.draw(batch, layout,
                                        bounds.x + iconW + 2f * margin,
                                        bounds.y + bounds.height / 2f + layout.height / 2f);
            }
            else if (dialog.speaker == Speaker.system) {
                if (typing) {
                    keyFrame = system.textureAnimation.getKeyFrame(animationTimer);
                    batch.draw(keyFrame, bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                } else {
                    batch.draw(system.textureAnimation.getKeyFrame(0), bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                }
                float textWidth = bounds.width - iconW - 3f * margin;
                layout.setText(assets.fontPixel16, dialog.text.substring(0, typingIndex), Color.LIGHT_GRAY, textWidth, Align.right, true);
                assets.fontPixel16.draw(batch, layout,
                        bounds.x + bounds.width - 3f * margin - iconW - textWidth,
                        bounds.y + bounds.height / 2f + layout.height / 2f);
            }
            else if (dialog.speaker == Speaker.boss1) {
                if (typing) {
                    keyFrame = boss1.textureAnimation.getKeyFrame(animationTimer);
                    batch.draw(keyFrame, bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                } else {
                    batch.draw(boss1.textureAnimation.getKeyFrame(0), bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                }
                float textWidth = bounds.width - iconW - 3f * margin;
                layout.setText(assets.fontPixel16, dialog.text.substring(0, typingIndex), Color.LIGHT_GRAY, textWidth, Align.right, true);
                assets.fontPixel16.draw(batch, layout,
                        bounds.x + bounds.width - 3f * margin - iconW - textWidth,
                        bounds.y + bounds.height / 2f + layout.height / 2f);
            }
            else if (dialog.speaker == Speaker.boss2) {
                if (typing) {
                    keyFrame = boss2.textureAnimation.getKeyFrame(animationTimer);
                    batch.draw(keyFrame, bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                } else {
                    batch.draw(boss2.textureAnimation.getKeyFrame(0), bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                }
                float textWidth = bounds.width - iconW - 3f * margin;
                layout.setText(assets.fontPixel16, dialog.text.substring(0, typingIndex), Color.LIGHT_GRAY, textWidth, Align.right, true);
                assets.fontPixel16.draw(batch, layout,
                        bounds.x + bounds.width - 3f * margin - iconW - textWidth,
                        bounds.y + bounds.height / 2f + layout.height / 2f);
            }
            else if (dialog.speaker == Speaker.boss3) {
                if (typing) {
                    keyFrame = boss3.textureAnimation.getKeyFrame(animationTimer);
                    batch.draw(keyFrame, bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                } else {
                    batch.draw(boss3.textureAnimation.getKeyFrame(0), bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                }
                float textWidth = bounds.width - iconW - 3f * margin;
                layout.setText(assets.fontPixel16, dialog.text.substring(0, typingIndex), Color.LIGHT_GRAY, textWidth, Align.right, true);
                assets.fontPixel16.draw(batch, layout,
                        bounds.x + bounds.width - 3f * margin - iconW - textWidth,
                        bounds.y + bounds.height / 2f + layout.height / 2f);
            }
            else if (dialog.speaker == Speaker.boss4) {
                if (typing) {
                    keyFrame = boss4.textureAnimation.getKeyFrame(animationTimer);
                    batch.draw(keyFrame, bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                } else {
                    batch.draw(boss4.textureAnimation.getKeyFrame(0), bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                }
                float textWidth = bounds.width - iconW - 3f * margin;
                layout.setText(assets.fontPixel16, dialog.text.substring(0, typingIndex), Color.LIGHT_GRAY, textWidth, Align.right, true);
                assets.fontPixel16.draw(batch, layout,
                        bounds.x + bounds.width - 3f * margin - iconW - textWidth,
                        bounds.y + bounds.height / 2f + layout.height / 2f);
            }
            else if (dialog.speaker == Speaker.finalBoss) {
                if (typing) {
                    keyFrame = finalBoss.textureAnimation.getKeyFrame(animationTimer);
                    batch.draw(keyFrame, bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                } else {
                    batch.draw(finalBoss.textureAnimation.getKeyFrame(0), bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                }
                float textWidth = bounds.width - iconW - 3f * margin;
                layout.setText(assets.fontPixel16, dialog.text.substring(0, typingIndex), Color.LIGHT_GRAY, textWidth, Align.right, true);
                assets.fontPixel16.draw(batch, layout,
                        bounds.x + bounds.width - 3f * margin - iconW - textWidth,
                        bounds.y + bounds.height / 2f + layout.height / 2f);
            }
            else if (dialog.speaker == Speaker.specialBoss) {
                if (typing) {
                    keyFrame = specialBoss.textureAnimation.getKeyFrame(animationTimer);
                    batch.draw(keyFrame, bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                } else {
                    batch.draw(specialBoss.textureAnimation.getKeyFrame(0), bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);
                }
                float textWidth = bounds.width - iconW - 3f * margin;
                layout.setText(assets.fontPixel16, dialog.text.substring(0, typingIndex), Color.LIGHT_GRAY, textWidth, Align.right, true);
                assets.fontPixel16.draw(batch, layout,
                        bounds.x + bounds.width - 3f * margin - iconW - textWidth,
                        bounds.y + bounds.height / 2f + layout.height / 2f);
            }
            else {
                batch.draw(pilot.textureHead, bounds.x + bounds.width - margin - iconW, bounds.y + bounds.height / 2f - iconH / 2f, iconW, iconH);

                float textWidth = bounds.width - iconW - 3f * margin;
                layout.setText(assets.fontPixel16, dialog.text.substring(0, typingIndex), Color.LIGHT_GRAY, textWidth, Align.right, true);
                assets.fontPixel16.draw(batch, layout,
                                        bounds.x + bounds.width - 3f * margin - iconW - textWidth,
                                        bounds.y + bounds.height / 2f + layout.height / 2f);
            }
            if (!typing) {
                float mouseW = 18f;
                float mouseH = 22f;
                float s = scale.floatValue();
                float x = bounds.x + bounds.width / 2f - (mouseW * s) / 2f;
                float y = bounds.y + margin;
                batch.setColor(34f / 255f, 139f / 255f, 32f / 255f, 0.1f + (s - minScale) / (maxScale - minScale));
                batch.draw(leftMouse, x, y, mouseW / 2f, mouseH / 2f, mouseW, mouseH, s, s, 0f);
            }
        }
    }

}
