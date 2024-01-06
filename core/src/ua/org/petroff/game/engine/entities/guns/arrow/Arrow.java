package ua.org.petroff.game.engine.entities.guns.arrow;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import java.util.ArrayList;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class Arrow implements EntityInterface {

    public static final String DESCRIPTOR = "Arrow";
    public static final int MAX = 50;

    public Body body;
    private final View view;
    private final GameResources gameResources;
    private final Telegraph telegraph;
    private final ArrayList<Body> arrows = new ArrayList<>();

    public Arrow(Assets asset, GameResources gameResources, GraphicResources graphicResources)  {
        view = new View(asset, graphicResources, this);
        this.gameResources = gameResources;
        telegraph = new Telegraph(this, gameResources);// is need this?

    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    @Override
    public void update() {
    }

    public void shoot(float x, float y, float angular, float forceX, float forceY) {
        body = initArrow(x, y);
        body.setAngularVelocity(angular);
        body.applyForceToCenter(forceX, forceY, true);
        if (forceX < 0) {
            body.setUserData(WorldInterface.Vector.LEFT);
        } else {
            body.setUserData(WorldInterface.Vector.RIGHT);
        }

        arrows.add(body);
        if (arrows.size() > MAX) {
            Body bodyRemove = arrows.get(0);
            arrows.remove(0);
            gameResources.getWorld().destroyBody(bodyRemove);
        }
    }

    public ArrayList<Body> getArrows() {
        return arrows;
    }

    private Body initArrow(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = false;
        Body bodyArrow = gameResources.getWorld().createBody(bodyDef);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(0.46f, 0.05f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.density = 1;
        bodyArrow.createFixture(fixtureDef);
        poly.dispose();
        bodyArrow.setTransform(x, y, 0);
        return bodyArrow;
    }

}
