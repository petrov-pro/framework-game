package ua.org.petroff.game.engine.entities.characters.enemies.simple;

import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.characters.enemies.EnemyBowGraphic;
import ua.org.petroff.game.engine.entities.characters.base.creature.View;
import ua.org.petroff.game.engine.entities.characters.enemies.Enemy;
import ua.org.petroff.game.engine.entities.guns.arrow.Telegram;
import static ua.org.petroff.game.engine.entities.player.Player.FIRE_FORCE;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class DirtyMan extends Enemy {

    public static final String DESCRIPTOR = "dirty_man";
    public static final float FIRE_SPEED = 0.0035f;

    public DirtyMan(int x, int y, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        super(x, y, asset, gameResources, graphicResources, DESCRIPTOR);
        view = new View(asset, graphicResources, this, new EnemyBowGraphic(asset, graphicResources, DESCRIPTOR, FIRE_SPEED));
    }

    @Override
    public boolean fire() {
        boolean canFire = super.fire();

        if (canFire) {
            float x = body.getPosition().x + (vector == WorldInterface.Vector.RIGHT ? 0.5f : -0.5f);
            float y = body.getPosition().y;
            float forceX = (vector == WorldInterface.Vector.RIGHT) ? FIRE_FORCE : -FIRE_FORCE;

            gameResources.getMessageManger().dispatchMessage(StateInterface.State.FIRE.telegramNumber, new Telegram(x, y, forceX));
        }

        return canFire;
    }

}
