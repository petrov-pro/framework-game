package ua.org.petroff.game.engine.entities.cloud;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.util.RandomGenerate;

public class Cloud {

    private final GameResources gameResources;

    private Body bodyCloud;

    private final float bodyWidth = 2f;
    private final float bodyHeight = 0.05f;
    private int type = 0;

    public Cloud(GameResources gameResources, float x, float y, float forceX) {
        this.gameResources = gameResources;
        type = RandomGenerate.generate(0, 2);
        init(x, y, forceX);
    }

    public Cloud(GameResources gameResources, float x, float y, float forceX, int type) {
        this.gameResources = gameResources;
        this.type = type;
        init(x, y, forceX);
    }

    public Body getBody() {
        return bodyCloud;
    }

    public void destroy() {
        gameResources.getWorld().destroyBody(bodyCloud);
    }

    public int getType() {
        return type;
    }

    private void init(float x, float y, float forceX) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.gravityScale = 0;

        bodyCloud = gameResources.getWorld().createBody(bodyDef);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(bodyWidth, bodyHeight);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.density = 0.1f;
        fixtureDef.isSensor = true;
        bodyCloud.createFixture(fixtureDef);
        bodyCloud.setTransform(x, y, 0);
        bodyCloud.setUserData(this);
        bodyCloud.applyLinearImpulse(forceX, 0f, bodyCloud.getPosition().x, bodyCloud.getPosition().y, true);
        poly.dispose();
    }

}
