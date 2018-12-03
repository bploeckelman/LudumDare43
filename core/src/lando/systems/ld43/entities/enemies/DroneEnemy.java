package lando.systems.ld43.entities.enemies;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld43.entities.Bullet;
import lando.systems.ld43.screens.GameScreen;

public class DroneEnemy extends Enemy {

    private float shootDelay;
    private MutableFloat wobble;

    public DroneEnemy(GameScreen gameScreen, float x, float y) {
        super(gameScreen);
        this.position.set(x, y);
        this.shootDelay = 2f;
        this.pointWorth = 1000;

        float wobbleRange = MathUtils.random(1f, 3f);
        this.wobble = new MutableFloat(-wobbleRange);
        Tween.to(wobble, -1, 0.33f)
             .target(wobbleRange)
             .repeatYoyo(-1, 0f)
             .start(gameScreen.tween);
    }

    @Override
    public void update(float dt){
        shootDelay -= dt;
        if (shootDelay <= 0){
            Bullet b = gameScreen.bulletPool.obtain();
            b.init(assets.shotRed, position.x, position.y, -150, 0, false, 10, 10, 10, 1);
            gameScreen.aliveBullets.add(b);
            shootDelay += 2f;
        }
        position.x -= 50 * dt;
        if (position.x < -width) alive = false;
        super.update(dt);
    }

    @Override
    public void render(SpriteBatch batch){
        float dmgPercent = targetPoints.get(0).health / targetPoints.get(0).maxHealth;
        batch.setColor(1f, dmgPercent, dmgPercent, 1f);
        batch.draw(assets.shipEnemyUFO,
                   position.x - width/2,
                   position.y - height/2 + wobble.floatValue(),
                   width, height);
        batch.setColor(Color.WHITE);
    }
}
