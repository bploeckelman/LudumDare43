package lando.systems.ld43.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld43.LudumDare43;
import lando.systems.ld43.ui.PilotSelectUI;
import lando.systems.ld43.utils.Assets;

public class TitleScreen extends BaseScreen {

    private PilotSelectUI pilotSelectUI;

    public TitleScreen(LudumDare43 game, Assets assets) {
        super(game, assets);

        this.pilotSelectUI = new PilotSelectUI(assets);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void update(float dt) {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop
         && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        // TODO: remove me, just testing for now
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            game.setScreen((new EndScreen(game, assets)));
        }

        pilotSelectUI.update(dt);

        if (Gdx.input.justTouched() && !pilotSelectUI.isVisible()) {
            pilotSelectUI.reset(this).show();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        {
            batch.draw(assets.titleTexture, 0f, 0f, hudCamera.viewportWidth, hudCamera.viewportHeight);

            pilotSelectUI.render(batch);
        }
        batch.end();
    }

}
