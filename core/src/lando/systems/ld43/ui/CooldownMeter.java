package lando.systems.ld43.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld43.entities.PlayerShip;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Config;
import lando.systems.ld43.utils.Utils;

public class CooldownMeter extends UserInterface {
    float width = 100;
    float height = 20;
    public boolean isCharging;
    private Color color;
    private PlayerShip playerShip;
    private float laserChargePercentage;
    public CooldownMeter(Assets assets, GameScreen gameScreen) {
        super(assets);
        this.playerShip = gameScreen.player;
        laserChargePercentage = 0;
        isCharging = false;
    }
    public void render(SpriteBatch batch) {
        batch.setColor(Color.BLACK);
        batch.draw(assets.whitePixel, 25f, Config.window_height - 150f, width, height);
        color = Config.laser_color;
        batch.setColor(color);
        batch.draw(assets.whitePixel, 25f, Config.window_height - 150f, width * laserChargePercentage, height);
        batch.setColor(Color.WHITE);
        assets.ninePatch.draw(batch, 25f, Config.window_height - 150f, width, height);
        UserInterface.drawText(assets, batch, "Laser Charge", 30f, Config.window_height - 120f, Color.WHITE, 0.25f);
        if (isCharging) {
            UserInterface.drawText(assets, batch, "Recharging", 35f, Config.window_height - 135f, Color.WHITE, 0.25f);
        }

    }

    public void update(float dt) {
        laserChargePercentage = 1 - playerShip.getLaserChargePercent();
        if (playerShip.laserCooldown != 0) {
            isCharging = true;
        } else {
            isCharging = false;
        }
    }



}
