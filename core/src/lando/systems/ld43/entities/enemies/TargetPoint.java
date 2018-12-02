package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.math.Vector2;

public class TargetPoint {
    public Vector2 positionOffset;
    public float radius;
    public Enemy owner;
    public float health;

    public TargetPoint(Enemy owner, Vector2 positionOffset, float radius, float health){
        this.owner = owner;
        this.positionOffset = positionOffset;
        this.radius = radius;
        this.health = health;
    }
}
