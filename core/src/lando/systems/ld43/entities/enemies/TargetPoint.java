package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.utils.QuadTreeable;

public class TargetPoint extends QuadTreeable {
    public Vector2 positionOffset;
    public float diameter;
    public float health;
    public float damageIndicator;

    public TargetPoint(Vector2 positionOffset, float diameter, float health){
        this.positionOffset = positionOffset;
        this.diameter = diameter;
        this.health = health;
        this.damageIndicator = 0;
        this.collisionBounds = new Rectangle();
    }
}
