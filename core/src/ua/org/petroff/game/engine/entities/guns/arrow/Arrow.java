package ua.org.petroff.game.engine.entities.guns.arrow;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.entities.Interfaces.GroundedInterface;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.guns.GunInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class Arrow implements GunInterface, GroundedInterface, com.badlogic.gdx.ai.msg.Telegraph {

    private final GameResources gameResources;

    private Body bodyArrow;

    private final float velocityThreshold = 0f;
    private final float bodyWidth = 0.46f;
    private final float bodyHeight = 0.05f;
    private WorldInterface.Vector vector;
    private final int damage = 10;
    private boolean grounded = false;

    public Arrow(GameResources gameResources, float x, float y, float angular, float forceX, float forceY) {
        this.gameResources = gameResources;
        initArrow(x, y, angular, forceX, forceY);
    }

    @Override
    public void ground(boolean isGround) {
        this.grounded = isGround;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public Vector2 getPlaceHit() {
        return bodyArrow.getPosition();
    }

    public WorldInterface.Vector getVector() {
        return vector;
    }

    public Body getBody() {
        return bodyArrow;
    }

    public void handleActive() {
        if (bodyArrow.isActive() && grounded && bodyArrow.getLinearVelocity().len() <= velocityThreshold) {
            bodyArrow.setActive(false);
        }
    }

    public void destroy() {
        gameResources.getWorld().destroyBody(bodyArrow);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (StateInterface.State.GROUND.telegramNumber == msg.message) {
            ground((boolean) msg.extraInfo);
        }

        return true;
    }

    private void initArrow(float x, float y, float angular, float forceX, float forceY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = false;

        bodyArrow = gameResources.getWorld().createBody(bodyDef);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(bodyWidth, bodyHeight);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.density = 0.1f;
        bodyArrow.createFixture(fixtureDef);
        bodyArrow.setTransform(x, y, 0);
        bodyArrow.setUserData(this);

        Vector2 centerArrow = bodyArrow.getLocalCenter().cpy();
        if (forceX < 0) {
            centerArrow = centerArrow.sub(bodyWidth, 0);
            vector = WorldInterface.Vector.LEFT;
        } else {
            centerArrow = centerArrow.add(bodyWidth, 0);
            vector = WorldInterface.Vector.RIGHT;
        }

        poly.setAsBox(bodyWidth / 20, bodyHeight, centerArrow, 0);
        FixtureDef fixtureSensorDef = new FixtureDef();
        fixtureSensorDef.shape = poly;
        fixtureSensorDef.isSensor = true;
        Fixture bodyArrowSensor = bodyArrow.createFixture(fixtureSensorDef);
        bodyArrowSensor.setUserData(this);
        poly.dispose();

        bodyArrow.setAngularVelocity(angular);
        bodyArrow.applyForceToCenter(forceX, forceY, true);
    }

}
