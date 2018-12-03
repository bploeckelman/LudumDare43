package lando.systems.ld43.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld43.entities.PlayerShip;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Config;

public class CooldownMeter extends UserInterface {

    private static final float margin = 10f;
    private static final float width = 100;
    private static final float height = 25;

    private GameScreen screen;
    private PlayerShip playerShip;
    private TextureRegion icon;
    private boolean isCharging;
    private float laserChargePercentage;
    private float laserTimer;
    private boolean isLaserOn;

    public CooldownMeter(Assets assets, GameScreen gameScreen) {
        super(assets);
        this.screen = gameScreen;
        this.playerShip = screen.player;
        this.icon = assets.iconBeam;
        this.isCharging = false;
        this.laserChargePercentage = 0;
        this.laserTimer = 0;
        this.bounds.set(margin, screen.hudCamera.viewportHeight - screen.progressUI.bounds.height - screen.healthMeter.bounds.height - 7f * margin, width, height);
    }

    public void update(float dt) {
        laserChargePercentage = 1 - playerShip.getLaserChargePercent();
        isCharging = (playerShip.laserCooldown != 0);
        isLaserOn = playerShip.laserOn;
        laserTimer += dt;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(Color.BLACK);
        batch.draw(assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);

        batch.setColor(Config.laser_color);
        batch.draw(assets.whitePixel, bounds.x, bounds.y, bounds.width * laserChargePercentage, bounds.height);

        batch.setColor(Color.WHITE);
        assets.ninePatch.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        float iconSize = icon.getRegionHeight();

        if (isCharging) {
            layout.setText(assets.fontPixel8, "Recharge...");
            assets.fontPixel8.draw(batch, layout, bounds.x + 5f, bounds.y + bounds.height / 2f + layout.height / 2f);
            if (laserTimer % .5f > .25f) {
                batch.draw(icon, bounds.x + bounds.width + margin, bounds.y + bounds.height / 2f - iconSize / 2f, iconSize, iconSize);
            }
        } else {
            if (isLaserOn) {
                float pulsePercentage = (laserTimer % 0.25f) +1f;
                iconSize = iconSize * pulsePercentage;
            }
            batch.draw(icon, bounds.x + bounds.width + margin, bounds.y + bounds.height / 2f - iconSize / 2f, iconSize, iconSize);
        }
    }

}
