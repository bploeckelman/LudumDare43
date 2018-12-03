package lando.systems.ld43.particles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lando.systems.ld43.utils.Assets;

public class ParticleSystem {

    private final Array<Particle> activeParticles = new Array<Particle>(false, 1028);
    private final Pool<Particle> particlePool = Pools.get(Particle.class, 2000);
    private Assets assets;

    public ParticleSystem(Assets assets) {
        this.assets = assets;
        for (int i =0; i < 800; i++){
            particlePool.free(new Particle());
        }
    }

    public void addSmoke(float x, float y){
        int smokeParticles = 1;
        for (int i = 0; i < smokeParticles; i++){
            Particle particle = particlePool.obtain();

            float scale = MathUtils.random(30f, 50f);

            float posX = x + MathUtils.random(-20f, 20f) - scale/2f;
            float posY = y + MathUtils.random(-20f, 20f) - scale/2f;

            float velX = MathUtils.random(-20f, 20f);
            float velY =  MathUtils.random(-20f, 20f);
            float ttl = MathUtils.random(.5f, 1.5f);
            float grayValue = MathUtils.random(.7f) + .3f;

            particle.init(posX, posY, velX, velY, -velX, -velY,
                    0.5f, grayValue, grayValue, grayValue, 1f,
                    grayValue, grayValue, grayValue, 0f, scale, ttl, assets.smoke);
            activeParticles.add(particle);
        }
    }

    public void addExplosion(float x, float y, float width, float height){
        Particle particle = particlePool.obtain();

        float posX = x - width/2f;
        float posY = y - height/2f;

        float velX = 0;
        float velY = 0;
        float ttl = MathUtils.random(.5f, 1.5f);
        float white = 1f;

        particle.init(posX, posY, velX, velY, -velX, -velY,
                0f, white, white, white, 1f,
                white, white, white, .5f, width, height, ttl, null, assets.explosionAnimation);
        activeParticles.add(particle);
    }

    public void update(float dt){
        int len = activeParticles.size;
        for (int i = len -1; i >= 0; i--){
            Particle part = activeParticles.get(i);
            part.update(dt);
            if (part.timeToLive <= 0){
                activeParticles.removeIndex(i);
                particlePool.free(part);
            }
        }
    }

    public void render(SpriteBatch batch){
        for (Particle part : activeParticles){
            part.render(batch);
        }
    }

    public void clearParticles(){
        particlePool.freeAll(activeParticles);
        activeParticles.clear();
    }
}
