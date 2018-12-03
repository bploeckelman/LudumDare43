package lando.systems.ld43.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld43.entities.PlayerShip;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Config;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Utils;


public class HealthMeter extends UserInterface {

    private static final float margin = 10f;
    private static final float width = 100;
    private static final float height = 25;

    private Color color;
    private GameScreen screen;
    private PlayerShip playerShip;
    private TextureRegion icon;
    private float healthPercentage;

    public HealthMeter(Assets assets, GameScreen gameScreen) {
        super(assets);
        this.screen = gameScreen;
        this.playerShip = gameScreen.player;
        this.icon = assets.iconHeart;
        this.healthPercentage = 1;
        this.bounds.set(margin, screen.hudCamera.viewportHeight - screen.progressUI.bounds.height - height - 3f * margin, width, height);
    }

    public void update(float dt) {
        healthPercentage = playerShip.getCurrentHealthPercent();
    }

    public void render(SpriteBatch batch) {
        batch.setColor(Color.BLACK);
        batch.draw(assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);

        color = Utils.hsvToRgb(((healthPercentage * 120f) - 20) / 365f, 1.0f, 1.0f, color);
        batch.setColor(color);
        batch.draw(assets.whitePixel, bounds.x, bounds.y, bounds.width * healthPercentage, bounds.height);

        batch.setColor(Color.WHITE);
        assets.ninePatch.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);

        // TODO: pulse heart on damage / heal (diff betw. prev and curr frame healthPercent)
        float iconSize = icon.getRegionHeight();
        batch.draw(icon, bounds.x + bounds.width + margin, bounds.y + bounds.height / 2f - iconSize / 2f, iconSize, iconSize);
    }

}
