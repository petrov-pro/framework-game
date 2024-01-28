package ua.org.petroff.game.engine.entities.characters.base.creature;

import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.LocalListener;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class CreatureListener extends LocalListener<Creature> {

    public CreatureListener(GameResources gameResources, Creature entity) {
        super(gameResources, entity);
    }

    @Override
    public void beginContact(Contact contact) {
        super.beginContact(contact);
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

    @Override
    protected boolean shouldContact(Fixture entityA, Fixture entityB) {
        return entityA.getBody().getUserData() instanceof CreatureInterface
                && !entityA.isSensor()
                && entityB.isSensor()
                && entityB.getUserData() instanceof CreatureInterface;
    }

    private void handleContact(Object creatureData, Object entityData) {
        gameResources.getMessageManger().dispatchMessage(
                this,
                (Telegraph) entityData,
                StateInterface.State.CREATURE_COLLISION.telegramNumber,
                creatureData,
                false
        );
    }

}
