package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.utils.Assets;

import java.util.ArrayList;

public class MiniBoss1 extends Enemy {
    public MiniBoss1(Assets assets, float xPos, float yPos) {
        super(assets);
        this.position.set(xPos, yPos);
        this.height = 200;
        this.width = 100;
        this.alive = true;
        this.targetPoints.clear();
        this.targetPoints.add(new TargetPoint(this, new Vector2(0,0), 30, 40));
        this.targetPoints.add(new TargetPoint(this, new Vector2(0,-50), 30, 40));
        this.targetPoints.add(new TargetPoint(this, new Vector2(0,50), 30, 40));
        this.collisionBounds = new Rectangle(0,0, width, height);
    }

    @Override
    public void update(float dt){
        position.x -= 50 * dt;
        if (position.x < 700) position.x = 700;

        super.update(dt);
    }

    @Override
    public void render(SpriteBatch batch){
        batch.draw(assets.whitePixel, position.x - width/2, position.y - height/2, width, height);
    }
}
