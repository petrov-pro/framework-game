package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.entities.BodyDescriber;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.MoveEntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.MapResolver;

public class Player implements EntityInterface, MoveEntityInterface {

    public static final String OBJECT_NAME = "start player";
    public static final String DESCRIPTOR = "Player";
    public static final String SENSOR = "foot";
    public Actions state;
    public View.GraphicType graphicFrame;
    public boolean isLoopAnimation = true;

    private final int zIndex = 2;
    private final Assets asset;
    private Body body;
    private Float currentVelocityX = 0f;
    private Float currentVelocityY = 0f;
    private static final float VELOCITYX = 3f;
    private static final float VELOCITYY = 0f;
    private GameResources gameResources;

    public enum Actions {
        MOVE, JUMP, STAY
    };
    public View view;

    public Player(Assets asset) {
        view = new View(asset, this);
        state = Actions.STAY;
        graphicFrame = View.GraphicType.STAY;
        this.asset = asset;
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public void init(GameResources gameResources) {
        this.gameResources = gameResources;
        MapObject playerObject = ua.org.petroff.game.engine.util.MapResolver.findObject(asset.getMap(),
                OBJECT_NAME);
        createBody(playerObject);
        int x = playerObject.getProperties().get("x", Float.class).intValue();
        int y = playerObject.getProperties().get("y", Float.class).intValue();
        Vector2 position = new Vector2(MapResolver.coordinateToWorld(x), MapResolver.coordinateToWorld(y));
        body.setTransform(position, 0);

    }

    private void createBody(MapObject playerObject) {
        float width = MapResolver.coordinateToWorld(playerObject.getProperties().get("width", Float.class).intValue());
        float height = MapResolver.coordinateToWorld(playerObject.getProperties().get("height", Float.class).intValue());
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        body = gameResources.getWorld().createBody(bodyDef);
        PolygonShape poly = new PolygonShape();
        Vector2 center = new Vector2(width / 2, height / 2.2f);
        poly.setAsBox(width / 4.5f, height / 2.5f, center, 0);
        body.createFixture(poly, 1);

        center.sub(0, 0.8f);
        poly.setAsBox(width / 6f, 0.05f, center, 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.density = 1;
        fixtureDef.isSensor = true;
        Fixture footSensorFixture = body.createFixture(fixtureDef);
        footSensorFixture.setUserData(new BodyDescriber(DESCRIPTOR, SENSOR));
        poly.dispose();

        gameResources.getWorldContactListener().addListener(new Listener(this));
    }

    @Override
    public void left() {
        state = Actions.MOVE;
        graphicFrame = View.GraphicType.MOVELEFT;
        currentVelocityX = -Player.VELOCITYX;
    }

    @Override
    public void right() {
        Gdx.app.log("Touch", "Right ");
        state = Actions.MOVE;
        graphicFrame = View.GraphicType.MOVERIGHT;
        currentVelocityX = Player.VELOCITYX;
    }

    @Override
    public void stop() {
        if (state == Actions.MOVE) {
            state = Actions.STAY;
            graphicFrame = View.GraphicType.STAY;
            currentVelocityX = 0f;
        }
    }

    @Override
    public void jump() {
        if (state != Actions.JUMP) {
            state = Actions.JUMP;
            if (body.getLinearVelocity().x > 0) {
                graphicFrame = View.GraphicType.JUMPRIGHT;
            } else if (body.getLinearVelocity().x < 0) {
                graphicFrame = View.GraphicType.JUMPLEFT;
            } else {
                graphicFrame = View.GraphicType.STAYJUMP;
            }

            body.applyForceToCenter(0, 1080f, true);
        }
    }

    @Override
    public void update() {

        if (state == Actions.MOVE || state == Actions.JUMP) {
            Vector2 velocity = body.getLinearVelocity().cpy();
            velocity.set(currentVelocityX, velocity.y);
            body.setLinearVelocity(velocity);
        }
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

}
