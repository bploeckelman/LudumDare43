package lando.systems.ld43.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld43.entities.enemies.TargetPoint;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Assets;

public class PlayerShip {

    public static float MAX_SHIELD_TIME = 8f;
    public static float MAX_FAST_WEAPONS_TIME = 4f;
    public static float MAX_LASER_TIME = 5f;
    public static float LASER_COOLDOWN = 3f;
    public static float MAX_HEALTH = 4;
    public static float TEXTURE_CHANGE_EPSILON = 15f;

    public Vector2 position;
    public Vector2 targetPosition;
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

    private boolean shieldOn;
    private float shieldTimer;
    private float shieldAnimTime;
    private TextureRegion shieldKeyframe;

    private boolean fastWeaponsOn;
    private float fastWeaponsTimer;

    private TextureRegion textureUp;
    private TextureRegion textureDown;
    private TextureRegion textureNormal;
    private TextureRegion keyframe;

    public PlayerShip(GameScreen gameScreen, Vector2 position, Pilot.Type pilotType) {
        this.gameScreen = gameScreen;
        this.assets = gameScreen.assets;
        this.position = position;
        this.width = this.height = 40;
        this.pilot = new Pilot(this, assets, pilotType);
        this.tempVec2 = new Vector2();
        this.tempVec3 = new Vector3();
        this.playerShips = new Array<SatelliteShip>();
        this.playerShips.add(new SatelliteShip(gameScreen, this, Equipment.Type.LASER));
        this.playerShips.add(new SatelliteShip(gameScreen, this, Equipment.Type.SPREAD));
        this.playerShips.add(new SatelliteShip(gameScreen, this, Equipment.Type.FIRE));
        this.playerShips.add(new SatelliteShip(gameScreen, this, Equipment.Type.MISSILE));
        this.targetPoint = new TargetPoint(new Vector2(0,0), 10, MAX_HEALTH);
        this.targetPoint.collisionBounds = new Rectangle(position.x, position.y, width, height);
        this.damageColor = new Color();
        this.laserOn = false;
        this.laserLength = 0;
        this.laserWidth = 20;
        this.shieldOn = false;
        this.shieldTimer = 0f;
        this.shieldAnimTime = 0f;
        this.shieldKeyframe = assets.animationShield.getKeyFrame(0f);
        this.fastWeaponsOn = false;
        this.fastWeaponsTimer = 0f;
        this.targetPosition = new Vector2();
        this.textureUp = assets.atlas.findRegion("ship-up");
        this.textureDown = assets.atlas.findRegion("ship-down");
        this.textureNormal = assets.atlas.findRegion("ship");
        this.keyframe = textureNormal;
        resetSatelliteLayout();
    }

    public void resetSatelliteLayout(){
        int satellitesLeft = playerShips.size;
        float delta = 180 / (satellitesLeft + 1);
        float dir = 90;
        for (SatelliteShip ship : playerShips){
            dir += delta;
            ship.xPosOffset = 60 * MathUtils.cosDeg(dir);
            ship.yPosOffset = 60 * MathUtils.sinDeg(dir);
        }
    }

