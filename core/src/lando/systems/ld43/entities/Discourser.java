package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld43.utils.Assets;

public class Discourser {
    public enum Type { system, boss1, boss2, boss3, boss4, finalBoss, specialBoss }

    private Discourser.Type type;

    public TextureRegion textureFull;
    public TextureRegion textureHead;
    public Animation<TextureRegion> textureAnimation;

    public Discourser(Assets assets, Discourser.Type type) {
        this.type = type;

        switch (type) {
            case system: {
                this.textureFull = assets.computerAnimation.getKeyFrames()[0];
                this.textureHead = assets.computerAnimation.getKeyFrames()[1];
                this.textureAnimation = assets.computerAnimation;
            } break;
            case boss1: {
                this.textureFull = assets.atlas.findRegion("badlogic");
                this.textureHead = assets.atlas.findRegion("badlogic");
                this.textureAnimation = assets.badLogicAnimation;
            } break;
            case boss2: {
                this.textureFull = assets.atlas.findRegion("badlogic");
                this.textureHead = assets.atlas.findRegion("badlogic");
                this.textureAnimation = assets.badLogicAnimation;
            } break;
            case boss3: {
                this.textureFull = assets.atlas.findRegion("badlogic");
                this.textureHead = assets.atlas.findRegion("badlogic");
                this.textureAnimation = assets.badLogicAnimation;
            } break;
            case boss4: {
                this.textureFull = assets.atlas.findRegion("badlogic");
                this.textureHead = assets.atlas.findRegion("badlogic");
                this.textureAnimation = assets.badLogicAnimation;
            } break;
            case finalBoss: {
                this.textureFull = assets.atlas.findRegion("badlogic");
                this.textureHead = assets.atlas.findRegion("badlogic");
                this.textureAnimation = assets.badLogicAnimation;
            } break;
            case specialBoss: {
                this.textureFull = assets.atlas.findRegion("badlogic");
                this.textureHead = assets.atlas.findRegion("badlogic");
                this.textureAnimation = assets.badLogicAnimation;
            } break;
        }
        if (textureFull == null) throw new GdxRuntimeException("Couldn't find full sprite for pilot of type '" + type.name() + "'");
        if (textureHead == null) throw new GdxRuntimeException("Couldn't find head sprite for pilot of type '" + type.name() + "'");
    }

}
