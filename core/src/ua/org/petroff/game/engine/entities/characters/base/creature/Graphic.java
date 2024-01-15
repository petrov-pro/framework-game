package ua.org.petroff.game.engine.entities.characters.base.creature;

import com.badlogic.gdx.Gdx;
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

public class Graphic extends ua.org.petroff.game.engine.entities.characters.base.Graphic {

    protected String regionName;
    protected float velocityFireAnimation;
    public ParticleEffect effectBlood;

    public Graphic(
            Assets asset,
            GraphicResources graphicResources,
            String regionName,
            float velocityFireAnimation
    ) {
        super(asset, graphicResources);
        this.regionName = regionName;
        this.velocityFireAnimation = velocityFireAnimation;
        loadAnimation();
        loadParticle();
    }

    private void loadParticle() {
        effectBlood = new ParticleEffect();
        effectBlood.load(Gdx.files.internal(Assets.PATH + "particles/blood/blood.p"), asset.getAtlas());
        effectBlood.scaleEffect(Settings.SCALE);
    }

    private void loadAnimation() {

        TextureAtlas atlas = asset.getAtlas();
        TextureRegion[] playerRegionsRight = new TextureRegion[7];
        TextureRegion[] playerRegionsLeft = new TextureRegion[7];
        TextureRegion[] playerRegionsJumpLeft = new TextureRegion[7];
        TextureRegion[] playerRegionsJumpRight = new TextureRegion[7];
        TextureRegion[] playerRegionsJumpStay = new TextureRegion[7];
        TextureRegion[] playerRegionsDied = new TextureRegion[5];
        TextureRegion[] playerRegionsFireRight = new TextureRegion[13];
        TextureRegion[] playerRegionsFireLeft = new TextureRegion[13];

        TextureAtlas.AtlasRegion creatureTexture = atlas.findRegion(regionName);
        TextureRegion creature = new TextureRegion(creatureTexture, 0, 68, 64, 64);
        sprite = new Sprite(creature);
        sprite.setScale(Settings.SCALE);
        sprite.setOriginCenter();

        for (int i = 0; i < 7; i++) {
            playerRegionsRight[i] = new TextureRegion(creatureTexture, 64 * i, 324, 64, 64);
        }
        Animation walkAnimationRight = new Animation(0.1f, (Object[]) playerRegionsRight);

        for (int i = 0; i < 7; i++) {
            playerRegionsLeft[i] = new TextureRegion(creatureTexture, 64 * i, 452, 64, 64);
        }
        Animation walkAnimationLeft = new Animation(0.1f, (Object[]) playerRegionsLeft);

        for (int i = 0; i < 7; i++) {
            playerRegionsJumpLeft[i] = new TextureRegion(creatureTexture, 64 * i, 4, 64, 64);
        }
        Animation jumpAnimationLeft = new Animation(0.2f, (Object[]) playerRegionsJumpLeft);

        for (int i = 0; i < 7; i++) {
            playerRegionsJumpRight[i] = new TextureRegion(creatureTexture, 64 * i, 132, 64, 64);
        }
        Animation jumpAnimationRight = new Animation(0.2f, (Object[]) playerRegionsJumpRight);

        for (int i = 0; i < 7; i++) {
            playerRegionsJumpStay[i] = new TextureRegion(creatureTexture, 64 * i, 68, 64, 64);
        }
        Animation jumpAnimationStay = new Animation(0.2f, (Object[]) playerRegionsJumpStay);

        for (int i = 0; i < 5; i++) {
            playerRegionsDied[i] = new TextureRegion(creatureTexture, 64 * i, 770, 64, 64);
        }
        Animation diedAnimationStay = new Animation(0.1f, (Object[]) playerRegionsDied);

        for (int i = 0; i < 13; i++) {
            playerRegionsFireRight[i] = new TextureRegion(creatureTexture, 64 * i, 706, 64, 64);
        }
        Animation fireAnimationRight = new Animation(0.1f, (Object[]) playerRegionsFireRight);

        for (int i = 0; i < 13; i++) {
            playerRegionsFireLeft[i] = new TextureRegion(creatureTexture, 64 * i, 642, 64, 64);
        }
        Animation fireAnimationLeft = new Animation(0.1f, (Object[]) playerRegionsFireLeft);

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
