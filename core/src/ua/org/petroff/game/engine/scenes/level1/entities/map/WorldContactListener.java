package ua.org.petroff.game.engine.scenes.level1.entities.map;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import java.util.ArrayList;

public class WorldContactListener implements ContactListener {

    private final ArrayList<ContactListener> contactHandlers = new ArrayList();

    @Override
    public void beginContact(Contact contact) {
        for (ContactListener contactHandler : contactHandlers) {
            contactHandler.beginContact(contact);
        }
    }

    @Override
    public void endContact(Contact contact) {
        for (ContactListener contactHandler : contactHandlers) {
            contactHandler.endContact(contact);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        for (ContactListener contactHandler : contactHandlers) {
            contactHandler.preSolve(contact, oldManifold);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        for (ContactListener contactHandler : contactHandlers) {
            contactHandler.postSolve(contact, impulse);
        }
    }

    public void addListener(ContactListener contactHandler) {
        contactHandlers.add(contactHandler);
    }

};
