package ua.org.petroff.game.engine.characters.graphicLoaders;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.interfaces.GraphicLoaderInterface;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.characters.base.GraphicElement;
import ua.org.petroff.game.engine.characters.base.Static;
import ua.org.petroff.game.engine.characters.creature.View;
import ua.org.petroff.game.engine.util.Assets;

public class MeleeWeaponGraphic implements GraphicLoaderInterface {

    /*
    1000x900-1100
    
     */
    @Override
    public HashMap<String, GraphicElement> loadAnimation(Sprite sprite, Assets asset, String regionName, float velocityFireAnimation) {
        TextureAtlas atlas = asset.getAtlas();
        TextureRegion[] creatureRegionsRight = new TextureRegion[8];
        TextureRegion[] creatureRegionsLeft = new TextureRegion[8];
        TextureRegion[] creatureRegionsJumpLeft = new TextureRegion[7];
        TextureRegion[] creatureRegionsJumpRight = new TextureRegion[7];
        TextureRegion[] creatureRegionsJumpStay = new TextureRegion[7];
        TextureRegion[] creatureRegionsDied = new TextureRegion[5];
        TextureRegion[] creatureRegionsHit = new TextureRegion[4];
        TextureRegion[] creatureRegionsFireRight = new TextureRegion[5];
        TextureRegion[] creatureRegionsFireLeft = new TextureRegion[5];

        TextureAtlas.AtlasRegion creatureTexture1 = atlas.findRegion(regionName);
        TextureAtlas.AtlasRegion creatureTexture2 = atlas.findRegion(regionName + "2");
        TextureRegion creature = new TextureRegion(creatureTexture1, 0, 134, 64, 64);
        sprite.setRegion(creature);
        sprite.setSize(creature.getRegionWidth(), creature.getRegionHeight());
        sprite.setScale(Settings.SCALE);
        sprite.setOriginCenter();

        for (int i = 0; i < creatureRegionsRight.length; i++) {
            creatureRegionsRight[i] = new TextureRegion(creatureTexture1, 64 * i, 710, 64, 64);
        }
        Animation walkAnimationRight = new Animation(0.1f, (Object[]) creatureRegionsRight);

        for (int i = 0; i < creatureRegionsLeft.length; i++) {
            creatureRegionsLeft[i] = new TextureRegion(creatureTexture1, 64 * i, 582, 64, 64);
        }
        Animation walkAnimationLeft = new Animation(0.1f, (Object[]) creatureRegionsLeft);

        for (int i = 0; i < creatureRegionsJumpLeft.length; i++) {
            creatureRegionsJumpLeft[i] = new TextureRegion(creatureTexture1, 64 * i, 70, 64, 64);
        }
        Animation jumpAnimationLeft = new Animation(0.2f, (Object[]) creatureRegionsJumpLeft);

        for (int i = 0; i < creatureRegionsJumpRight.length; i++) {
            creatureRegionsJumpRight[i] = new TextureRegion(creatureTexture1, 64 * i, 198, 64, 64);
        }
        Animation jumpAnimationRight = new Animation(0.2f, (Object[]) creatureRegionsJumpRight);

        for (int i = 0; i < creatureRegionsJumpStay.length; i++) {
            creatureRegionsJumpStay[i] = new TextureRegion(creatureTexture1, 64 * i, 133, 64, 64);
        }
        Animation jumpAnimationStay = new Animation(0.2f, (Object[]) creatureRegionsJumpStay);

        for (int i = 0; i < creatureRegionsDied.length; i++) {
            creatureRegionsDied[i] = new TextureRegion(creatureTexture2, (64 * i) + 2, 184, 64, 64);
        }
        Animation diedAnimationStay = new Animation(0.1f, (Object[]) creatureRegionsDied);

        for (int i = 0; i < creatureRegionsHit.length; i++) {
            creatureRegionsHit[i] = new TextureRegion(creatureTexture2, (64 * i) + 2, 184, 64, 64);
        }
        Animation hitAnimationStay = new Animation(0.1f, (Object[]) creatureRegionsHit);

        creatureRegionsFireRight[0] = new TextureRegion(creatureTexture2, (64 * 0) + 64, 890, 64, 64);
        creatureRegionsFireRight[1] = new TextureRegion(creatureTexture2, (64 * 1) + 196, 890, 64, 64);
        creatureRegionsFireRight[2] = new TextureRegion(creatureTexture2, (64 * 2) + 322, 890, 64, 64);
        creatureRegionsFireRight[3] = new TextureRegion(creatureTexture2, (64 * 3) + 452, 890, 64, 64);
        creatureRegionsFireRight[4] = new TextureRegion(creatureTexture2, (64 * 4) + 584, 890, 64, 64);

        Animation fireAnimationRight = new Animation(0.1f, (Object[]) creatureRegionsFireRight);

        creatureRegionsFireLeft[0] = new TextureRegion(creatureTexture2, (64 * 0) + 64, 506, 64, 64);
        creatureRegionsFireLeft[1] = new TextureRegion(creatureTexture2, (64 * 1) + 190, 506, 64, 64);
        creatureRegionsFireLeft[2] = new TextureRegion(creatureTexture2, (64 * 2) + 322, 506, 64, 64);
        creatureRegionsFireLeft[3] = new TextureRegion(creatureTexture2, (64 * 3) + 442, 506, 64, 64);
        creatureRegionsFireLeft[4] = new TextureRegion(creatureTexture2, (64 * 4) + 570, 506, 64, 64);

        Animation fireAnimationLeft = new Animation(0.1f, (Object[]) creatureRegionsFireLeft);

        HashMap<String, GraphicElement> graphic = new HashMap();
        graphic.put(View.getFrameName(StateInterface.State.MOVE, WorldInterface.Vector.STAY),
                new Static(creature, StateInterface.State.MOVE, WorldInterface.Vector.STAY));
        graphic.put(View.getFrameName(StateInterface.State.MOVE, WorldInterface.Vector.RIGHT),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(walkAnimationRight, StateInterface.State.MOVE, WorldInterface.Vector.RIGHT, true));
        graphic.put(View.getFrameName(StateInterface.State.MOVE, WorldInterface.Vector.LEFT),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(walkAnimationLeft, StateInterface.State.MOVE, WorldInterface.Vector.LEFT, true));
        graphic.put(View.getFrameName(StateInterface.State.JUMP, WorldInterface.Vector.LEFT),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(jumpAnimationLeft, StateInterface.State.JUMP, WorldInterface.Vector.LEFT, false));
        graphic.put(View.getFrameName(StateInterface.State.JUMP, WorldInterface.Vector.RIGHT),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(jumpAnimationRight, StateInterface.State.JUMP, WorldInterface.Vector.RIGHT, false));
        graphic.put(View.getFrameName(StateInterface.State.JUMP, WorldInterface.Vector.STAY),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(jumpAnimationStay, StateInterface.State.JUMP, WorldInterface.Vector.STAY, false));
        graphic.put(View.getFrameName(StateInterface.State.DIED, WorldInterface.Vector.STAY),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(diedAnimationStay, StateInterface.State.DIED, false));
        graphic.put(View.getFrameName(StateInterface.State.HIT, WorldInterface.Vector.STAY),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(hitAnimationStay, StateInterface.State.HIT, false));
        graphic.put(View.getFrameName(StateInterface.State.FIRE, WorldInterface.Vector.RIGHT),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(fireAnimationRight, StateInterface.State.FIRE, WorldInterface.Vector.RIGHT, false, velocityFireAnimation));
        graphic.put(View.getFrameName(StateInterface.State.FIRE, WorldInterface.Vector.LEFT),
                new ua.org.petroff.game.engine.entities.player.graphics.Animation(fireAnimationLeft, StateInterface.State.FIRE, WorldInterface.Vector.LEFT, false, velocityFireAnimation));

        return graphic;
    }

}
