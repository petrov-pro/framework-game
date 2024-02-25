package ua.org.petroff.game.engine.entities.player;

import ua.org.petroff.game.engine.interfaces.SkinInterface;
import ua.org.petroff.game.engine.characters.graphicLoaders.BareWeaponGraphic;
import ua.org.petroff.game.engine.characters.graphicLoaders.RangedWeaponGraphic;
import ua.org.petroff.game.engine.weapons.WeaponInterface;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class Graphic extends ua.org.petroff.game.engine.characters.base.Graphic {

    public Graphic(Assets asset, GraphicResources graphicResources) {
        super(asset, graphicResources);
        graphics.put(
                SkinInterface.Type.DEFAULT,
                (new BareWeaponGraphic()).loadAnimation(sprite, asset, Player.DESCRIPTOR + WeaponInterface.Type.BARE, Player.FIRE_BARE_SPEED)
        );

        graphics.put(
                SkinInterface.Type.BOW,
                (new RangedWeaponGraphic()).loadAnimation(sprite, asset, Player.DESCRIPTOR + WeaponInterface.Type.BOW, Player.FIRE_ARROW_SPEED)
        );
    }
}
