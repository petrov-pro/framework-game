package ua.org.petroff.game.engine.entities;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ua.org.petroff.game.engine.scenes.core.GameResources;

abstract public class CommonListener<T> extends Listener {

    private Map<T, ArrayList<Fixture>> toucheFixtures = new HashMap<>();

    public CommonListener(GameResources gameResources) {
        super(gameResources);
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

    @Override
    public void beginContact(Contact contact) {

    }

}
