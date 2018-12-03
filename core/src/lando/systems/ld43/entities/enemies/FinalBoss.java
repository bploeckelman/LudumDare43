package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.screens.GameScreen;


public class FinalBoss extends Enemy {
    private Boolean encounterStarted = false;
    public FinalBoss(GameScreen gameScreen, float xPos, float yPos) {
        super(gameScreen);
        this.position.set(xPos, yPos);
        this.height = 200;
        this.width = 100;
        this.alive = true;
        this.pointWorth = 10000;
        this.targetPoints.clear();
        this.targetPoints.add(new TargetPoint( new Vector2(0,-175), 50, 100));
        this.targetPoints.add(new TargetPoint( new Vector2(-7,-50), 50, 200));
        this.targetPoints.add(new TargetPoint( new Vector2(0,70), 50, 100));
    }

    @Override
    public void update(float dt){
        position.x -= 50 * dt;
        if (position.x < 700) position.x = 700;

        super.update(dt);
        if (!alive){
            gameScreen.dialogUI.reset(this.gameScreen, "bossfinal-fatality.json").show();
        }
    }

    @Override
    public void render(SpriteBatch batch){
        batch.setColor(damageColor);
        batch.draw(assets.finalBoss, position.x - width/2, position.y - height/2, width, height);
        batch.setColor(Color.WHITE);
        super.render(batch);
    }
}
