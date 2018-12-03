package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld43.utils.Assets;

public class Pilot {

    public enum Type { cat, dog }

    private Type type;
    private PlayerShip ship;

    // TODO: cache pilot related textures and such here
    public TextureRegion textureFull;
    public TextureRegion textureHead;
    public Animation<TextureRegion> textureAnimation;

    public Pilot(PlayerShip ship, Assets assets, Type type) {
        this.ship = ship;
        this.type = type;

        switch (type) {
            case cat: {
                this.textureFull = assets.atlas.findRegion("cat-full");
                this.textureHead = assets.atlas.findRegion("cat-head");
                this.textureAnimation = assets.talkingCatAnimation;
            } break;
            case dog: {
                this.textureFull = assets.atlas.findRegion("dog-full");
                this.textureHead = assets.atlas.findRegion("dog-head");
                this.textureAnimation = assets.talkingDogAnimation;

            } break;
        }
        if (textureFull == null) throw new GdxRuntimeException("Couldn't find full sprite for pilot of type '" + type.name() + "'");
        if (textureHead == null) throw new GdxRuntimeException("Couldn't find head sprite for pilot of type '" + type.name() + "'");
    }

    public Type getType() { return type; }

}
