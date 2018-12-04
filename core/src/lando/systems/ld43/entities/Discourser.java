package lando.systems.ld43.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld43.utils.Assets;

public class Discourser {
    public enum Type { system, boss1, boss2, boss3, boss4, finalBoss, specialBoss }

    private Discourser.Type type;

    public Animation<TextureRegion> textureAnimation;

    public Discourser(Assets assets, Discourser.Type type) {
        this.type = type;

        switch (type) {
            case system: {
                this.textureAnimation = assets.animationComputer;
            } break;
            case boss1: {
                this.textureAnimation = assets.animationRaptor;
            } break;
            case boss2: {
                this.textureAnimation = assets.animationOwl;
            } break;
            case boss3: {
                this.textureAnimation = assets.animationRacoon;
            } break;
            case boss4: {
                this.textureAnimation = assets.animationSteve;
            } break;
            case finalBoss: {
                this.textureAnimation = assets.animationPortraitFinalBoss;
            } break;
            case specialBoss: {
                this.textureAnimation = assets.animationPortraitFinalBoss;
            } break;
        }
        if (textureAnimation == null) throw new GdxRuntimeException("Couldn't find animation for speaker of type '" + type.name() + "'");
    }

}
