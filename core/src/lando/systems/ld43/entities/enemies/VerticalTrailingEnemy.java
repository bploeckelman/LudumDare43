package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.entities.Bullet;
import lando.systems.ld43.screens.GameScreen;

public class VerticalTrailingEnemy extends Enemy {

    public Vector2 playerPosition;
    private float direction;
    private float rotation;
    private float rotationRate;
    private float shootDelay;


    public VerticalTrailingEnemy(GameScreen gameScreen, float x, float y) {
        super(gameScreen);
        this.position.set(x, y);
        this.playerPosition = gameScreen.player.position;
        this.pointWorth = 3000;
        this.direction = 1;
        this.rotation = 0f;
        this.shootDelay = 2f;
        this.rotationRate = MathUtils.random(50f, 200f);
    }

    @Override
    public void update(float dt){
        shootDelay -= dt;
        if (shootDelay <= 0){
            int bulletAmmount = 10;
            float dirDelta = 360f/bulletAmmount;
            for (int i = 0; i < bulletAmmount; i++) {
                Bullet b = gameScreen.bulletPool.obtain();
                b.init(assets.shotYellow, position.x, position.y, 60 * MathUtils.cosDeg(dirDelta * i), 60 * MathUtils.sinDeg(dirDelta * i), false, 10, 10, 10, 1);
                gameScreen.aliveBullets.add(b);
            }
            shootDelay += 2f;
        }

        rotation += dt * rotationRate;
        position.x -= 50 * dt;
        if (position.y < playerPosition.y) {
            direction = 1;
        }
        else if (playerPosition.y < position.y) {
            direction = -1;
        }
        if (Math.abs(playerPosition.y - position.y) > 25) {
            position.y += direction * 25 * dt;
        }
        if (position.x < -width) alive = false;
        super.update(dt);
    }

    @Override
    public void render(SpriteBatch batch){
        float dmgPercent = targetPoints.get(0).health / targetPoints.get(0).maxHealth;
        batch.setColor(1f, dmgPercent, dmgPercent, 1f);
        batch.draw(assets.shipEnemyStar,
                   position.x - width / 2f,
                   position.y - height / 2f,
                   width / 2f, height / 2f,
                   width, height, 1f, 1f, rotation);
        batch.setColor(Color.WHITE);
    }
}
