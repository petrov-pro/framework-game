package ua.org.petroff.game.engine.entities.characters.base.creature;

import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import ua.org.petroff.game.engine.entities.Interfaces.GroundedInterface;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.LocalListener;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.entities.map.Surface;

public class GroundListener extends LocalListener<GroundedInterface> {

    public GroundListener(GameResources gameResources, GroundedInterface entity) {
        super(gameResources, entity);
    }

    @Override
    public void beginContact(Contact contact) {
        super.beginContact(contact);

        if (shouldContact(contact.getFixtureA(), contact.getFixtureB())) {
            handlerContact(contact.getFixtureA(), contact.getFixtureB());

            return;
        }

        if (shouldContact(contact.getFixtureB(), contact.getFixtureA())) {
            handlerContact(contact.getFixtureB(), contact.getFixtureA());
        }
    }

    @Override
    public void trueEndContact(Fixture entityA, Fixture entityB) {
        sentGroundTelegram(entityB.getBody().getUserData(), false);
    }

    @Override
    protected boolean shouldContact(Fixture entityA, Fixture entityB) {
        return entityA.getBody().getUserData() instanceof Surface
                && isGroundEntity(entityB);
    }

    private boolean isGroundEntity(Fixture creature) {
        return creature.isSensor()
                && creature.getUserData() instanceof GroundedInterface;
    }

    private void handlerContact(Fixture ground, Fixture creature) {
        if (ground.getUserData().equals(Surface.DEAD_TYPE)) {
            gameResources.getMessageManger().dispatchMessage(
                    0.1f,
                    this,
                    (Telegraph) creature.getBody().getUserData(),
                    StateInterface.State.DIED.telegramNumber,
                    false
            );

            return;
        }

        sentGroundTelegram(creature.getBody().getUserData(), true);

    }

    private void sentGroundTelegram(Object entityData, boolean isGround) {
        gameResources.getMessageManger().dispatchMessage(
                this,
                (Telegraph) entityData,
                StateInterface.State.GROUND.telegramNumber,
                isGround,
                false
        );
    }

}
