package lando.systems.ld43.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld43.LudumDare43;
import lando.systems.ld43.entities.Pilot;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Config;

public class EndScreen extends BaseScreen{

    private String heading = "Explosive Sacrifices";
    private String theme = "Made for Ludum Dare 43:\nTheme: Sacrifices must be made";
    private String thanks = "Thanks for playing our game!";
    private String developers = "Developed by:\nDoug Graham\nBrian Ploeckelman\nBrandon Humboldt\nJeffrey Hwang";
    private String artists = "Art by:\nMatt Neumann\nLuke Bain";
    private String emotionalSupport = "Emotional Support:\nAsuka the Shiba";
    //TODO add song title
    private String music = "Music by:\nTyler Pecora";
    private String libgdx = "Made with <3 and LibGDX";

    public EndScreen(LudumDare43 game, Assets assets) {
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
        Gdx.gl.glClearColor(0, 0, 0,  1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        batch.setColor(Color.WHITE);
        batch.draw(assets.titleTexture, 0, 0,hudCamera.viewportWidth, hudCamera.viewportHeight);
        batch.setColor(new Color(0f, 0f, 0f, .9f));
        batch.draw(assets.whitePixel, 0, 0, hudCamera.viewportWidth, hudCamera.viewportHeight);

        assets.drawString(batch, heading, 0, hudCamera.viewportHeight - 10, Config.end_screen_text_color, .8f, assets.font, hudCamera.viewportWidth, Align.center);
        assets.drawString(batch, theme, 0, hudCamera.viewportHeight - 60, Config.end_screen_text_color, .35f, assets.font, hudCamera.viewportWidth, Align.center);
        assets.drawString(batch, developers, 0, hudCamera.viewportHeight - 120, Config.end_screen_text_color, .3f, assets.font, hudCamera.viewportWidth/2, Align.center);
        assets.drawString(batch, emotionalSupport, 0, hudCamera.viewportHeight - 280, Config.end_screen_text_color, .3f, assets.font, hudCamera.viewportWidth/2, Align.center);
        assets.drawString(batch, artists, hudCamera.viewportWidth/2, hudCamera.viewportHeight - 120, Config.end_screen_text_color, .3f, assets.font, hudCamera.viewportWidth/2, Align.center);
        assets.drawString(batch, music, hudCamera.viewportWidth/2, hudCamera.viewportHeight - 220, Config.end_screen_text_color, .3f, assets.font, hudCamera.viewportWidth/2, Align.center);
        assets.drawString(batch, libgdx, hudCamera.viewportWidth/2, hudCamera.viewportHeight - 300, Config.end_screen_text_color, .4f, assets.font, hudCamera.viewportWidth/2, Align.center);
        assets.drawString(batch, thanks, 0, 200, Config.end_screen_text_color, .3f, assets.font, hudCamera.viewportWidth, Align.center);
        
        batch.end();
    }
}
