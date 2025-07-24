package ua.org.petroff.game.engine.entities.equipments;

import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import ua.org.petroff.game.engine.characters.creature.CreatureInterface;
import ua.org.petroff.game.engine.entities.Listener;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class EquipmentListener extends Listener {

    public EquipmentListener(GameResources gameResources) {
        super(gameResources);
    }

    @Override
    public void beginContact(Contact contact) {

        if (shouldContact(contact.getFixtureA(), contact.getFixtureB())) {
            handlerContact(contact.getFixtureA(), contact.getFixtureB());

            return;
        }

        if (shouldContact(contact.getFixtureB(), contact.getFixtureA())) {
            handlerContact(contact.getFixtureB(), contact.getFixtureA());
        }
    }

    protected boolean shouldContact(Fixture entityA, Fixture entityB) {
        return entityA.getBody().getUserData() instanceof CreatureInterface
                && entityB.getBody().getUserData() instanceof EquipmentInterface;
    }

    private void handlerContact(Fixture creature, Fixture equipment) {
        if (!((EquipmentInterface) equipment.getBody().getUserData()).isUsed()) {
            gameResources.getMessageManger().dispatchMessage(
                    (Telegraph) equipment.getBody().getUserData(),
                    (Telegraph) creature.getBody().getUserData(),
                    StateInterface.State.EQUIPMENT.telegramNumber,
                    equipment.getBody().getUserData(),
                    true
            );
        }
    }

}
