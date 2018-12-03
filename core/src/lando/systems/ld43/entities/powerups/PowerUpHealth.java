package lando.systems.ld43.entities.powerups;

import com.badlogic.gdx.math.Vector2;
import lando.systems.ld43.entities.PlayerShip;
import lando.systems.ld43.utils.Assets;

public class PowerUpHealth extends PowerUp {

    public PowerUpHealth(Assets assets, float posX, float posY, float velX, float velY) {
        super(assets, posX, posY, velX, velY);
        this.type = Type.health;
        this.width = 40f;
        this.height = 40f;
        this.texture = assets.powerUpHealth;
    }

    public PowerUpHealth(Assets assets, Vector2 position, Vector2 velocity) {
        this(assets, position.x, position.y, velocity.x, velocity.y);
    }

    public void apply(PlayerShip player) {
        // TODO: where is player health?
//        player.health = 100f;
    }

}
