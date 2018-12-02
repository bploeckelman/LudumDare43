package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld43.screens.GameScreen;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.QuadTreeable;

public class PlayerShip extends QuadTreeable {
    public Vector2 position;
    public float width;
    public float height;
    public GameScreen gameScreen;
    public Array<SatelliteShip> playerShips;

    private Assets assets;
    private Vector2 tempVec2;
    private Vector3 tempVec3;
    public Pilot pilot;

    public PlayerShip(GameScreen gameScreen, Assets assets, Vector2 position, Pilot.Type pilotType) {
        this.gameScreen = gameScreen;
        this.assets = assets;
        this.position = position;
        this.width = this.height = 20;
        this.pilot = new Pilot(this, assets, pilotType);
        this.tempVec2 = new Vector2();
        this.tempVec3 = new Vector3();
        this.playerShips = new Array<SatelliteShip>();
        this.playerShips.add(new SatelliteShip(gameScreen, this, SatelliteShip.EShipTypes.QUICK_SHOT));
        this.playerShips.add(new SatelliteShip(gameScreen, this, SatelliteShip.EShipTypes.STRAIGHT_SHOT));
        this.playerShips.add(new SatelliteShip(gameScreen, this, SatelliteShip.EShipTypes.SPREAD_SHOT));
        this.collisionBounds = new Rectangle(position.x, position.y, width, height);
    }

    public void update(float dt, Vector2 mousePos) {
        position.lerp(mousePos, .1f);
        collisionBounds.set(position.x - width/2, position.y - height/2f, width, height);
        for (SatelliteShip satShip: playerShips) {
            satShip.update(dt);
        }
    }


    public void render(SpriteBatch batch) {
        batch.draw(assets.whitePixel, position.x - width/2, position.y - height/2, width, height);
        for (SatelliteShip satShip: playerShips) {
            satShip.render(batch);
        }
    }
}
