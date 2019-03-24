package ua.org.petroff.game.engine.entities.Interfaces;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

public interface EntityListenerInterface {

    public void beginContact(Contact contact);

    public void endContact(Contact contact);

    public void preSolve(Contact contact, Manifold oldManifold);

    public void postSolve(Contact contact, ContactImpulse impulse);

}
