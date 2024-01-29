package ua.org.petroff.game.engine.entities.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.entities.characters.base.creature.CreatureInterface;

public class GroundedFilter implements ContactFilter {

    private final Vector2 platfromPosition = new Vector2();
    private final Vector2 platfromPosition1 = new Vector2();
    private final Vector2 entityPosition = new Vector2();

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

    private boolean isBodyDescriberSurface(Object userData) {
        return userData instanceof Surface;
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
