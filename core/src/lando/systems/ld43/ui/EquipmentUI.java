package lando.systems.ld43.ui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld43.accessors.RectangleAccessor;
import lando.systems.ld43.entities.PlayerShip;
import lando.systems.ld43.entities.SatelliteShip;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Assets;

public class EquipmentUI extends UserInterface {

    private static final float margin = 10f;

    private class Equipment {
        public SatelliteShip.EShipTypes type;
        public Rectangle bounds;
        public Rectangle boundsInitial;
        public Rectangle boundsFinal;
        public boolean available;

        public Equipment(SatelliteShip.EShipTypes equipmentType) {
            this.type = equipmentType;
            this.bounds = new Rectangle();
            this.boundsInitial = new Rectangle();
            this.boundsFinal = new Rectangle();
            this.available = true;
        }

        public TextureRegion getIcon(Assets assets) {
            switch (type) {
                case STRAIGHT_SHOT: return assets.redBullet;
                case QUICK_SHOT: return assets.satelliteLaserBullet;
                case SPREAD_SHOT: return assets.spreadBullet;
                default: return assets.testTexture;
            }
        }
    }

    private GameScreen screen;
    private NinePatch border;
    private Rectangle boundsEquipment;
    private Rectangle boundsDescription;
    private Rectangle boundsAcceptButton;
    private Array<Equipment> equipments;
    private ObjectMap<SatelliteShip.EShipTypes, Equipment> typeEquipmentMap;
    private boolean transitionComplete;
    private boolean acceptButtonActive;

    public SatelliteShip.EShipTypes selectedEquipmentType;

    public EquipmentUI(Assets assets) {
        super(assets);

        this.border = assets.ninePatch;
        this.boundsEquipment = new Rectangle();
        this.boundsDescription = new Rectangle();
        this.boundsAcceptButton = new Rectangle();
        this.equipments = new Array<Equipment>();
        this.typeEquipmentMap = new ObjectMap<SatelliteShip.EShipTypes, Equipment>();
        for (SatelliteShip.EShipTypes type : SatelliteShip.EShipTypes.values()) {
            Equipment equipment = new Equipment(type);
            this.equipments.add(equipment);
            this.typeEquipmentMap.put(type, equipment);
        }
        this.transitionComplete = false;
        this.selectedEquipmentType = null;
        this.acceptButtonActive = false;
    }

    public EquipmentUI reset(GameScreen screen) {
        this.screen = screen;
        this.bounds.set(screen.hudCamera.viewportWidth / 2f, screen.hudCamera.viewportHeight / 2f, 0f, 0f);
        this.selectedEquipmentType = null;

        float finalBorderW = screen.hudCamera.viewportWidth - 2f * margin;
        float finalBorderH = screen.hudCamera.viewportHeight - 2f * margin;
        float finalBorderX = margin;
        float finalBorderY = margin;

        float panelWidth = finalBorderW / 2f - 2f * margin;
        float panelHeight = finalBorderH - 2f * margin;
        this.boundsEquipment.set(finalBorderX + margin, finalBorderY + margin, panelWidth, panelHeight);
        this.boundsDescription.set(finalBorderX + finalBorderW - panelWidth - margin, finalBorderY + margin,
                                   finalBorderW / 2f - 2f * margin, finalBorderH - 2f * margin);
        this.boundsAcceptButton.set(boundsDescription.x + margin, boundsDescription.y + margin,
                                    boundsDescription.width - 2f * margin, 50f);

        // Initialize equipment data
        // NOTE: this is a whole lot of work to generic-ify this on the number of equipment types
        // NOTE: frankly its pretty stupid since we'll probably have 6 max...
        int equipmentCount = equipments.size;
        int equipmentRows = Math.round(equipmentCount / 2f);
        int equipmentCols = 2;
        float width = (boundsEquipment.width / equipmentCols) - ((equipmentCols + 1f) / equipmentCols) * margin; // 2 columns
        float height = (boundsEquipment.height / equipmentRows) - ((equipmentRows + 1f) / equipmentRows) * margin; // N rows
        float equipmentSize = Math.min(width, height);
        float x = boundsEquipment.x + margin;
        float y = boundsEquipment.y + boundsEquipment.height - margin - equipmentSize;
        int i = 0;
        for (Equipment equipment : equipments) {
            equipment.available = false;
            equipment.boundsInitial.set(x + equipmentSize / 2f, y + equipmentSize / 2f, 0f, 0f);
            equipment.boundsFinal.set(x, y, equipmentSize, equipmentSize);
            equipment.bounds.set(equipment.boundsInitial);

            i++;
            if (i % 2 == 0) {
                x = boundsEquipment.x + margin;
                y -= equipmentSize + margin;
            } else {
                x += boundsEquipment.width - ((equipmentCols - 1f) * equipmentSize) - (2f * margin);
            }
        }

        // Set equipment availability based on player ship
        PlayerShip ship = screen.player;
        for (SatelliteShip satelliteShip : ship.playerShips) {
            typeEquipmentMap.get(satelliteShip.shipType).available = true;
        }

        return this;
    }

