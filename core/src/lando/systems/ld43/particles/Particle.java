package lando.systems.ld43.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Particle implements Pool.Poolable {

    Animation<TextureRegion> animation;
    TextureRegion region;
    Vector2 pos;
    Vector2 vel;
    Vector2 acc;
    float acc_damping;
    //    Color startColor;
    float sR;
    float sG;
    float sB;
    float sA;
    //    Color endColor;
    float fR;
    float fG;
    float fB;
    float fA;
    float scaleX;
    float scaleY;
    float timeToLive;
    float totalTTL;

    public Particle(){
        pos = new Vector2();
        vel = new Vector2();
        acc = new Vector2();
        acc_damping = 1;
//        startColor = new Color();
//        endColor = new Color();
        scaleX = .1f;
        scaleY = .1f;
    }

    @Override
    public void reset() {
        animation = null;
        region = null;
        timeToLive = -1;
        acc_damping = 1;
    }



    public void init(float px, float py,
                     float vx, float vy,
                     float ax, float ay, float ad,
                     float ir, float ig, float ib, float ia,
                     float fr, float fg, float fb, float fa,
                     float s,  float t,
                     Animation animation){
        init(px, py, vx, vy, ax, ay, ad, ir, ig, ib, ia, fr, fg, fb, fa, s, s, t, null, animation);
    }

    public void init(float px, float py,
                     float vx, float vy,
                     float ax, float ay, float ad,
                     float ir, float ig, float ib, float ia,
                     float fr, float fg, float fb, float fa,
                     float s,  float t,
                     TextureRegion reg){
        init(px, py, vx, vy, ax, ay, ad, ir, ig, ib, ia, fr, fg, fb, fa, s, s, t, reg, null);
    }

    public void init(float px, float py,
                     float vx, float vy,
                     float ax, float ay, float ad,
                     float ir, float ig, float ib, float ia,
                     float fr, float fg, float fb, float fa,
                     float sX, float sY,  float t,
                     TextureRegion reg, Animation animation) {

        pos.set(px, py);
        vel.set(vx, vy);
        acc.set(ax, ay);
        acc_damping = ad;

        sR = ir;
        sG = ig;
        sB = ib;
        sA = ia;

        fR = fr;
        fG = fg;
        fB = fb;
        fA = fa;
        scaleX = sX;
        scaleY = sY;
        timeToLive = t;
        totalTTL = t;
        region = reg;
        this.animation = animation;
    }

    public void update(float dt){
        timeToLive -= dt;
        vel.add(acc.x * dt, acc.y * dt);
        pos.add(vel.x * dt, vel.y * dt);

        acc.scl(acc_damping);
        if (acc.epsilonEquals(0.0f, 0.0f, 0.01f)) {
            acc.set(0f, 0f);
        }
    }

    public void render(SpriteBatch batch){
        // Equivalent to finalColor.cpy().lerp(initialColor, timeToLive / totalTTL)
        // but without the allocation for cpy()
        float t = timeToLive / totalTTL;

        float r = fR + t * (sR - fR);
        float g = fG + t * (sG - fG);
        float b = fB + t * (sB - fB);
        float a = fA + t * (sA - fA);


        r = (r < 0) ? 0 : (r > 1) ? 1 : r;
        g = (g < 0) ? 0 : (g > 1) ? 1 : g;
        b = (b < 0) ? 0 : (b > 1) ? 1 : b;
        a = (a < 0) ? 0 : (a > 1) ? 1 : a;

        batch.setColor(r, g, b, a);
        if (animation != null){
            float totalDuration = animation.getAnimationDuration();
            batch.draw(animation.getKeyFrame(t * totalDuration), pos.x, pos.y, scaleX, scaleY);
        } else {
            batch.draw(region, pos.x, pos.y, scaleX, scaleY);
        }
        batch.setColor(Color.WHITE);
    }
}
