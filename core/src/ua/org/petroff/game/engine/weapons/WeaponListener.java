package ua.org.petroff.game.engine.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import ua.org.petroff.game.engine.interfaces.GroundedInterface;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.Listener;
import ua.org.petroff.game.engine.characters.creature.CreatureInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.entities.map.Surface;
import ua.org.petroff.game.engine.equipment.Shield;

public class WeaponListener extends Listener {

    public WeaponListener(GameResources gameResources) {
        super(gameResources);
    }

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        if ((contact.getFixtureA().getUserData() instanceof Shield && !contact.getFixtureA().isSensor())
                || (contact.getFixtureB().getUserData() instanceof Shield && !contact.getFixtureB().isSensor())) {
            return;
        }

        if (shouldContact(contact.getFixtureA())) {
            handleContact(contact, contact.getFixtureA().getUserData(), userDataB);

            return;
        }

        if (shouldContact(contact.getFixtureB())) {
            handleContact(contact, contact.getFixtureB().getUserData(), userDataA);
        }
    }

    private boolean shouldContact(Fixture weapon) {
        return weapon.getUserData() instanceof WeaponInterface
                && !(weapon.getUserData() instanceof RayCastCallback)
                && weapon.isSensor();

    }

    private void handleContact(Contact contact, Object weaponData, Object entityData) {
        if (entityData instanceof CreatureInterface) {
            Vector2 positionA = contact.getFixtureA().getBody().getPosition();
            Vector2 positionB = contact.getFixtureB().getBody().getPosition();
            gameResources.getMessageManger().dispatchMessage(
                    this,
                    (Telegraph) entityData,
                    StateInterface.State.HIT.telegramNumber,
                    ((WeaponInterface) weaponData).setDirectionHit(
                            positionB.cpy().sub(positionA).nor()
                    ),
                    false
            );
        } else if (entityData instanceof Surface
                && weaponData instanceof GroundedInterface) {
            gameResources.getMessageManger().dispatchMessage(
                    this,
                    (Telegraph) weaponData,
                    StateInterface.State.GROUND.telegramNumber,
                    true,
                    false
            );
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        // Compare fields or properties here
        return true;
    }

}
