package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.entities.Bullet;
import lando.systems.ld43.screens.GameScreen;


public class MiniBoss4 extends Enemy {
    private float yDir;
    private float shootDelay;
    private int shootCount;
    private Vector2 tempVec2;

    public MiniBoss4(GameScreen gameScreen, float xPos, float yPos) {
        super(gameScreen);
        this.position.set(xPos, yPos);
        this.height = 100;
        this.width = 100;
        this.shootDelay = 2f;
        this.shootCount = 0;
        this.yDir = 50;
        this.alive = true;
        this.tempVec2 = new Vector2();
        this.pointWorth = 10000;
        this.targetPoints.clear();
        this.targetPoints.add(new TargetPoint( new Vector2(0,0), 50, 90));
    }

    @Override
    public void update(float dt){
        shootDelay -= dt;
        if (shootDelay <= 0){
            if (shootCount % 2 == 1){
                Bullet b = gameScreen.bulletPool.obtain();
                tempVec2.set(gameScreen.player.position).sub(position).nor();
                b.init(assets.shotRed, position.x, position.y, 300 * tempVec2.x, 300 * tempVec2.y, false, 10, 10, 10, 1);
                gameScreen.aliveBullets.add(b);
            } else {
                int spreadCount = (shootCount) + 5;
                TextureRegion tex = assets.shotYellow;
                switch ((shootCount/2) % 3) {
                    case 1:
                        tex = assets.shotMagenta;
                        break;
                    case 2:
                        tex = assets.shotCyan;
                        break;
                }
                for (int i = 0; i < spreadCount; i++) {
                    float dir = 120 + ((float) i / (spreadCount)) * 120;
                    Bullet b = gameScreen.bulletPool.obtain();
                    b.init(tex, position.x, position.y, 100 * MathUtils.cosDeg(dir), 100 * MathUtils.sinDeg(dir), false, 20, 20, 20, 1);
                    gameScreen.aliveBullets.add(b);
                }
            }


            shootCount++;
            shootDelay += .4f;
            if (shootCount >= 12){
                shootCount = 0;
                shootDelay = 2f;
            }
        }


        position.x -= 50 * dt;
        if (position.x < 700) position.x = 700;
        position.y += yDir * dt;
        if (position.y > 360){
            yDir *= -1;
            position.y = 360;
        }
        if (position.y < 260){
            yDir *= -1;
            position.y = 260;
        }
        super.update(dt);
        if (!alive){
            gameScreen.dialogUI.reset(this.gameScreen, "boss4-fatality.json").show();
        }
    }

    @Override
    public void render(SpriteBatch batch){
        batch.setColor(damageColor);
        batch.draw(assets.shipEnemyCube, position.x - width/2, position.y - height/2, width, height);
        batch.setColor(Color.WHITE);
        super.render(batch);
    }
}