    public void update(float dt, boolean allowShooting) {
        if (shieldOn) {
            shieldTimer -= dt;
            if (shieldTimer < 0f) {
                shieldOn = false;
            }

            shieldAnimTime += dt;
            shieldKeyframe = assets.animationShield.getKeyFrame(shieldAnimTime);
        }

        if (fastWeaponsOn) {
            fastWeaponsTimer -= dt;
            if (fastWeaponsTimer < 0f) {
                fastWeaponsTimer = 0f;
                fastWeaponsOn = false;
            }
        }

        if (allowShooting && Gdx.input.isTouched() && laserCooldown <= 0){
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

        if      (targetPosition.y < position.y - TEXTURE_CHANGE_EPSILON) keyframe = textureDown;
        else if (targetPosition.y > position.y + TEXTURE_CHANGE_EPSILON) keyframe = textureUp;
        else                                                             keyframe = textureNormal;

        position.lerp(targetPosition, .2f);
        damageColor.set(1f, 1- (damageIndicator/damageIndicatorLength), 1- (damageIndicator/damageIndicatorLength), 1f);
        targetPoint.collisionBounds.set(position.x + targetPoint.positionOffset.x - targetPoint.diameter/2,
                                        position.y + targetPoint.positionOffset.y - targetPoint.diameter/2f,
                                           targetPoint.diameter, targetPoint.diameter);
        for (SatelliteShip satShip: playerShips) {
            satShip.update(dt, allowShooting, fastWeaponsOn);
        }
    }

    public void setTargetPosition(Vector2 pos){
        targetPosition.set(pos);
    }

    public void resetAllPositions(Vector2 pos){
        targetPosition.set(pos);
        position.set(pos);
        for (SatelliteShip ship : playerShips){
            ship.position.set(pos);
        }
    }

    public void checkBulletCollision(Bullet b){
        if (shieldOn) {
            float distBulletToTarget = b.position.dst(position.x - 5f, position.y);
            float distanceThreshold = (width + 10f / 2f) + b.collisionRadius;
            if (distBulletToTarget < distanceThreshold) {
                b.isAlive = false;
            }
            return;
        }

        // Circle intersection
        if (damageIndicator <= 0 && b.position.dst(position.x + targetPoint.positionOffset.x, position.y + targetPoint.positionOffset.y) < b.collisionRadius/2f + targetPoint.diameter /2f) {
            b.isAlive = false;
            damageIndicator = damageIndicatorLength;
            targetPoint.damageIndicator = damageIndicatorLength;
            targetPoint.health -= b.damage;
            if (targetPoint.health <= 0){
                replenishHealth();
                gameScreen.clearAllBullets();
                gameScreen.scoreUI.resetScore();
                gameScreen.dialogUI.reset(this.gameScreen, "youdied.json").show();
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.setColor(damageColor);
        batch.draw(keyframe, position.x - width/2, position.y - height/2, width, height);

//        batch.setColor(1f, targetPoint.damageIndicator/damageIndicatorLength, targetPoint.damageIndicator/damageIndicatorLength, 1f);
//        batch.draw(assets.whiteCircle,
//                position.x + targetPoint.positionOffset.x - targetPoint.diameter / 2,
//                position.y + targetPoint.positionOffset.y - targetPoint.diameter / 2,
//                targetPoint.diameter,
//                targetPoint.diameter);

        batch.setColor(Color.WHITE);
        if (shieldOn) {
            batch.draw(shieldKeyframe,
                       position.x - (width  + 10f) / 2f - 5f,
                       position.y - (height + 10f) / 2f,
                       width + 20f, height + 20f);
        }
        for (SatelliteShip satShip: playerShips) {
            satShip.render(batch);
        }
    }

    public void renderLaser(SpriteBatch batch){
        batch.setColor(Color.WHITE);
        if (laserOn){
            float length = 20;
            batch.draw(assets.laserContinue, position.x + width/2 + length, position.y - laserWidth/2f, laserLength-length, laserWidth);
            batch.draw(assets.laser, position.x + width/2, position.y - laserWidth/2f, length+1, laserWidth);
        }
    }

    public float getCurrentHealthPercent(){
        return targetPoint.health / MAX_HEALTH;
    }

    public void replenishHealth(){
        targetPoint.health = MAX_HEALTH;
    }

    public void replenishLaser() {
        laserCharge = 0f;
        laserCooldown = 0f;
    }

    public float getLaserChargePercent() {
        return laserCharge / MAX_LASER_TIME;
    }

    public float getLaserCooldownPercent() {
        return laserCooldown / LASER_COOLDOWN;
    }

    public void shieldsToMaximum() {
        shieldOn = true;
        shieldTimer = MAX_SHIELD_TIME;
        shieldAnimTime = 0f;
        shieldKeyframe = assets.animationShield.getKeyFrame(shieldAnimTime);
    }

    public void weaponsToMaximum() {
        fastWeaponsOn = true;
        fastWeaponsTimer = MAX_FAST_WEAPONS_TIME;
    }

}
