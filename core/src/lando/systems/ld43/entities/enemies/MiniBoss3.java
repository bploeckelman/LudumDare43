package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.screens.GameScreen;


public class MiniBoss3 extends Enemy {
    private float yDir;
    private float shootDelay;
    private int shootCount;

    public MiniBoss3(GameScreen gameScreen, float xPos, float yPos) {
        super(gameScreen);
        this.position.set(xPos, yPos);
        this.height = 100;
        this.width = 100;
        this.alive = true;
        this.shootDelay = 2f;
        this.shootCount = 0;
        this.yDir = 200;
        this.pointWorth = 10000;
        this.targetPoints.clear();
        this.targetPoints.add(new TargetPoint( new Vector2(0,0), 50, 70));
    }

    @Override
    public void update(float dt){
        position.x -= 50 * dt;
        if (position.x < 700) position.x = 700;

        position.y += yDir * dt;
        if (position.y > 500){
            yDir *= -1;
            position.y = 500;
        }
        if (position.y < 100){
            yDir *= -1;
            position.y = 100;
        }
        super.update(dt);
        if (!alive){
            gameScreen.dialogUI.reset(this.gameScreen, "boss3-fatality.json").show();
        }
    }

    @Override
    public void render(SpriteBatch batch){
        batch.setColor(damageColor);
        batch.draw(assets.shipEnemyStar, position.x - width/2, position.y - height/2, width, height);
        batch.setColor(Color.WHITE);
        super.render(batch);
    }
}
