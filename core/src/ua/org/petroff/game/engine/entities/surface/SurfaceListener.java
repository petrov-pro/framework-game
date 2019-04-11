package ua.org.petroff.game.engine.entities.surface;

import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import ua.org.petroff.game.engine.entities.BodyDescriber;
import ua.org.petroff.game.engine.entities.GroupDescriber;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.EntityListenerInterface;
import ua.org.petroff.game.engine.entities.TelegramDescriber;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class SurfaceListener implements EntityListenerInterface {

    private final GameResources gameResources;
    private final Surface surface;
    private final Vector2 platfrom = new Vector2();

    public SurfaceListener(GameResources gameResources, Surface surface) {
        this.gameResources = gameResources;
        this.surface = surface;
        gameResources.getWorld().setContactFilter(new ContactFilter() {

            @Override
            public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
                Object userDataA = fixtureA.getUserData();
                Object userDataB = fixtureB.getUserData();

                if (isBodyDescriberSurface(userDataA)) {
                    return shouldContact((BodyDescriber) userDataA, fixtureA, fixtureB);
                }

                if (isBodyDescriberSurface(userDataB)) {
                    return shouldContact((BodyDescriber) userDataB, fixtureB, fixtureA);
                }
                return true;
            }

        });
    }

    private boolean shouldContact(BodyDescriber surfaceData, Fixture fixtureSurface, Fixture fixtureEntity) {

        if (surfaceData.getType().equals(Surface.PLATFORM_TYPE)) {
            Vector2 positionEntity = fixtureEntity.getBody().getPosition();

            //check collise platform
            ChainShape chain = (ChainShape) fixtureSurface.getShape();

            for (int i = 0; i < chain.getVertexCount(); i++) {
                chain.getVertex(i, platfrom);
                if (positionEntity.y < platfrom.y) {
                    return false;
                } else {
                    return true;
                }
            }

        }
        return true;
    }

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getUserData();
        Object userDataB = contact.getFixtureB().getUserData();

        if (isBodyDescriberSurface(userDataA)) {
            handlerContact((BodyDescriber) userDataA, contact.getFixtureA(), userDataB, contact.getFixtureB());
        }

        if (isBodyDescriberSurface(userDataB)) {
            handlerContact((BodyDescriber) userDataB, contact.getFixtureB(), userDataA, contact.getFixtureA());
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

    private boolean isBodyDescriberSurface(Object userData) {
        return userData instanceof BodyDescriber && ((BodyDescriber) userData).getGroup().equals(GroupDescriber.SURFACE);
    }

    private void handlerContact(BodyDescriber surfaceData, Fixture fixtureSurface, Object userData, Fixture fixtureObject) {
        if (userData instanceof BodyDescriber
                && ((BodyDescriber) userData).getGroup().equals(GroupDescriber.ALIVE)) {

            if (surfaceData.getType().equals(Surface.DEAD_TYPE)) {
                Telegraph telegraph = gameResources.getMessageManger().getTelegraph(userData.toString());
                if (telegraph != null) {
                    gameResources.getMessageManger().dispatchMessage(surface, telegraph, TelegramDescriber.DEAD, 100);
                }
            } else if (surfaceData.getType().equals(Surface.PLATFORM_TYPE)) {

                Vector2 vector = fixtureObject.getBody().getLinearVelocity();
                if (vector.y == 0) {
                }
            }
        }
    }

}
