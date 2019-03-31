package ua.org.petroff.game.engine.scenes.level1.entities.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import ua.org.petroff.game.engine.entities.BodyDescriber;
import ua.org.petroff.game.engine.entities.GroupDescriber;
import ua.org.petroff.game.engine.entities.Interfaces.EntityListenerInterface;

public class SurfaceListener implements EntityListenerInterface {

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getUserData();
        Object userDataB = contact.getFixtureB().getUserData();

        if (isBodyDescriber(userDataA)) {
            handlerDamage((BodyDescriber) userDataA, userDataB);
        }

        if (isBodyDescriber(userDataB)) {
            handlerDamage((BodyDescriber) userDataB, userDataA);
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
        return userData instanceof BodyDescriber && ((BodyDescriber) userData).getGroup().equals(GroupDescriber.SURFACE);
    }

    private void handlerDamage(BodyDescriber surfaceData, Object userData) {
        if (surfaceData.getType().equals(Surface.DAMAGE_TYPE)
                && userData instanceof BodyDescriber
                && ((BodyDescriber) userData).getGroup().equals(GroupDescriber.ALIVE)) {
            Gdx.app.log("CONTACT", "contact ");
        }
    }

}
