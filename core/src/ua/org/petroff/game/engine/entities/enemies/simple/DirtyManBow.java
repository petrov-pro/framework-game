package ua.org.petroff.game.engine.entities.enemies.simple;

import ua.org.petroff.game.engine.interfaces.SkinInterface;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.characters.enemies.EnemyGraphic;
import ua.org.petroff.game.engine.characters.creature.View;
import ua.org.petroff.game.engine.characters.graphicLoaders.RangedWeaponGraphic;
import ua.org.petroff.game.engine.characters.visual.effects.Blood;
import ua.org.petroff.game.engine.characters.enemies.Enemy;
import ua.org.petroff.game.engine.entities.weapons.ranged.Telegram;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class DirtyManBow extends Enemy {

    public static final String DESCRIPTOR = "dirty_man";
    public static final float FIRE_SPEED = 0.0035f;
    public static final float FIRE_ARROW_FORCE = 7f;

    public DirtyManBow(int x, int y, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        super(x, y, asset, gameResources, graphicResources, DESCRIPTOR);
        skin = SkinInterface.Type.BOW;
        view = new View(
                asset,
                graphicResources,
                this,
                new EnemyGraphic(
                        asset,
                        graphicResources,
                        new RangedWeaponGraphic(),
                        skin,
                        DESCRIPTOR,
                        FIRE_SPEED
                ),
                new Blood(asset, graphicResources)
        );
    }

    @Override
    public boolean fire() {
        boolean canFire = super.fire();

        if (canFire) {
            float x = body.getPosition().x + (vector == WorldInterface.Vector.RIGHT ? 0.5f : -0.5f);
            float y = body.getPosition().y;
            float forceX = (vector == WorldInterface.Vector.RIGHT) ? FIRE_ARROW_FORCE : -FIRE_ARROW_FORCE;

            gameResources.getMessageManger().dispatchMessage(StateInterface.State.FIRE.telegramNumber, new Telegram(WeaponInterface.Type.BOW, x, y, forceX));
        }

        return canFire;
    }

}
