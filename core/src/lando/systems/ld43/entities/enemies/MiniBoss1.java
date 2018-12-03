package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.entities.Bullet;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Assets;


public class MiniBoss1 extends Enemy {
    private Boolean encounterStarted = false;
    private float yDir;
    private float shootDelay;
    private int shootCount;
    public MiniBoss1(GameScreen gameScreen, float xPos, float yPos) {
        super(gameScreen);
        this.position.set(xPos, yPos);
        this.height = 100;
        this.width = 100;
        this.alive = true;
        this.shootDelay = 3f;
        this.shootCount = 0;
        this.yDir = 100;
        this.pointWorth = 10000;
        this.targetPoints.clear();
        this.targetPoints.add(new TargetPoint( new Vector2(0,0), 50, 100));
    }

    @Override
    public void update(float dt){
        shootDelay -= dt;
        if (shootDelay <= 0){
            Bullet b = gameScreen.bulletPool.obtain();
            b.init(assets.redBullet, position.x, position.y, -150, 0, false, 20, 20, 20, 1);
            gameScreen.aliveBullets.add(b);
            shootCount++;
            shootDelay += .2f;
            if (shootCount > 10){
                shootCount =0;
                shootDelay = 1f;
            }

        }

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
            gameScreen.dialogUI.reset(this.gameScreen, "boss1-fatality.json").show();
        }
    }

    @Override
    public void render(SpriteBatch batch){
        batch.setColor(damageColor);
        batch.draw(assets.shipEnemyPlane, position.x - width/2, position.y - height/2, width, height);
        batch.setColor(Color.WHITE);
        super.render(batch);
    }
}
