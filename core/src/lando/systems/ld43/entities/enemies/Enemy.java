package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
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
    public ArrayList<Vector3> targetOffsets;

    protected Assets assets;

    public Enemy(Assets assets){
        this.assets = assets;
        this.height = 20;
        this.width = 20;
        this.position = new Vector2();
        this.alive = true;
        this.targetOffsets = new ArrayList<Vector3>();
        this.targetOffsets.add(new Vector3(0, 0, 10));
        this.collisionBounds = new Rectangle(0,0, width, height);
    }

    public void update(float dt){
        // Implement specific update in derived classes
        collisionBounds.set(position.x - width/2, position.y - height/2, width, height);
    }


    public void render(SpriteBatch batch){
        // Implement specific render in derived classes
    }

    public void renderTarget(SpriteBatch batch){
        batch.setColor(Color.RED);
        for (Vector3 targetOffset : targetOffsets) {
            batch.draw(assets.whiteCircle,
                    position.x + targetOffset.x - targetOffset.z / 2,
                    position.y + targetOffset.y - targetOffset.z / 2,
                    targetOffset.z,
                    targetOffset.z);
        }
        batch.setColor(Color.WHITE);
    }
}
