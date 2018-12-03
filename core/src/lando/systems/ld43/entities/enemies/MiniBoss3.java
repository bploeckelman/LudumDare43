package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.entities.Bullet;
import lando.systems.ld43.screens.GameScreen;


public class MiniBoss3 extends Enemy {
    private float yDir;
    private float shootDelay;
    private int shootCount;
    private boolean shotDir;
    private float rotation;

    public MiniBoss3(GameScreen gameScreen, float xPos, float yPos) {
        super(gameScreen);
        this.position.set(xPos, yPos);
        this.height = 100;
        this.width = 100;
        this.alive = true;
        this.shootDelay = 2f;
        this.shootCount = 0;
        this.yDir = 5;
        this.pointWorth = 10000;
        this.targetPoints.clear();
        this.shotDir = true;
        this.targetPoints.add(new TargetPoint( new Vector2(0,0), 50, 120));
    }

    @Override
    public void update(float dt){
        shootDelay -= dt;
        rotation += 50 * dt;
        if (shootDelay <= 0){
            int shots = 16;
            float deltaDeg = 70f/shots;
            for (int i = 0; i < shots; i++){
                TextureRegion tex = assets.shotCyan;
                switch (i){
                    case 2:
                    case 3:
                    case 12:
                    case 13:
                        tex = assets.shotMagenta;
                        break;
                    case 4:
                    case 5:
                    case 10:
                    case 11:
                        tex = assets.shotYellow;
                        break;
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        tex = assets.shotRed;
                        break;

                }
                for (int j =0; j < 4; j++) {
                    float shotDelta = shootCount * .8f;
                    if (shotDir) shotDelta *= -1;
                    float deg = (90 * j) + (deltaDeg * i) + shotDelta + 10;
                    Bullet b = gameScreen.bulletPool.obtain();
                    b.init(tex, position.x + 30 * MathUtils.cosDeg(deg), position.y + 30 * MathUtils.sinDeg(deg), 200 * MathUtils.cosDeg(deg), 200 * MathUtils.sinDeg(deg), false, 20, 20, 20, 1);
                    gameScreen.aliveBullets.add(b);
                }
            }


            shootCount++;
            shootDelay += .1f;
            if (shootCount > 30){
                shootCount = 0;
                shootDelay = 2f;
                shotDir = !shotDir;
            }

        }

        position.x -= 50 * dt;
        if (position.x < 700) position.x = 700;
        position.y += yDir * dt;
        if (position.y > 320){
            yDir *= -1;
            position.y = 320;
        }
        if (position.y < 280){
            yDir *= -1;
            position.y = 280;
        }
        super.update(dt);
        if (!alive){
            gameScreen.dialogUI.reset(this.gameScreen, "boss3-fatality.json").show();
        }
    }

    @Override
    public void render(SpriteBatch batch){
        batch.setColor(damageColor);
        batch.draw(assets.shipEnemyStar, position.x - width/2, position.y - height/2,
                width/2, height/2,
                width, height,
                1f, 1f, rotation);
        batch.setColor(Color.WHITE);
        super.render(batch);
    }
}
