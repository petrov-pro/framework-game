package ua.org.petroff.game.engine.entities.characters.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.characters.base.Static;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.characters.base.creature.View;

public class EnemyBowGraphic extends ua.org.petroff.game.engine.entities.characters.base.Graphic {

    protected float velocityFireAnimation;
    public ParticleEffect effectBlood;

    public EnemyBowGraphic(
            Assets asset,
            GraphicResources graphicResources,
            String regionName,
            float velocityFireAnimation
    ) {
        super(asset, graphicResources);
        this.regionName = regionName;
        this.velocityFireAnimation = velocityFireAnimation;
        loadAnimation();
    }

    private void loadAnimation() {

        TextureAtlas atlas = asset.getAtlas();
        TextureRegion[] creatureRegionsRight = new TextureRegion[8];
        TextureRegion[] creatureRegionsLeft = new TextureRegion[8];
        TextureRegion[] creatureRegionsJumpLeft = new TextureRegion[7];
        TextureRegion[] creatureRegionsJumpRight = new TextureRegion[7];
        TextureRegion[] creatureRegionsJumpStay = new TextureRegion[7];
        TextureRegion[] creatureRegionsDied = new TextureRegion[5];
        TextureRegion[] creatureRegionsFireRight = new TextureRegion[13];
        TextureRegion[] creatureRegionsFireLeft = new TextureRegion[13];

        TextureAtlas.AtlasRegion creatureTexture1 = atlas.findRegion(regionName);
        TextureAtlas.AtlasRegion creatureTexture2 = atlas.findRegion(regionName + "_two");
        TextureRegion creature = new TextureRegion(creatureTexture2, 0, 258, 64, 64);
        sprite = new Sprite(creature);
        sprite.setScale(Settings.SCALE);
        sprite.setOriginCenter();

        for (int i = 0; i < 8; i++) {
            creatureRegionsRight[i] = new TextureRegion(creatureTexture2, (128 * i) + 30, 866, 64, 64);
        }
        Animation walkAnimationRight = new Animation(0.1f, (Object[]) creatureRegionsRight);

        for (int i = 0; i < 8; i++) {
            creatureRegionsLeft[i] = new TextureRegion(creatureTexture2, (128 * i) + 30, 610, 64, 64);
        }
        Animation walkAnimationLeft = new Animation(0.1f, (Object[]) creatureRegionsLeft);

        for (int i = 0; i < 7; i++) {
            creatureRegionsJumpLeft[i] = new TextureRegion(creatureTexture1, 64 * i, 68, 64, 64);
        }
        Animation jumpAnimationLeft = new Animation(0.2f, (Object[]) creatureRegionsJumpLeft);

        for (int i = 0; i < 7; i++) {
            creatureRegionsJumpRight[i] = new TextureRegion(creatureTexture1, 64 * i, 198, 64, 64);
        }
        Animation jumpAnimationRight = new Animation(0.2f, (Object[]) creatureRegionsJumpRight);

        for (int i = 0; i < 7; i++) {
            creatureRegionsJumpStay[i] = new TextureRegion(creatureTexture1, 64 * i, 133, 64, 64);
        }
        Animation jumpAnimationStay = new Animation(0.2f, (Object[]) creatureRegionsJumpStay);

        for (int i = 0; i < 5; i++) {
            creatureRegionsDied[i] = new TextureRegion(creatureTexture2, (64 * i) + 2, 384, 64, 64);
        }
        Animation diedAnimationStay = new Animation(0.1f, (Object[]) creatureRegionsDied);

        for (int i = 0; i < 13; i++) {
            creatureRegionsFireRight[i] = new TextureRegion(creatureTexture2, 64 * i, 320, 64, 64);
        }
        Animation fireAnimationRight = new Animation(0.1f, (Object[]) creatureRegionsFireRight);

        for (int i = 0; i < 13; i++) {
            creatureRegionsFireLeft[i] = new TextureRegion(creatureTexture2, 64 * i, 188, 64, 64);
        }
        Animation fireAnimationLeft = new Animation(0.1f, (Object[]) creatureRegionsFireLeft);

        defaultActionFrame = new Static(creature, StateInterface.State.MOVE, WorldInterface.Vector.STAY);
        graphics.put(View.getFrameName(StateInterface.State.MOVE, WorldInterface.Vector.STAY),
                defaultActionFrame);
        graphics.put(View.getFrameName(StateInterface.State.MOVE, WorldInterface.Vector.RIGHT),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(walkAnimationRight, StateInterface.State.MOVE, WorldInterface.Vector.RIGHT, true));
        graphics.put(View.getFrameName(StateInterface.State.MOVE, WorldInterface.Vector.LEFT),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(walkAnimationLeft, StateInterface.State.MOVE, WorldInterface.Vector.LEFT, true));
        graphics.put(View.getFrameName(StateInterface.State.JUMP, WorldInterface.Vector.LEFT),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(jumpAnimationLeft, StateInterface.State.JUMP, WorldInterface.Vector.LEFT, false));
        graphics.put(View.getFrameName(StateInterface.State.JUMP, WorldInterface.Vector.RIGHT),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(jumpAnimationRight, StateInterface.State.JUMP, WorldInterface.Vector.RIGHT, false));
        graphics.put(View.getFrameName(StateInterface.State.JUMP, WorldInterface.Vector.STAY),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(jumpAnimationStay, StateInterface.State.JUMP, WorldInterface.Vector.STAY, false));
        graphics.put(View.getFrameName(StateInterface.State.DIED, WorldInterface.Vector.STAY),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(diedAnimationStay, StateInterface.State.DIED, false));
        graphics.put(View.getFrameName(StateInterface.State.FIRE, WorldInterface.Vector.RIGHT),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(fireAnimationRight, StateInterface.State.FIRE, WorldInterface.Vector.RIGHT, false, velocityFireAnimation));
        graphics.put(View.getFrameName(StateInterface.State.FIRE, WorldInterface.Vector.LEFT),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(fireAnimationLeft, StateInterface.State.FIRE, WorldInterface.Vector.LEFT, false, velocityFireAnimation));
    }

}
