package lando.systems.ld43.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class StarInfo {
    public Vector2 position;
    public float size;
    public float distance;
    public Color color;

    public StarInfo(float x, float y, float distance){
        this.position = new Vector2(x, y);
        this.size = distance * MathUtils.random(.9f, 1.1f);
        this.distance = distance;
        float grey = MathUtils.random(.3f, .6f);
        this.color = new Color(grey, grey, .3f + MathUtils.random(.5f), 1);
    }
}