    @Override
    public void show() {
        super.show();

        transitionComplete = false;
        acceptButtonActive = false;

        float finalBorderW = screen.hudCamera.viewportWidth - 2f * margin;
        float finalBorderH = screen.hudCamera.viewportHeight - 2f * margin;
        float finalBorderX = margin;
        float finalBorderY = margin;

        for (Equipment equipment : equipments) {
            equipment.bounds.set(equipment.boundsInitial);
        }

        Timeline.createSequence()
                .push(
                        Tween.to(bounds, RectangleAccessor.XYWH, 0.5f)
                             .target(finalBorderX, finalBorderY, finalBorderW, finalBorderH)
                             .ease(Bounce.OUT)
                )
                .push(
                        // Dumb, but effective?
                        Tween.call(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                for (Equipment equipment : equipments) {
                                    Tween.to(equipment.bounds, RectangleAccessor.XYWH, 0.33f)
                                         .target(equipment.boundsFinal.x, equipment.boundsFinal.y,
                                                 equipment.boundsFinal.width, equipment.boundsFinal.height)
                                         .ease(Quad.OUT)
                                         .start(screen.tween);
                                }
                            }
                        })
                )
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        transitionComplete = true;
                    }
                })
                .start(screen.tween);
    }

    @Override
    public void hide() {
        transitionComplete = false;

        float centerX = screen.hudCamera.viewportWidth / 2f;
        float centerY = screen.hudCamera.viewportHeight / 2f;

        Timeline.createSequence()
                .push(
                        Tween.to(bounds, RectangleAccessor.XYWH, 0.33f)
                             .target(centerX, centerY, 0f, 0f)
                             .ease(Quad.OUT)
                )
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        EquipmentUI.super.hide();
                        // TODO: call public GameScreen method to kickoff super fatality animation with selected equipment type
                    }
                })
                .start(screen.tween);
    }

    @Override
    public void update(float dt) {
        if (screen == null) return;

        // don't allow clicks until show() tweens are fully completed
        if (!transitionComplete) return;

        if (Gdx.input.justTouched()) {
            // Update touch position
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
            screen.hudCamera.unproject(touchPos);

            // Check for accept button click first
            if (acceptButtonActive && boundsAcceptButton.contains(touchPos.x, touchPos.y)) {
                hide();
                return;
            }

            selectedEquipmentType = null;
            acceptButtonActive = false;
            for (Equipment equipment : equipments) {
                if (equipment.available && equipment.bounds.contains(touchPos.x, touchPos.y)) {
                    selectedEquipmentType = equipment.type;
                    acceptButtonActive = true;
                    break;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isVisible()) return;

        // background
        batch.setColor(Color.LIGHT_GRAY);
        batch.draw(assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
        border.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);

        // TODO: draw components (highlight selected, visible active, dim inactive)
        if (!transitionComplete) return;

        // draw region backgrounds
        batch.setColor(Color.GRAY);
        batch.draw(assets.whitePixel, boundsEquipment.x, boundsEquipment.y, boundsEquipment.width, boundsEquipment.height);
        batch.draw(assets.whitePixel, boundsDescription.x, boundsDescription.y, boundsDescription.width, boundsDescription.height);
        batch.setColor(Color.WHITE);
        border.draw(batch, boundsEquipment.x, boundsEquipment.y, boundsEquipment.width, boundsEquipment.height);
        border.draw(batch, boundsDescription.x, boundsDescription.y, boundsDescription.width, boundsDescription.height);

        // draw equipment selection buttons
        for (Equipment equipment : equipments) {
            if (equipment.available) {
                batch.setColor((selectedEquipmentType == equipment.type) ? Color.YELLOW : Color.WHITE);
            } else  {
                batch.setColor(Color.DARK_GRAY);
            }
            batch.draw(assets.whitePixel, equipment.bounds.x, equipment.bounds.y, equipment.bounds.width, equipment.bounds.height);
            batch.setColor(Color.WHITE);
            batch.draw(equipment.getIcon(assets), equipment.bounds.x + 2f * margin, equipment.bounds.y + 2f * margin,
                       equipment.bounds.width - 4f * margin, equipment.bounds.height - 4f * margin);
            border.draw(batch, equipment.bounds.x, equipment.bounds.y, equipment.bounds.width, equipment.bounds.height);
        }

        if (selectedEquipmentType != null) {
            // draw equipment name
            layout.setText(assets.fontPixel32, selectedEquipmentType.name, Color.BLUE, boundsDescription.width, Align.center, false);
            float nameY = boundsDescription.y + boundsDescription.height - layout.height - margin;
            assets.fontPixel32.draw(batch, layout, boundsDescription.x, nameY);

            // draw equipment icon
            Equipment equipment = typeEquipmentMap.get(selectedEquipmentType);
            TextureRegion icon = equipment.getIcon(assets);
            float iconY = nameY - layout.height - equipment.bounds.height - 2f * margin;
            batch.draw(icon, boundsDescription.x + boundsDescription.width / 2f - equipment.bounds.width / 2f,
                       iconY, equipment.bounds.width, equipment.bounds.height);

            // draw equipment description
            layout.setText(assets.fontPixel16, selectedEquipmentType.description, Color.DARK_GRAY, boundsDescription.width - 4f * margin, Align.center, true);
            assets.fontPixel16.draw(batch, layout, boundsDescription.x + 2f * margin, iconY - layout.height - 2f * margin);
        }

        // draw accept button
        if (acceptButtonActive) batch.setColor(Color.GREEN);
        else                    batch.setColor(Color.GRAY);
        batch.draw(assets.whitePixel, boundsAcceptButton.x, boundsAcceptButton.y, boundsAcceptButton.width, boundsAcceptButton.height);
        batch.setColor(Color.WHITE);
        border.draw(batch, boundsAcceptButton.x, boundsAcceptButton.y, boundsAcceptButton.width, boundsAcceptButton.height);
        String buttonText = (acceptButtonActive) ? "Sacrifice Equipment" : "Choose Equipment...";
        layout.setText(assets.fontPixel16, buttonText, Color.DARK_GRAY, boundsAcceptButton.width, Align.center, false);
        assets.fontPixel16.draw(batch, layout, boundsAcceptButton.x, boundsAcceptButton.y + boundsAcceptButton.height / 2f + layout.height / 2f);
    }

}
