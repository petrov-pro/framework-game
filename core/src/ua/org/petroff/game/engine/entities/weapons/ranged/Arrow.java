package ua.org.petroff.game.engine.entities.weapons.ranged;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Pool;
import ua.org.petroff.game.engine.interfaces.GroundedInterface;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class Arrow implements WeaponInterface, GroundedInterface, com.badlogic.gdx.ai.msg.Telegraph, Pool.Poolable {

    private GameResources gameResources;

    private Body bodyArrow;

    private final float velocityThreshold = 0f;
    private final float bodyWidth = 0.46f;
    private final float bodyHeight = 0.05f;
    private WorldInterface.Vector vector;
    private int damage = 10;
    private boolean grounded = false;
    private Vector2 vectorHit;

    public Arrow() {
    }

    public Arrow(GameResources gameResources, Vector2 start, int damage, float angular, float forceX, float forceY) {
        this.gameResources = gameResources;
        this.damage = damage;
        initArrow(start, angular, forceX, forceY);
    }

    public void init(GameResources gameResources, Vector2 start, int damage, float angular, float forceX, float forceY) {
        this.gameResources = gameResources;
        this.damage = damage;
        if (bodyArrow == null) {
            initArrow(start, angular, forceX, forceY);

            return;
        }
        bodyArrow.setTransform(start, 0);
        bodyArrow.setActive(true);
        bodyArrow.setAngularVelocity(angular);
        bodyArrow.applyForceToCenter(forceX, forceY, true);
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

    @Override
    public WeaponInterface setDirectionHit(Vector2 vectorHit) {
        this.vectorHit = vectorHit;

        return this;
    }

    @Override
    public Vector2 getDirectionHit() {
        return vectorHit;
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

    @Override
    public void reset() {
        bodyArrow.setTransform(new Vector2(-50, -50), 0);
        bodyArrow.setLinearVelocity(0, 0);
        bodyArrow.setActive(false);
        damage = 10;
        grounded = false;
        vectorHit = null;
    }

    private void initArrow(Vector2 start, float angular, float forceX, float forceY) {
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
        bodyArrow.setTransform(start, 0);
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
