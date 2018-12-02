package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld43.entities.Bullet;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.QuadTreeable;

import java.util.ArrayList;

public class Enemy extends QuadTreeable {
    public float width;
    public float height;
    public Vector2 position;
    public boolean alive;

    /**
     * These should be where the enemy is able to be damaged at
     * x and y are offsets from the ship position
     * z is the radius of the circle
     */
    public ArrayList<TargetPoint> targetPoints;

    protected Assets assets;

    public Enemy(Assets assets){
        this.assets = assets;
        this.height = 20;
        this.width = 20;
        this.position = new Vector2();
        this.alive = true;
        this.targetPoints = new ArrayList<TargetPoint>();
        this.targetPoints.add(new TargetPoint(this, new Vector2(0,0), 10, 4));
        this.collisionBounds = new Rectangle(0,0, width, height);
    }

    public void update(float dt){
        // Implement specific update in derived classes
        collisionBounds.set(position.x - width/2, position.y - height/2, width, height);
    }

    public void checkBulletCollision(Bullet b){
        float healthLeft = 0;
        for (TargetPoint target : targetPoints){
            // Circle intersection
            if (b.position.dst2(position.x + target.positionOffset.x, position.y + target.positionOffset.y) < b.collisionRadius * b.collisionRadius + target.radius * target.radius) {
                b.isAlive = false;
                target.health -= b.damage;
            }
            healthLeft += target.health;
        }
        if (healthLeft <= 0) alive = false;
    }

    public void render(SpriteBatch batch){
        // Implement specific render in derived classes
    }

    public void renderTarget(SpriteBatch batch){
        batch.setColor(Color.RED);
        for (TargetPoint targetPoint : targetPoints) {
            batch.draw(assets.whiteCircle,
                    position.x + targetPoint.positionOffset.x - targetPoint.radius / 2,
                    position.y + targetPoint.positionOffset.y - targetPoint.radius / 2,
                    targetPoint.radius,
                    targetPoint.radius);
        }
        batch.setColor(Color.WHITE);
    }
}
