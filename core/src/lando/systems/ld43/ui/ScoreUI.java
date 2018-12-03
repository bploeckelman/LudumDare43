package lando.systems.ld43.ui;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Sine;
import aurelienribon.tweenengine.primitives.MutableFloat;
import aurelienribon.tweenengine.primitives.MutableInteger;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld43.LudumDare43;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Config;

public class ScoreUI extends UserInterface {
    public int score;
    public LudumDare43 game;
    private MutableInteger mutScore;

    public ScoreUI(Assets assets, LudumDare43 game) {
        super(assets);
        this.score = 0;
        this.game = game;
        mutScore = new MutableInteger(0);
    }

    public void update(float dt) {
        score = mutScore.intValue();
    }

    public void render(SpriteBatch batch) {
        UserInterface.drawText(assets, batch, "Score: " + score, 25f, Config.window_height - 25f, Color.WHITE, 0.5f);
    }

    public void resetScore() {
        Tween.to(mutScore, 1, 1f).target(0).ease(Sine.OUT).start(game.tween);
    }

    public void addScore(int amntToAdd) {
        mutScore.setValue(mutScore.intValue() + amntToAdd);
    }
}
