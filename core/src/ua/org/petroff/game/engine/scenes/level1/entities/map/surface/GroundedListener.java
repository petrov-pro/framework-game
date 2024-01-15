package ua.org.petroff.game.engine.scenes.level1.entities.map.surface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.entities.Interfaces.GroundedInterface;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.Listener;
import ua.org.petroff.game.engine.entities.characters.base.creature.CreatureInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class GroundedListener extends Listener implements ContactFilter {

    private final Vector2 platfromPosition = new Vector2();
    private final Vector2 platfromPosition1 = new Vector2();
    private final Vector2 entityPosition = new Vector2();

    public GroundedListener(GameResources gameResources) {
        super(gameResources);
    }

    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        Object userDataA = fixtureA.getBody().getUserData();
        Object userDataB = fixtureB.getBody().getUserData();
        if (isBodyDescriberSurface(userDataA)) {
            return shouldContact(fixtureA, fixtureB);
        }

        if (isBodyDescriberSurface(userDataB)) {
            return shouldContact(fixtureB, fixtureA);
        }
        return true;
    }

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        if (isBodyDescriberSurface(userDataA)) {
            handlerContact((String) contact.getFixtureA().getUserData(), userDataB, contact.getFixtureB());

            return;
        }

        if (isBodyDescriberSurface(userDataB)) {
            handlerContact((String) contact.getFixtureB().getUserData(), userDataA, contact.getFixtureA());
        }
    }

    private boolean isBodyDescriberSurface(Object userData) {
        return userData instanceof Surface;
    }

    private void handlerContact(String surfaceType, Object entityData, Fixture creature) {

        if (entityData instanceof CreatureInterface) {
            if (surfaceType.equals(Surface.DEAD_TYPE)) {
                gameResources.getMessageManger().dispatchMessage(
                        this,
                        (Telegraph) entityData,
                        StateInterface.State.DIED.telegramNumber,
                        false
                );
            }
        }

        if (entityData instanceof GroundedInterface
                && creature.isSensor()) {
            sentGroundTelegram(entityData, true);
        }

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

    private boolean shouldContact(Fixture fixtureSurface, Fixture fixtureEntity) {
        if (fixtureSurface.getUserData().equals(Surface.PLATFORM_TYPE)
                && fixtureEntity.getBody().getUserData() instanceof CreatureInterface) {

            ChainShape chain = (ChainShape) fixtureSurface.getShape();
            chain.getVertex(0, platfromPosition);
            chain.getVertex(1, platfromPosition1);
            if (!(fixtureEntity.getShape() instanceof PolygonShape)) {
                throw new Error("Unsuported shape");
            }
            PolygonShape entityShape = (PolygonShape) fixtureEntity.getShape();
            entityShape.getVertex(0, entityPosition);
            Vector2 entityBottomPosition = fixtureEntity.getBody().getWorldPoint(entityPosition);
            Vector2 entityCenter = fixtureEntity.getBody().getWorldCenter();

            if (entityBottomPosition.y > platfromPosition.y && (platfromPosition.x <= entityCenter.x
                    && entityCenter.x <= platfromPosition1.x)) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

}
