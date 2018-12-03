package lando.systems.ld43.ui;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Sine;
import aurelienribon.tweenengine.primitives.MutableFloat;
import aurelienribon.tweenengine.primitives.MutableInteger;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld43.LudumDare43;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Config;

public class ScoreUI extends UserInterface {
    public int score;
    public GameScreen screen;
    private MutableInteger mutScore;

    public ScoreUI(Assets assets, GameScreen screen) {
        super(assets);
        this.score = 0;
        this.screen = screen;
        this.mutScore = new MutableInteger(0);
    }

    public void update(float dt) {
        score = mutScore.intValue();
    }

    public void render(SpriteBatch batch) {
        String text = "Score: " + score;
        layout.setText(assets.fontPixel16, text);
        assets.fontPixel16.draw(batch, layout,
                                screen.hudCamera.viewportWidth - layout.width - 10f,
                                screen.hudCamera.viewportHeight - 10f - screen.progressUI.bounds.height - 10f);
    }

    public void resetScore() {
        Tween.to(mutScore, 1, 1f).target(0).ease(Sine.OUT).start(screen.tween);
    }

    public void addScore(int amount) {
        mutScore.setValue(mutScore.intValue() + amount);
    }

    public void subScore(int amount) {
        mutScore.setValue(mutScore.intValue() - amount);
    }

}
