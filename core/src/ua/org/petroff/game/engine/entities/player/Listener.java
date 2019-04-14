package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import ua.org.petroff.game.engine.entities.BodyDescriber;
import ua.org.petroff.game.engine.entities.Interfaces.EntityListenerInterface;

public class Listener implements EntityListenerInterface {

    private final Player model;

    public Listener(Player model) {
        this.model = model;
    }

    @Override
    public void beginContact(Contact contact) {
        if (isBodyDescriber(contact.getFixtureA().getUserData())) {
            model.grounded();
        }

        if (isBodyDescriber(contact.getFixtureB().getUserData())) {
            model.grounded();
        }

    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private boolean isBodyDescriber(Object userData) {
        return userData instanceof BodyDescriber && userData.toString().equals(Player.DESCRIPTOR)
                && ((BodyDescriber) userData).getType().equals(BodyDescriber.BODY_FOOT);
    }

}
