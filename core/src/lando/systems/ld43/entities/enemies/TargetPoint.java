package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.math.Vector2;

public class TargetPoint {
    public Vector2 positionOffset;
    public float diameter;
    public Enemy owner;
    public float health;
    public float damageIndicator;

    public TargetPoint(Enemy owner, Vector2 positionOffset, float diameter, float health){
        this.owner = owner;
        this.positionOffset = positionOffset;
        this.diameter = diameter;
        this.health = health;
        this.damageIndicator = 0;
    }
}
