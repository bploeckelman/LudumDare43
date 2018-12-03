package lando.systems.ld43.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld43.LudumDare43;
import lando.systems.ld43.ui.PilotSelectUI;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Audio;

public class TitleScreen extends BaseScreen {

    private PilotSelectUI pilotSelectUI;

    private Vector3 mousePos;
    private TextureRegion texturePointer;

    public TitleScreen(LudumDare43 game, Assets assets) {
        super(game, assets);

        this.pilotSelectUI = new PilotSelectUI(assets);
        this.mousePos = new Vector3();
        this.texturePointer = assets.pointer;

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCursorPosition((int) (hudCamera.viewportWidth / 2f), (int) (hudCamera.viewportHeight / 2f));
    }

    @Override
    public void update(float dt) {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop
                && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        Gdx.input.setCursorPosition(
                (int) MathUtils.clamp(Gdx.input.getX(), 0, hudCamera.viewportWidth - texturePointer.getRegionWidth()),
                (int) MathUtils.clamp(Gdx.input.getY(), 0, hudCamera.viewportHeight - texturePointer.getRegionHeight()));

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
        hudCamera.unproject(mousePos);

        audio.update(dt);

        pilotSelectUI.update(dt);

        if (Gdx.input.justTouched() && !pilotSelectUI.isVisible()) {
            audio.playMusic(Audio.Musics.SpaceAmbWithMaster);
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

            batch.draw(texturePointer, mousePos.x, mousePos.y - texturePointer.getRegionHeight());
        }
        batch.end();
    }

}
