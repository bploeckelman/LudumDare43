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
                this.textureFull = assets.animationComputer.getKeyFrames()[0];
                this.textureHead = assets.animationComputer.getKeyFrames()[1];
                this.textureAnimation = assets.animationComputer;
            } break;
            case boss1: {
                this.textureFull = assets.animationRaptor.getKeyFrames()[0];
                this.textureHead = assets.animationRaptor.getKeyFrames()[1];
                this.textureAnimation = assets.animationRaptor;
            } break;
            case boss2: {
                this.textureFull = assets.animationOwl.getKeyFrames()[0];
                this.textureHead = assets.animationOwl.getKeyFrames()[1];
                this.textureAnimation = assets.animationOwl;
            } break;
            case boss3: {
                this.textureFull = assets.animationRacoon.getKeyFrames()[0];
                this.textureHead = assets.animationRacoon.getKeyFrames()[1];
                this.textureAnimation = assets.animationRacoon;
            } break;
            case boss4: {
                this.textureFull = assets.animationSteve.getKeyFrames()[0];
                this.textureHead = assets.animationSteve.getKeyFrames()[1];
                this.textureAnimation = assets.animationSteve;
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
