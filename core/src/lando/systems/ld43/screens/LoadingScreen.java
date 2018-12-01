package lando.systems.ld43.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld43.LudumDare43;
import lando.systems.ld43.utils.Assets;

/**
 * Even though this will be a web game and assets are loaded before the game
 * using this to have the user click the screen once before starting any sounds for GWT BS
 */
public class LoadingScreen extends BaseScreen {
    public LoadingScreen(LudumDare43 game, Assets assets) {
        super(game, assets);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.justTouched()) {
            game.setScreen(new TitleScreen(game, assets));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // TODO add out text font with click to start
    }
}
