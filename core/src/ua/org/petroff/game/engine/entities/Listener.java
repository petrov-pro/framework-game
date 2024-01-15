package ua.org.petroff.game.engine.entities;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import ua.org.petroff.game.engine.scenes.core.GameResources;

abstract public class Listener<T> implements ContactListener, Telegraph {

    protected final GameResources gameResources;

    public Listener(GameResources gameResources) {
        this.gameResources = gameResources;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        throw new UnsupportedOperationException("Not supported yet.");
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

}
