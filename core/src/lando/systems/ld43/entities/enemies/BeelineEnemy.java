package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.screens.GameScreen;

import java.util.Random;

public class BeelineEnemy extends Enemy {

    private Vector2 velocity = new Vector2(-50, 0);
    private float directionChangeFrequency = .5f;
    private float minDirectionChangeAmount = 40f;
    private float maxDirectionChangeAmount = 60f;
    private static Random randomNumberGen = new Random();
    float directionChangetimer = 0;

    public BeelineEnemy(GameScreen gameScreen, float x, float y) {
        super(gameScreen);
        position.set(x, y);
        this.pointWorth = 5000;
    }

    @Override
    public void update(float dt){
        position.x += velocity.x * dt;
        position.y += velocity.y * dt;

        directionChangetimer += dt;
        if(directionChangetimer >= directionChangeFrequency){
            directionChangetimer -= directionChangeFrequency;
            float directionChangeRange = maxDirectionChangeAmount - minDirectionChangeAmount;
            // calculate a random change amount between the minimum and max
            float directionChangeAmount = randomNumberGen.nextFloat() * directionChangeRange + minDirectionChangeAmount;
            // apply the change amount to the velocity;
            if(randomNumberGen.nextBoolean() || velocity.x > 0){
                if(randomNumberGen.nextBoolean()){
                    directionChangeAmount  = -directionChangeAmount;
                }
                velocity.y += directionChangeAmount;
            } else {
                velocity.x -= directionChangeAmount;
            }
        }
        super.update(dt);
    }

    @Override
    public void render(SpriteBatch batch){
        float dmgPercent = targetPoints.get(0).health / targetPoints.get(0).maxHealth;
        batch.setColor(1f, dmgPercent, dmgPercent, 1f);
        batch.draw(assets.shipEnemyPlane, position.x - width/2, position.y - height/2, width, height);
        batch.setColor(Color.WHITE);
    }
}
