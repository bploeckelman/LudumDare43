package lando.systems.ld43.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld43.entities.PlayerShip;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Config;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Utils;


public class HealthMeter extends UserInterface {
    float width = 100;
    float height = 20;
    public int maxHealth = 100;
    public boolean isGoingUp = true;
    private Color color;
    private PlayerShip playerShip;
    private float healthPercentage;
    public HealthMeter(Assets assets, GameScreen gameScreen) {
        super(assets);
        this.playerShip = gameScreen.player;
        healthPercentage = 1;
    }
    public void render(SpriteBatch batch) {
        batch.setColor(Color.BLACK);
        batch.draw(assets.whitePixel, 25f, Config.window_height - 100f, width, height);
        color = Utils.hsvToRgb(((healthPercentage * 120f) - 20) / 365f, 1.0f, 1.0f, color);
        batch.setColor(color);
        batch.draw(assets.whitePixel, 25f, Config.window_height - 100f, width * healthPercentage, height);
        batch.setColor(Color.WHITE);
        assets.ninePatch.draw(batch, 25f, Config.window_height - 100f, width, height);
        UserInterface.drawText(assets, batch, "Health", 50f, Config.window_height - 70f, Color.WHITE, 0.25f);

    }

    public void update(float dt) {
        healthPercentage = playerShip.getCurrentHealthPercent();
    }


}
