package lando.systems.ld43.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld43.entities.PlayerShip;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Assets;

public class ProgressUI extends UserInterface {

    private static final float margin = 10f;
    private static final float height = 42f;

    private GameScreen screen;
    private PlayerShip player;

    private TextureRegion barLeft;
    private TextureRegion barRight;
    private TextureRegion barCenter;
    private TextureRegion miniBossIcon;
    private TextureRegion finalBossIcon;

    private Rectangle boundsInterior;
    private Rectangle boundsFill;
    private Color fillColor;
    private float fillPercent;

    public ProgressUI(Assets assets) {
        super(assets);

        this.screen = null;
        this.player = null;
        this.barLeft = assets.testTexture;
        this.barRight = assets.testTexture;
        this.barCenter = assets.testTexture;
        this.miniBossIcon = assets.testTexture;
        this.finalBossIcon = assets.testTexture;
        this.boundsInterior = new Rectangle();
        this.boundsFill = new Rectangle();
        this.fillColor = new Color();
        this.fillPercent = 0f;
    }

    public ProgressUI reset(GameScreen screen, PlayerShip player) {
        this.screen = screen;
        this.player = player;

        float fillOffsetY = 0f;
        float fillHeight = height;
        switch (player.pilot.getType()) {
            case cat: {
                barLeft = assets.progressCatLeft;
                barRight = assets.progressCatRight;
                barCenter = assets.progressCatCenter;
                fillColor.set(207f / 255f, 202f / 255f, 194f / 255f, 0.9f);
                fillOffsetY = 19f;
                fillHeight = 18f;
            } break;
            case dog: {
                barLeft = assets.progressDogLeft;
                barRight = assets.progressDogRight;
                barCenter = assets.progressDogCenter;
                fillColor.set(229f / 255f, 229f / 255f, 229f / 255f, 0.9f);
                fillOffsetY = 13f;
                fillHeight = 15f;
            } break;
        }

        // TODO: start offscreen, drop on (like kingdoms-fall) on show()
        float y = screen.hudCamera.viewportHeight - margin - height;
        this.bounds.set(margin, y, screen.hudCamera.viewportWidth - 2f * margin, height);

        float interiorW = bounds.width - barLeft.getRegionWidth() - barRight.getRegionWidth();
        this.boundsInterior.set(margin + barLeft.getRegionWidth(), y, interiorW, height);
        this.boundsFill.set(boundsInterior.x, boundsInterior.y + fillOffsetY, 0f, fillHeight);

        return this;
    }

    @Override
    public void update(float dt) {
        if (screen == null) return;
        if (player == null) return;

        // determine progress
        fillPercent = screen.level.timer / screen.level.latestTime;
        // TODO: include overall progress, not just current level progress
//        fillPercent = (screen.levelIndex - 1f) / screen.NUM_LEVELS;
        fillPercent = MathUtils.clamp(fillPercent, 0f, 1f);
        boundsFill.width = fillPercent * boundsInterior.width;
    }

    @Override
    public void render(SpriteBatch batch) {
        // draw left end
        batch.draw(barLeft, bounds.x, bounds.y, barLeft.getRegionWidth(), bounds.height);

        // draw right end
        batch.draw(barRight, boundsInterior.x + boundsInterior.width, bounds.y, barRight.getRegionWidth(), bounds.height);

        // draw center
        batch.draw(barCenter, boundsInterior.x, boundsInterior.y, boundsInterior.width, boundsInterior.height);

        // draw fill
        batch.setColor(fillColor);
        batch.draw(assets.whitePixel, boundsFill.x, boundsFill.y, boundsFill.width, boundsFill.height);
        batch.setColor(Color.WHITE);

        // draw miniboss icons

        // draw final boss icon

        // TODO: draw 'Progress' text?
    }

}
