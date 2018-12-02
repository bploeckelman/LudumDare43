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
    private ArrayList<StarInfo> stars;

    public StarfieldBackground(Assets assets){
        this.assets = assets;
        this.stars = new ArrayList<StarInfo>();
        for (int i = 0; i < 300; i++){
            stars.add(new StarInfo(MathUtils.random(Config.window_width),
                                   MathUtils.random(Config.window_height),
                                   MathUtils.random(1f, 3f)));
        }
    }

    @Override
    public void update (float dt) {
        for (StarInfo star : stars){
            star.position.x -= (star.distance * speed.floatValue() * dt);
            if (star.position.x < 10){
                star.position.x = Config.window_width + 10;
                star.position.y = MathUtils.random(Config.window_height);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        for (StarInfo star : stars){
            batch.setColor(star.color);
            batch.draw(assets.whitePixel, star.position.x, star.position.y, star.size, star.size);
        }
        batch.setColor(Color.WHITE);
    }
}
