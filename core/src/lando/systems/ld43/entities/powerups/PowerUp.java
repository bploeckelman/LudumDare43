package lando.systems.ld43.entities.powerups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.entities.GameObject;
import lando.systems.ld43.entities.PlayerShip;
import lando.systems.ld43.utils.Assets;

public abstract class PowerUp extends GameObject {

    public enum Type { health, cooldown, shield, speed }

    public Type type;

    protected Vector2 velocity;

    private Rectangle boundsPowerUp;
    private Rectangle boundsPlayerShip;

    public PowerUp(Assets assets, float posX, float posY, float velX, float velY) {
        super(assets);
        this.type = null;
        this.position.set(posX, posY);
        this.velocity = new Vector2(velX, velY);
        this.texture = assets.testTexture;

        this.boundsPowerUp = new Rectangle();
        this.boundsPlayerShip = new Rectangle();
    }

    public PowerUp(Assets assets, Vector2 position, Vector2 velocity) {
        this(assets, position.x, position.y, velocity.x, velocity.y);
    }

    public boolean gotCollected(PlayerShip player) {
        boundsPowerUp.set(position.x - width / 2f,
                          position.y - height / 2f,
                          width, height);
        boundsPlayerShip.set(player.position.x - player.width / 2f,
                             player.position.y - player.height / 2f,
                             player.width, player.height);
        return boundsPowerUp.overlaps(boundsPlayerShip);
    }

    public abstract void apply(PlayerShip player);

    @Override
    public void update(float dt) {
        position.add(velocity.x * dt, velocity.y * dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2f, position.y - height / 2f, width, height);
    }

    public static PowerUp createRandom(Assets assets, float posX, float posY, float velX, float velY) {
        PowerUp powerUp;
        float r = MathUtils.random();
        // TODO: tune 'drop percentages'
        if      (r < 0.25f) powerUp = new PowerUpHealth(assets, posX, posY, velX, velY);
        else if (r < 0.50f) powerUp = new PowerUpCooldown(assets, posX, posY, velX, velY);
        else if (r < 0.75f) powerUp = new PowerUpShield(assets, posX, posY, velX, velY);
        else                powerUp = new PowerUpSpeed(assets, posX, posY, velX, velY);
        return powerUp;
    }

    public static PowerUp createRandom(Assets assets, Vector2 position, Vector2 velocity) {
        return PowerUp.createRandom(assets, position.x, position.y, velocity.x, velocity.y);
    }

}
