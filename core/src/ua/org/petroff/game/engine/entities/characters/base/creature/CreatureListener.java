package ua.org.petroff.game.engine.entities.characters.base.creature;

import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.Listener;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class CreatureListener extends Listener {

    public CreatureListener(GameResources gameResources) {
        super(gameResources);
    }

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        if (shouldContact(contact.getFixtureA(), contact.getFixtureB())) {
            handleContact(userDataA, userDataB);

            return;
        }

        if (shouldContact(contact.getFixtureB(), contact.getFixtureA())) {
            handleContact(userDataB, userDataA);
        }
    }

    private boolean shouldContact(Fixture creature, Fixture victim) {
        return creature.getBody().getUserData() instanceof CreatureInterface
                && victim.getBody().getUserData() instanceof CreatureInterface
                && victim.isSensor();

    }

    private void handleContact(Object creatureData, Object entityData) {
        if (entityData instanceof CreatureInterface) {
            gameResources.getMessageManger().dispatchMessage(
                    this,
                    (Telegraph) entityData,
                    StateInterface.State.CREATURE_COLLISION.telegramNumber,
                    creatureData,
                    false
            );
        }
    }

}
