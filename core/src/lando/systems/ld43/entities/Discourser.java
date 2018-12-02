package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld43.utils.Assets;

public class Discourser {
    public enum Type { system, boss1, boss2 }

    private Discourser.Type type;

    // TODO: cache pilot related textures and such here
    public TextureRegion textureFull;
    public TextureRegion textureHead;

    public Discourser(Assets assets, Discourser.Type type) {
        this.type = type;

        switch (type) {
            case system: {
                this.textureFull = assets.atlas.findRegion("cat-full");
                this.textureHead = assets.atlas.findRegion("cat-head");
            } break;
            case boss1: {
                this.textureFull = assets.atlas.findRegion("badlogic");
                this.textureHead = assets.atlas.findRegion("badlogic");
            } break;
            case boss2: {
                this.textureFull = assets.atlas.findRegion("dog-full");
                this.textureHead = assets.atlas.findRegion("dog-head");
            } break;
        }
        if (textureFull == null) throw new GdxRuntimeException("Couldn't find full sprite for pilot of type '" + type.name() + "'");
        if (textureHead == null) throw new GdxRuntimeException("Couldn't find head sprite for pilot of type '" + type.name() + "'");
    }

}
