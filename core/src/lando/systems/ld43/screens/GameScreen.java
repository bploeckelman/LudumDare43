package lando.systems.ld43.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld43.LudumDare43;
import lando.systems.ld43.entities.PlayerShip;
import lando.systems.ld43.ui.Background;
import lando.systems.ld43.utils.Assets;

public class GameScreen extends BaseScreen {

    public Background background;
    public PlayerShip player;


    private Vector2 tempVec2;
    private Vector3 mousePos;

    public GameScreen(LudumDare43 game, Assets assets) {
        super(game, assets);
        tempVec2 = new Vector2();
        mousePos = new Vector3();
        Vector2 startPosition = new Vector2(40, worldCamera.viewportHeight/2);
        player = new PlayerShip(assets, startPosition);
        background = new Background(assets);
    }

    @Override
    public void update(float dt) {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldCamera.unproject(mousePos);

        tempVec2.set(MathUtils.clamp(mousePos.x, player.width, worldCamera.viewportWidth/2),
                     MathUtils.clamp(mousePos.y, player.height, worldCamera.viewportHeight - player.height));
        player.update(dt, tempVec2);

        background.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            background.render(batch);
            player.render(batch);
        }
        batch.end();
    }
}
