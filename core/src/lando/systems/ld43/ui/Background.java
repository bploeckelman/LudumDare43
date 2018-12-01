package lando.systems.ld43.ui;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Background {
    MutableFloat speed = new MutableFloat(0);
    void update(float dt);
    void render(SpriteBatch batch);
}
