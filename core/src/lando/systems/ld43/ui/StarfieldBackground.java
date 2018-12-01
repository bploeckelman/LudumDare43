package lando.systems.ld43.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld43.utils.Assets;
import lando.systems.ld43.utils.Config;

import java.util.ArrayList;

public class StarfieldBackground implements Background{

    private Assets assets;
    private ArrayList<Vector3> stars;

    public StarfieldBackground(Assets assets){
        this.assets = assets;
        this.stars = new ArrayList<Vector3>();
        for (int i = 0; i < 300; i++){
            stars.add(new Vector3(MathUtils.random(Config.window_width),
                                  MathUtils.random(Config.window_height),
                                  MathUtils.random(1f, 3f)));
        }
    }

    @Override
    public void update (float dt) {
        for (Vector3 star : stars){
            star.x -= (star.z * speed.floatValue() * dt);
            if (star.x < 10){
                star.x = Config.window_width + 10;
                star.y = MathUtils.random(Config.window_height);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(.5f, .5f, .5f, 1f);
        for (Vector3 star : stars){
            batch.draw(assets.whitePixel, star.x, star.y, star.z, star.z);
        }
        batch.setColor(Color.WHITE);
    }
}
