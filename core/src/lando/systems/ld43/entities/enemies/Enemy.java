package lando.systems.ld43.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.entities.Bullet;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Audio;
import lando.systems.ld43.utils.QuadTreeable;

import java.util.ArrayList;

public class Enemy {
    public float width;
    public float height;
    public Vector2 position;
    public boolean alive;
    public int pointWorth;
    public float damageIndicator;
    public float damageIndicatorLength = .3f;
    public Color damageColor;
    public boolean destroyed;
    public GameScreen gameScreen;

    /**
     * These should be where the enemy is able to be damaged at
     * x and y are offsets from the ship position
     * z is the diameter of the circle
     */
    public ArrayList<TargetPoint> targetPoints;

    protected Assets assets;

    public Enemy(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        this.assets = gameScreen.assets;
        this.height = 40;
        this.width = 40;
        this.position = new Vector2();
        this.alive = true;
        this.damageIndicator = 0;
        this.targetPoints = new ArrayList<TargetPoint>();
        this.targetPoints.add(new TargetPoint(new Vector2(0,0), 10, 4));
        this.damageColor = new Color();
        this.destroyed = false;
        this.pointWorth = 1000;
    }

    public void update(float dt){
        float healthLeft = 0;
        // Implement specific update in derived classes
        damageColor.set(1f, 1 - (damageIndicator/damageIndicatorLength), 1 - (damageIndicator/damageIndicatorLength), 1f);
        damageIndicator = Math.max(damageIndicator - dt, 0);
        for (int i = targetPoints.size() -1; i >= 0; i--){
            TargetPoint target = targetPoints.get(i);
            if (target.health <= 0){
                target.health = 0;
                gameScreen.particleSystem.addSmoke(position.x + target.positionOffset.x, position.y + target.positionOffset.y);
            }
            healthLeft += target.health;
            target.collisionBounds.set(position.x + target.positionOffset.x - target.diameter/2, position.y + target.positionOffset.y - target.diameter/2, target.diameter, target.diameter);
            target.damageIndicator = Math.max(target.damageIndicator - dt, 0);
        }
        if (healthLeft <= 0) alive = false;
    }

    public void updateWhileDisabled(float dt){
        for (int i = targetPoints.size() -1; i >= 0; i--){
            TargetPoint target = targetPoints.get(i);
            gameScreen.particleSystem.addSmoke(position.x + target.positionOffset.x, position.y + target.positionOffset.y);

        }
    }

    public void checkBulletCollision(Bullet b, TargetPoint target){
        // Circle intersection
        if (b.position.dst(position.x + target.positionOffset.x, position.y + target.positionOffset.y) < b.collisionRadius/2f + target.diameter /2f) {
            b.isAlive = false;
            damageIndicator = damageIndicatorLength;
            target.damageIndicator = damageIndicatorLength;
            target.health -= b.damage;
            if (target.health <= 0) {
                gameScreen.audio.playSound(Audio.Sounds.explosion_tiny);
                gameScreen.particleSystem.addExplosion(position.x + target.positionOffset.x, position.y + target.positionOffset.y, target.diameter * 5f, target.diameter* 5f);
            }
        }
    }

    public void render(SpriteBatch batch){
        // Implement specific render in derived classes
        renderTarget(batch);
    }

    public void renderTarget(SpriteBatch batch){
        for (TargetPoint targetPoint : targetPoints) {
            if (targetPoint.health <= 0) continue;
            batch.setColor(targetPoint.damageIndicator/damageIndicatorLength, 0, 0, targetPoint.damageIndicator/damageIndicatorLength * .3f);
            batch.draw(assets.whiteCircle,
                    position.x + targetPoint.positionOffset.x - targetPoint.diameter / 2,
                    position.y + targetPoint.positionOffset.y - targetPoint.diameter / 2,
                    targetPoint.diameter,
                    targetPoint.diameter);
        }
        batch.setColor(Color.WHITE);
    }

    public void explodingAnimations(){
        gameScreen.particleSystem.addExplosion(
                    MathUtils.random(position.x - width/2f, position.x + width/2f),
                    MathUtils.random(position.y - height/2f, position.y + height/2f),
                    60, 60);
    }
}
