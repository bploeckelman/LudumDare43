package lando.systems.ld43.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld43.entities.enemies.TargetPoint;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.ui.DialogUI;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.QuadTreeable;

public class PlayerShip {

    public static float MAX_LASER_TIME = 5f;
    public static float LASER_COOLDOWN = 3f;
    public Vector2 position;
    public float width;
    public float height;
    public GameScreen gameScreen;
    public TargetPoint targetPoint;
    public float damageIndicator;
    public float damageIndicatorLength = .3f;
    public Color damageColor;

    // TODO: make this a map:SatelliteShip.EShipType -> SatelliteShip, one satellite per 'equipment' type
    public Array<SatelliteShip> playerShips;

    private Assets assets;
    private Vector2 tempVec2;
    private Vector3 tempVec3;
    public Pilot pilot;
    public float laserCharge;
    public float laserCooldown;
    public boolean laserOn;
    public float laserLength;
    public float laserWidth;

    public PlayerShip(GameScreen gameScreen, Vector2 position, Pilot.Type pilotType) {
        this.gameScreen = gameScreen;
        this.assets = gameScreen.assets;
        this.position = position;
        this.width = this.height = 40;
        this.pilot = new Pilot(this, assets, pilotType);
        this.tempVec2 = new Vector2();
        this.tempVec3 = new Vector3();
        this.playerShips = new Array<SatelliteShip>();
        this.playerShips.add(new SatelliteShip(gameScreen, this, SatelliteShip.EShipTypes.QUICK_SHOT));
        this.playerShips.add(new SatelliteShip(gameScreen, this, SatelliteShip.EShipTypes.STRAIGHT_SHOT));
        this.playerShips.add(new SatelliteShip(gameScreen, this, SatelliteShip.EShipTypes.SPREAD_SHOT));
        this.targetPoint = new TargetPoint(new Vector2(0,0), 10, 4);
        this.targetPoint.collisionBounds = new Rectangle(position.x, position.y, width, height);
        this.damageColor = new Color();
        this.laserOn = false;
        this.laserLength = 0;
        this.laserWidth = 20;
    }

    public void update(float dt, Vector2 mousePos) {
        if (Gdx.input.isTouched() && laserCooldown <= 0){
            laserOn = true;
            laserCharge += dt;
            if (laserCharge >= MAX_LASER_TIME) {
                laserCooldown = LASER_COOLDOWN;
            }
        } else {
            laserOn = false;
            laserCharge = Math.max(laserCharge - 2f * dt, 0);
        }
        laserCooldown = Math.max(laserCooldown - dt, 0f);
        damageIndicator = Math.max(damageIndicator - dt, 0);
        targetPoint.damageIndicator = Math.max(targetPoint.damageIndicator - dt, 0);
        position.lerp(mousePos, .2f);
        damageColor.set(1f, 1- (damageIndicator/damageIndicatorLength), 1- (damageIndicator/damageIndicatorLength), 1f);
        targetPoint.collisionBounds.set(position.x + targetPoint.positionOffset.x - targetPoint.diameter/2,
                                        position.y + targetPoint.positionOffset.y - targetPoint.diameter/2f,
                                           targetPoint.diameter, targetPoint.diameter);
        for (SatelliteShip satShip: playerShips) {
            satShip.update(dt);
        }
    }

    public void checkBulletCollision(Bullet b){
        // Circle intersection
        if (damageIndicator <= 0 && b.position.dst(position.x + targetPoint.positionOffset.x, position.y + targetPoint.positionOffset.y) < b.collisionRadius/2f + targetPoint.diameter /2f) {
            b.isAlive = false;
            damageIndicator = damageIndicatorLength;
            targetPoint.damageIndicator = damageIndicatorLength;
            targetPoint.health -= b.damage;
            if (targetPoint.health <= 0){
                Gdx.app.log("Player", "Player died");
                // TODO show a "This is LD screen"
                // TODO: remove me, just testing for now
                targetPoint.health = 4;
                gameScreen.clearAllBullets();
                gameScreen.scoreUI.resetScore();
                gameScreen.dialogUI.reset(this.gameScreen, "youdied.json").show();
            }
        }
    }


    public void render(SpriteBatch batch) {
        batch.setColor(damageColor);
        batch.draw(assets.whitePixel, position.x - width/2, position.y - height/2, width, height);
        batch.setColor(1f, targetPoint.damageIndicator/damageIndicatorLength, targetPoint.damageIndicator/damageIndicatorLength, 1f);
        batch.draw(assets.whiteCircle,
                position.x + targetPoint.positionOffset.x - targetPoint.diameter / 2,
                position.y + targetPoint.positionOffset.y - targetPoint.diameter / 2,
                targetPoint.diameter,
                targetPoint.diameter);
        batch.setColor(Color.WHITE);
        for (SatelliteShip satShip: playerShips) {
            satShip.render(batch);
        }
    }

    public void renderLaser(SpriteBatch batch){
        batch.setColor(Color.WHITE);
        if (laserOn){
            batch.draw(assets.laserContinue, position.x + width/2 + 7, position.y - laserWidth/2f, laserLength-7, laserWidth);
            batch.draw(assets.laser, position.x + width/2, position.y - laserWidth/2f, 8, laserWidth);
        }
    }
}
