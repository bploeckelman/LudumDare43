package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld43.utils.Assets;

public class Equipment {

    public enum Type {
        FIRE("Fire", "Burns a little. It hurts people's bodies, and maybe even spaceships."),
        LASER("Laser", "Like a bullet, but flatter and longer. Can't matrix your way out of this."),
        SPREAD("Spread", "Shoots many bullets, in many directions. Covers a lot of area, good value."),
        MISSILE("Missile", "A self-guided explode-y projectile. With crass pictures drawn on it.");

        public String name;
        public String description;
        Type(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    public Type type;
    public Rectangle bounds;
    public Rectangle boundsInitial;
    public Rectangle boundsFinal;
    public boolean available;

    public Equipment(Type equipmentType) {
        this.type = equipmentType;
        this.bounds = new Rectangle();
        this.boundsInitial = new Rectangle();
        this.boundsFinal = new Rectangle();
        this.available = true;
    }

    public TextureRegion getIcon(Assets assets) {
        switch (type) {
            case FIRE: return assets.droneFire;
            case LASER: return assets.droneLaser;
            case SPREAD: return assets.droneSpread;
            case MISSILE: return assets. droneMissile;
            default: return assets.testTexture;
        }
    }

    public TextureRegion getShot(Assets assets) {
        switch (type) {
            case FIRE: return assets.shotFire;
            case LASER: return assets.iconLaser;
            case SPREAD: return assets.shotSpread;
            case MISSILE: return assets. shotMissile;
            default: return assets.testTexture;
        }
    }

}
