package ua.org.petroff.game.engine.entities;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import java.util.ArrayList;
import ua.org.petroff.game.engine.entities.exceptions.NotMyEntityException;
import ua.org.petroff.game.engine.scenes.core.GameResources;

abstract public class LocalListener<T> extends Listener {

    protected final ArrayList<Fixture> list = new ArrayList<>();
    protected final T entity;

    public LocalListener(GameResources gameResources, T entity) {
        super(gameResources);
        this.entity = entity;
    }

    abstract protected boolean shouldContact(Fixture entityA, Fixture entityB);

    @Override
    public void endContact(Contact contact) {
        if (!isOwner(contact)) {
            throw new NotMyEntityException();
        }

        if (shouldContact(contact.getFixtureA(), contact.getFixtureB())) {
            remove(contact.getFixtureA());
            if (list.isEmpty()) {
                trueEndContact(contact.getFixtureA(), contact.getFixtureB());
            }
        } else if (shouldContact(contact.getFixtureB(), contact.getFixtureA())) {
            remove(contact.getFixtureB());
            if (list.isEmpty()) {
                trueEndContact(contact.getFixtureB(), contact.getFixtureA());
            }
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        if (!isOwner(contact)) {
            throw new NotMyEntityException();
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        if (!isOwner(contact)) {
            throw new NotMyEntityException();
        }
    }

    @Override
    public void beginContact(Contact contact) {
        if (!isOwner(contact)) {
            throw new NotMyEntityException();
        }

        if (shouldContact(contact.getFixtureA(), contact.getFixtureB())) {
            add(contact.getFixtureA());
        } else if (shouldContact(contact.getFixtureB(), contact.getFixtureA())) {
            add(contact.getFixtureB());
        }
    }

    public void trueEndContact(Fixture entityA, Fixture entityB) {

    }

    protected void add(Fixture fixture) {
        list.add(fixture);
    }

    protected void remove(Fixture fixture) {
        list.remove(fixture);
    }

    protected boolean isOwner(Contact contact) {
        if (entity == null) {
            throw new RuntimeException("For local listener you should set up entity");
        }
        return contact.getFixtureA().getBody().getUserData() == entity || contact.getFixtureB().getBody().getUserData() == entity;
    }

}
