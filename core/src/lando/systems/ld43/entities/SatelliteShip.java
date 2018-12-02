package lando.systems.ld43.entities;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.screens.GameScreen;

public class SatelliteShip {

    // TODO: change to 'EquipmentType' and move to own file
    public enum EShipTypes {
        STRAIGHT_SHOT("Single Shot", "Just a normal bullet. It hurts people's bodies, and maybe spaceships."),
        QUICK_SHOT("Quick Shot", "Just a normal bullet, but faster. Can't matrix your way out of this."),
        SPREAD_SHOT("Spread Shot", "Shoots many bullets, in many directions. All of them super deadly and stuff.")
        // TODO: remove me
        , OTHER_THING0("Thing 0", "It's a temporary thing")
        , OTHER_THING1("Thing 1", "It's another temporary thing")
        , OTHER_THING2("Thing 2", "It's yet another temporary thing");

        public String name;
        public String description;
        EShipTypes(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    public GameScreen gameScreen;
    public PlayerShip player;
    public float xPosOffset;
    public float yPosOffset;
    public MutableFloat recoil;
    public TextureRegion texture;
    public EShipTypes shipType;
    public Vector2 position;
    public float width;
    public float height;
    public float shootDelay;
    public float driftAccum;
    public Vector2 targetPosition;

    public SatelliteShip(GameScreen gameScreen, PlayerShip player, EShipTypes shipType) {
        this.gameScreen = gameScreen;
        this.player = player;
        this.shipType = shipType;
        switch(this.shipType){
            case SPREAD_SHOT:
                this.texture = gameScreen.assets.satelliteSpreadShip;
                this.xPosOffset = -50;
                this.yPosOffset = 0;
                break;
            case STRAIGHT_SHOT:
                this.texture = gameScreen.assets.satelliteShip;
                this.xPosOffset = 0;
                this.yPosOffset = 50;
                break;
            case QUICK_SHOT:
                this.texture = gameScreen.assets.satelliteShip;
                this.xPosOffset = 0;
                this.yPosOffset = -50;
                break;
        }
        this.shootDelay = 1f;
        this.recoil = new MutableFloat(0);
        this.width = this.height = 35;
        this.position = new Vector2(player.position.x + this.xPosOffset, player.position.y + this.yPosOffset);
        this.driftAccum = MathUtils.random(3f);
        this.targetPosition = new Vector2(this.position);
    }

    public void update(float dt) {
        driftAccum += MathUtils.random(dt);
        targetPosition.set(player.position.x + xPosOffset + (MathUtils.cos(driftAccum * 2.5f)*5), player.position.y + yPosOffset + (MathUtils.sin(driftAccum * 2)*10));
        float dist = position.dst(player.position);
        dist = MathUtils.clamp(dist/50f, 0f, 1f);
        dist = MathUtils.clamp(1f - dist, 0.2f, .3f);
        position.x += recoil.floatValue();
        position.lerp(targetPosition, dist);
        position.x -= recoil.floatValue();
//        position.x = player.position.x + xPosOffset - recoil.floatValue();
//        position.y = player.position.y + yPosOffset;
        shootDelay -= dt;
        if (shootDelay <= 0){
            switch (shipType){
                case SPREAD_SHOT:
                    int spreadshots = 10;
                    for (int i = 0; i < spreadshots; i++){
                        float dir = -90 + ((float)i / (spreadshots-1)) * 180;
                        Bullet bullet = gameScreen.bulletPool.obtain();
                        bullet.init(gameScreen.assets.spreadBullet, position.x, position.y, 600 * MathUtils.cosDeg(dir), 600 * MathUtils.sinDeg(dir), true, 10f, 10f, 10f, 1f);
                        gameScreen.aliveBullets.add(bullet);
                    }
                    recoil.setValue(6);
                    Tween.to(recoil, 0, .4f)
                            .target(0)
                            .start(gameScreen.game.tween);
                    shootDelay = .5f;
                    break;
                case STRAIGHT_SHOT:
                    Bullet bullet = gameScreen.bulletPool.obtain();
                    bullet.init(gameScreen.assets.redBullet, position.x, position.y, 500f, 0f, true, 30f, 30f, 30f, 2f);
                    gameScreen.aliveBullets.add(bullet);
                    shootDelay = 1f;
                    recoil.setValue(15);
                    Tween.to(recoil, 0, .8f)
                            .target(0)
                            .start(gameScreen.game.tween);
                    break;
                case QUICK_SHOT:
                    bullet = gameScreen.bulletPool.obtain();
                    bullet.init(gameScreen.assets.satelliteLaserBullet, position.x, position.y, 900f, 0f, true, 50f, 15f, 15f, .5f);
                    gameScreen.aliveBullets.add(bullet);
                    recoil.setValue(4);
                    Tween.to(recoil, 0, .18f)
                            .target(0)
                            .start(gameScreen.game.tween);
                    shootDelay = .2f;
                    break;
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width, height);
    }

}
