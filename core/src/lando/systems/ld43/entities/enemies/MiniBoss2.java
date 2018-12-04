package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.entities.Bullet;
import lando.systems.ld43.screens.GameScreen;


public class MiniBoss2 extends Enemy {
    private float yDir;
    private float shootDelay;
    private int shootCount;
    private Vector2 tempVec2;
    public MiniBoss2(GameScreen gameScreen, float xPos, float yPos) {
        super(gameScreen);
        this.position.set(xPos, yPos);
        this.height = 100;
        this.width = 100;
        this.shootDelay = 2f;
        this.shootCount = 0;
        this.yDir = 20;
        this.alive = true;
        this.tempVec2 = new Vector2();
        this.pointWorth = 10000;
        this.targetPoints.clear();
        this.targetPoints.add(new TargetPoint( new Vector2(0,0), 50, 200));
    }

    @Override
    public void update(float dt){
        shootDelay -= dt;
        if (shootDelay <= 0){
            Bullet b = gameScreen.bulletPool.obtain();
            tempVec2.set(gameScreen.player.position).sub(position).nor();
            b.init(assets.shotMagenta, position.x, position.y, 400 * tempVec2.x, 400 * tempVec2.y, false, 20, 20, 20, 1);
            gameScreen.aliveBullets.add(b);
            shootCount++;
            shootDelay += .1f;
            if (shootCount > 15){
                shootCount = 0;
                shootDelay = 1.5f;
            }

        }

        position.x -= 50 * dt;
        if (position.x < 700) position.x = 700;
        position.y += yDir * dt;
        if (position.y > 350){
            yDir *= -1;
            position.y = 350;
        }
        if (position.y < 250){
            yDir *= -1;
            position.y = 250;
        }

        super.update(dt);
        if (!alive){
            gameScreen.dialogUI.reset(this.gameScreen, "boss2-fatality.json").show();
        }
    }

    @Override
    public void render(SpriteBatch batch){
        batch.setColor(damageColor);
        batch.draw(assets.shipEnemyUFO, position.x - width/2, position.y - height/2, width, height);
        batch.setColor(Color.WHITE);
        super.render(batch);
    }
}
