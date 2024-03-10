package ua.org.petroff.game.engine.entities.enemies.simple;

import com.badlogic.gdx.Gdx;
import ua.org.petroff.game.engine.interfaces.SkinInterface;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.characters.creature.View;
import ua.org.petroff.game.engine.characters.graphicLoaders.MeleeWeaponGraphic;
import ua.org.petroff.game.engine.characters.visual.effects.Blood;
import ua.org.petroff.game.engine.characters.enemies.Enemy;
import ua.org.petroff.game.engine.characters.enemies.EnemyGraphic;
import ua.org.petroff.game.engine.entities.weapons.melee.Telegram;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.Timer;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class DirtyManSword extends Enemy {

    public static final String DESCRIPTOR = "dirty_man_sword";
    public static final float FIRE_SPEED = 0.00005f;
    public static final int FIRE_DAMAGE = 30;

    public DirtyManSword(int x, int y, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        super(x, y, asset, gameResources, graphicResources, DESCRIPTOR);
        skin = SkinInterface.Type.DEFAULT;
        view = new View(
                asset,
                graphicResources,
                this,
                new EnemyGraphic(
                        asset,
                        graphicResources,
                        new MeleeWeaponGraphic(),
                        DESCRIPTOR,
                        FIRE_SPEED
                ),
                new Blood(asset, graphicResources)
        );
        hitRange = 2f;
    }

    @Override
    public boolean fire() {
        if (!Timer.runReset(this.toString() + "fire", 0.5f)) {
            return false;
        }

        boolean canFire = super.fire();

        if (canFire) {
            gameResources.getMessageManger().dispatchMessage(
                    StateInterface.State.FIRE.telegramNumber,
                    new Telegram(
                            WeaponInterface.Type.SWORD,
                            vector,
                            body.getPosition().cpy(),
                            FIRE_DAMAGE,
                            bodyWidth / 2,
                            0.2f,
                            0.6f
                    )
            );
        }

        return canFire;
    }

    @Override
    public boolean withinReachFire() {
        boolean canTouch = super.withinReachFire();

        if (canTouch && body.getLinearVelocity().len() < 1f) {
            return false;
        }

        return canTouch;
    }

}
