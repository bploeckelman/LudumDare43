package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.entities.Bullet;
import lando.systems.ld43.screens.GameScreen;


public class FinalBoss extends Enemy {

    private float yDir;
    private float shootdelay;
    private int shootCount;
    private Vector2 tempVec2;
    private float animationAccum;

    public FinalBoss(GameScreen gameScreen, float xPos, float yPos) {
        super(gameScreen);
        this.position.set(xPos, yPos);
        this.height = 400;
        this.width = 200;
        this.yDir = 30;
        this.shootdelay = 1f;
        this.shootCount = 0;
        this.tempVec2 = new Vector2();
        this.alive = true;
        this.pointWorth = 10000;
        this.targetPoints.clear();
        this.targetPoints.add(new TargetPoint( new Vector2(0,175), 50, 50));
        this.targetPoints.add(new TargetPoint( new Vector2(0,50), 50, 100));
        this.targetPoints.add(new TargetPoint( new Vector2(0,-70), 50, 50));
    }


    private Vector2 upperGunPos = new Vector2();
    private Vector2 lowerGunPos = new Vector2();
    @Override
    public void update(float dt){
        animationAccum += dt;
        shootdelay -= dt;
        if (shootdelay <= 0){
            upperGunPos.set(position.x - 96, position.y + 60);
            lowerGunPos.set(position.x - 96, position.y - 66);
            shootCount++;
            int targetsActive = 0;
            for (TargetPoint target : targetPoints){
                if (target.health > 0) targetsActive++;
            }
            if (targetsActive == 3){
                Bullet b = gameScreen.bulletPool.obtain();
                tempVec2.set(gameScreen.player.position).add(0, 10).sub(upperGunPos).nor();
                b.init(assets.shotRed, upperGunPos.x, upperGunPos.y, 400 * tempVec2.x, 400 * tempVec2.y, false, 10, 10, 10, 1);
                gameScreen.aliveBullets.add(b);

                Bullet b2 = gameScreen.bulletPool.obtain();
                tempVec2.set(gameScreen.player.position).add(0, -10).sub(lowerGunPos).nor();
                b2.init(assets.shotRed, lowerGunPos.x, lowerGunPos.y, 400 * tempVec2.x, 400 * tempVec2.y, false, 10, 10, 10, 1);
                gameScreen.aliveBullets.add(b2);
                shootdelay += .3f;
                if (shootCount % 4 == 0) shootdelay += 2f;
            } else if (targetsActive == 2){
                if (shootCount % 3 == 0){
                    Bullet b = gameScreen.bulletPool.obtain();
                    tempVec2.set(gameScreen.player.position).add(0, 10).sub(position).sub(-96, 60).nor();
                    b.init(assets.shotRed, position.x - 96, position.y + 60, 400 * tempVec2.x, 400 * tempVec2.y, false, 10, 10, 10, 1);
                    gameScreen.aliveBullets.add(b);

                    Bullet b2 = gameScreen.bulletPool.obtain();
                    tempVec2.set(gameScreen.player.position).add(0, -10).sub(position).sub(-96, -66).nor();
                    b2.init(assets.shotRed, position.x - 96, position.y - 66, 400 * tempVec2.x, 400 * tempVec2.y, false, 10, 10, 10, 1);
                    gameScreen.aliveBullets.add(b2);
                } else {
                    int spreadCount = (shootCount % 3) + 5;
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
                        Vector2 pos = shootCount%2 == 0 ? upperGunPos : lowerGunPos;
                        float dir = 120 + ((float) i / (spreadCount)) * 120;
                        Bullet b = gameScreen.bulletPool.obtain();
                        b.init(tex, pos.x, pos.y, 100 * MathUtils.cosDeg(dir), 100 * MathUtils.sinDeg(dir), false, 20, 20, 20, 1);
                        gameScreen.aliveBullets.add(b);
                    }
                }
                if (shootCount % 4 == 0) shootdelay += 1f;
                shootdelay += .5f;
            } else {
                if (shootCount % 6 == 0){
                    Bullet b = gameScreen.bulletPool.obtain();
                    tempVec2.set(gameScreen.player.position).add(0, 10).sub(position).sub(-96, 60).nor();
                    b.init(assets.shotRed, position.x - 96, position.y + 60, 400 * tempVec2.x, 400 * tempVec2.y, false, 10, 10, 10, 1);
                    gameScreen.aliveBullets.add(b);

                    Bullet b2 = gameScreen.bulletPool.obtain();
                    tempVec2.set(gameScreen.player.position).add(0, -10).sub(position).sub(-96, -66).nor();
                    b2.init(assets.shotRed, position.x - 96, position.y - 66, 400 * tempVec2.x, 400 * tempVec2.y, false, 10, 10, 10, 1);
                    gameScreen.aliveBullets.add(b2);
                } else {
                    int spreadCount = (shootCount % 3) + 5;
                    TextureRegion tex = assets.shotYellow;
                    switch ((shootCount/2) % 3) {
                        case 1:
                            tex = assets.shotMagenta;
                            break;
                        case 2:
                            tex = assets.shotCyan;
                            break;
                    }
                    for (int g = 0; g < 2; g++) {
                        Vector2 pos = g %2 == 0 ? upperGunPos : lowerGunPos;
                        for (int i = 0; i < spreadCount; i++) {
                            float dir = 120 + ((float) i / (spreadCount)) * 120;
                            Bullet b = gameScreen.bulletPool.obtain();
                            b.init(tex, pos.x, pos.y, 100 * MathUtils.cosDeg(dir), 100 * MathUtils.sinDeg(dir), false, 20, 20, 20, 1);
                            gameScreen.aliveBullets.add(b);
                        }
                    }
                }
                if (shootCount % 6 == 0) shootdelay += 2f;
                shootdelay += .5f;
            }

        }

        position.x -= 50 * dt;
        if (position.x < 700) position.x = 700;
        position.y += yDir * dt;
        if (position.y > 330){
            yDir *= -1;
            position.y = 330;
        }
        if (position.y < 270){
            yDir *= -1;
            position.y = 270;
        }
        super.update(dt);
        if (!alive){
            gameScreen.dialogUI.reset(this.gameScreen, "bossfinal-fatality.json").show();
        }
    }

    @Override
    public void render(SpriteBatch batch){
        batch.setColor(damageColor);
        batch.draw(assets.animationFinalBoss.getKeyFrame(animationAccum), position.x - width/2, position.y - height/2, width, height);
        batch.setColor(Color.WHITE);
        super.render(batch);
    }
}
