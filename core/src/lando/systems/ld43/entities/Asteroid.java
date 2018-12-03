package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.screens.GameScreen;

public class Asteroid {
    public static float MAXSPEED = 80;
    public Vector2 position;
    public Vector2 velocity;
    public float size;
    private GameScreen gameScreen;
    public boolean alive;

    public Asteroid(GameScreen screen){
        this.gameScreen = screen;
        int side = MathUtils.random(2);
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.size = MathUtils.random(10f, 50f);
        switch (side){
            case 0: // TOP
                this.position.set(MathUtils.random(size + 10, gameScreen.worldCamera.viewportWidth - size - 10), gameScreen.worldCamera.viewportHeight + size);
                this.velocity.set(MathUtils.random(-MAXSPEED, MAXSPEED), MathUtils.random(-10f, -MAXSPEED));
                break;
            case 1: // BOTTOM
                this.position.set(MathUtils.random(size + 10, gameScreen.worldCamera.viewportWidth - size - 10), - size);
                this.velocity.set(MathUtils.random(-MAXSPEED, MAXSPEED), MathUtils.random(10f, MAXSPEED));
                break;
//            case 2: // LEFT
//                this.position.set(-size, MathUtils.random(size + 10, gameScreen.worldCamera.viewportHeight - size - 10));
//                this.velocity.set(MathUtils.random(10f, MAXSPEED), MathUtils.random(-MAXSPEED, MAXSPEED));
//                break;
            case 2: // RIGHT
                this.position.set(gameScreen.worldCamera.viewportWidth  + size, MathUtils.random(size + 10, gameScreen.worldCamera.viewportHeight - size - 10));
                this.velocity.set(MathUtils.random(-10f, -MAXSPEED), MathUtils.random(-MAXSPEED, MAXSPEED));
                break;
        }

        this.alive = true;
    }

    public void update(float dt) {
        position.add(velocity.x * dt, velocity.y * dt);
        if (velocity.x < 0 && position.x < -size) alive = false;
        if (velocity.x > 0 && position.x > gameScreen.worldCamera.viewportWidth + size) alive = false;
        if (velocity.y < 0 && position.y < -size) alive = false;
        if (velocity.y > 0 && position.y > gameScreen.worldCamera.viewportHeight + size) alive = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(gameScreen.assets.asteroid, position.x - size/2, position.y - size/2, size, size);
    }
}
