package ua.org.petroff.game.engine.entities.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import java.util.ArrayList;
import ua.org.petroff.game.engine.entities.exceptions.NotMyEntityException;

public class WorldContactListener implements ContactListener {

    private final ArrayList<ContactListener> contactHandlers = new ArrayList();

    @Override
    public void beginContact(Contact contact) {
        for (ContactListener contactHandler : contactHandlers) {
            try {
                contactHandler.beginContact(contact);
            } catch (NotMyEntityException e) {
                Gdx.app.debug("Listener", "Skip entity");
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        for (ContactListener contactHandler : contactHandlers) {
            try {
                contactHandler.endContact(contact);
            } catch (NotMyEntityException e) {
                Gdx.app.debug("Listener", "Skip entity");
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        for (ContactListener contactHandler : contactHandlers) {

            try {
                contactHandler.preSolve(contact, oldManifold);
            } catch (NotMyEntityException e) {
                Gdx.app.debug("Listener", "Skip entity");
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        for (ContactListener contactHandler : contactHandlers) {
            try {
                contactHandler.postSolve(contact, impulse);
            } catch (NotMyEntityException e) {
                Gdx.app.debug("Listener", "Skip entity");
            }
        }
    }

    public void addListener(ContactListener contactHandler) {
        contactHandlers.add(contactHandler);
    }

};
