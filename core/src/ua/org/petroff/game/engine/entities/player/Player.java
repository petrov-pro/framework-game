package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
import ua.org.petroff.game.engine.scenes.core.CameraBound;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.MapResolver;

public class Player implements EntityInterface, MoveEntityInterface {

    public static final String OBJECT_NAME = "start player";
    public static final String DESCRIPTOR = "Player";
    public static final String SENSOR = "foot";

    public enum Actions {
        MOVE, JUMP, USE, HIT
    };

    private final int zIndex = 2;
    private final Assets asset;
    private Body body;
    private Float currentVelocityX = 0f;
    private static final float VELOCITYX = 3f;
    private static final float JUMPVELOCITY = 800f;
    private GameResources gameResources;
    private final View view;

    public enum PlayerVector {
        LEFT, RIGHT, STAY
    };
    private boolean isMove = false;
    private boolean isJump = false;
    private boolean isGround = true;
    private boolean isAction = false;

    private PlayerVector vector;

    public Player(Assets asset) {
        view = new View(asset, this);
        this.asset = asset;
        vector = PlayerVector.STAY;
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    public boolean isGround() {
        return isGround;
    }

    public boolean isAction() {
        return isAction;
    }

    public PlayerVector getVector() {
        return vector;
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
        currentVelocityX = -Player.VELOCITYX;
        isMove = true;
        vector = PlayerVector.LEFT;
    }

    @Override
    public void right() {
        currentVelocityX = Player.VELOCITYX;
        isMove = true;
        vector = PlayerVector.RIGHT;
    }

    @Override
    public void stop(Player.Actions action) {

        switch (action) {
            case MOVE:
                vector = PlayerVector.STAY;
                currentVelocityX = 0f;
                isMove = false;
                break;

            case JUMP:
                isJump = false;
                break;
        }

    }

    @Override
    public void jump() {
        isJump = true;
    }

    @Override
    public void update() {

        if (isMove) {
            Vector2 velocity = body.getLinearVelocity().cpy();
            velocity.set(currentVelocityX, velocity.y);
            body.setLinearVelocity(velocity);
        }

        if (isJump && isGround) {
            view.isLoopAnimation = false;
            view.speedAnimation = 0.3f;
            body.applyForceToCenter(0, JUMPVELOCITY, true);
            isGround = false;
        }

        calculateCameraPositionForPlayer();
    }

    public void grounded() {
        isGround = true;
        view.setDefaultAnimationParams();
    }

    private void calculateCameraPositionForPlayer() {
        Vector3 cameraPosition = view.graphicResources.getCamera().position;
        Float deltaTime = Gdx.graphics.getDeltaTime();
        float lerp = 0.9f;
        float x = cameraPosition.x;
        float y = cameraPosition.y;

        x += (getPosition().x - cameraPosition.x) * lerp * deltaTime;
        y += (getPosition().y - cameraPosition.y) * lerp * deltaTime;

        ((CameraBound) view.graphicResources.getCamera()).positionSafe(x, y);

//        Gdx.app.log("Camera", "x: " + getPosition().x + " y: " + getPosition().y);
    }

}
