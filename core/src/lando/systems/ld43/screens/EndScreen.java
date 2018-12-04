package lando.systems.ld43.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld43.LudumDare43;
import lando.systems.ld43.entities.Pilot;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Audio;
import lando.systems.ld43.utils.Config;
import sun.awt.ConstrainableGraphics;

public class EndScreen extends BaseScreen{

    private String heading = "Ulti-MEME SACRIFICE";
    private String theme = "Made for Ludum Dare 43:\nTheme: Sacrifices must be made";
    private String thanks = "Thanks for playing our game!";
    private String developers = "Developed by:\nDoug Graham\nBrian Ploeckelman\nJeffrey Hwang\nBrandon Humboldt\nLuke Bain";
    private String artists = "Art by:\nMatt Neumann\nLuke Bain";
    private String emotionalSupport = "Emotional Support:\nAsuka the Shiba\nNyquil\nEatStreet";
    //TODO add song title
    private String music = "Music by:\nTyler Pecora";
    private String libgdx = "Made with <3 and LibGDX";
    private String disclaimer = "Disclaimer!!!\nNo animals were harmed in making of this game.";

    private Vector3 mousePos;
    private Vector2 suitPos;
    private float suitRotation;
    private float rotationDT;
    private Vector2 suitVelocity;
    private TextureRegion playerSuit;
    private TextureRegion texturePointer;

    public EndScreen(LudumDare43 game, Assets assets, Pilot.Type pilot) {
        super(game, assets);
        if (pilot == Pilot.Type.cat) playerSuit = assets.catSuit;
        else playerSuit = assets.dogSuit;
        rotationDT = MathUtils.random(-45, 45);
        suitPos = new Vector2(Config.window_width/2f, Config.window_height/2f);
        suitVelocity = new Vector2(MathUtils.random(1f, 10f), MathUtils.random(1f, 10f)).nor();
        this.mousePos = new Vector3();
        this.texturePointer = assets.pointer;
        audio.playMusic(Audio.Musics.SpaceFanfareWithMaster);
    }

    @Override
    public void update(float dt) {
        suitRotation += rotationDT * dt;
        suitPos.add(suitVelocity.x * dt * 100, suitVelocity.y * dt * 100);
        if (suitPos.x < 0){
            rotationDT = MathUtils.random(-45, 45);
            suitPos.x = 0;
            suitVelocity.x *= -1;
        }
        if (suitPos.x > 700){
            rotationDT = MathUtils.random(-45, 45);
            suitPos.x = 700;
            suitVelocity.x *= -1;
        }
        if (suitPos.y < 100){
            rotationDT = MathUtils.random(-45, 45);
            suitPos.y = 100;
            suitVelocity.y *= -1;
        }
        if (suitPos.y > 500){
            rotationDT = MathUtils.random(-45, 45);
            suitPos.y = 500;
            suitVelocity.y *= -1;
        }

        Gdx.input.setCursorPosition(
                (int) MathUtils.clamp(Gdx.input.getX(), 0, hudCamera.viewportWidth - texturePointer.getRegionWidth()),
                (int) MathUtils.clamp(Gdx.input.getY(), 0, hudCamera.viewportHeight - texturePointer.getRegionHeight()));

        if (Gdx.app.getType() == Application.ApplicationType.Desktop && Gdx.input.justTouched()) {
            game.setScreen(new TitleScreen(game, assets));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0,  1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        {
            batch.setColor(Color.WHITE);
            batch.draw(assets.titleTexture, 0, 0,hudCamera.viewportWidth, hudCamera.viewportHeight);
            batch.setColor(new Color(0f, 0f, 0f, .9f));
            batch.draw(assets.whitePixel, 0, 0, hudCamera.viewportWidth, hudCamera.viewportHeight);
            batch.setColor(Color.WHITE);
            batch.draw(playerSuit, suitPos.x, suitPos.y, 50, 25, 100, 50, 1, 1, suitRotation);

            assets.drawString(batch, heading, 0, hudCamera.viewportHeight - 10, Config.end_screen_text_color, .8f, assets.font, hudCamera.viewportWidth, Align.center);
            assets.drawString(batch, theme, 0, hudCamera.viewportHeight - 80, Config.end_screen_text_color, .35f, assets.font, hudCamera.viewportWidth, Align.center);
            assets.drawString(batch, developers, 0, hudCamera.viewportHeight - 160, Config.end_screen_text_color, .3f, assets.font, hudCamera.viewportWidth/2, Align.center);
            assets.drawString(batch, emotionalSupport, 0, hudCamera.viewportHeight - 320, Config.end_screen_text_color, .3f, assets.font, hudCamera.viewportWidth/2, Align.center);
            assets.drawString(batch, artists, hudCamera.viewportWidth/2, hudCamera.viewportHeight - 160, Config.end_screen_text_color, .3f, assets.font, hudCamera.viewportWidth/2, Align.center);
            assets.drawString(batch, music, hudCamera.viewportWidth/2, hudCamera.viewportHeight - 320, Config.end_screen_text_color, .3f, assets.font, hudCamera.viewportWidth/2, Align.center);
            assets.drawString(batch, libgdx, hudCamera.viewportWidth/2, hudCamera.viewportHeight - 380, Config.end_screen_text_color, .4f, assets.font, hudCamera.viewportWidth/2, Align.center);
            assets.drawString(batch, disclaimer, 0, hudCamera.viewportHeight - 440, Config.end_screen_text_color, .5f, assets.font, hudCamera.viewportWidth, Align.center);
            assets.drawString(batch, thanks, 0, hudCamera.viewportHeight - 550, Config.end_screen_text_color, .4f, assets.font, hudCamera.viewportWidth, Align.center);
            batch.draw(texturePointer, mousePos.x, mousePos.y - texturePointer.getRegionHeight());
        }
        batch.end();
    }
}
