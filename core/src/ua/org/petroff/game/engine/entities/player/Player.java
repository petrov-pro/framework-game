package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.entities.BodyDescriber;
import ua.org.petroff.game.engine.entities.GroupDescriber;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.MoveEntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.MapResolver;

public class Player implements EntityInterface, MoveEntityInterface {

    public static final String OBJECT_NAME = "start player";
    public static final String DESCRIPTOR = "Player";

    public enum Actions {
        MOVE, JUMP, USE, HIT
    };

    private final int zIndex = 3;
    private final Assets asset;
    private Body body;
    private Float currentVelocityX;
    private int currentLive;
    private static final float VELOCITYX = 3f;
    private static final float JUMPVELOCITY = 800f;
    private GameResources gameResources;
    private final View view;
    private final ViewInterface graphic;
    private final Vector3 cameraNewPosition = new Vector3();
    private Telegraph telegraph;

    public enum PlayerVector {
        LEFT, RIGHT, STAY
    };
    private boolean isMove;
    private boolean isJump;
    private boolean isGround;
    private boolean isDie;
    private boolean isAction;

    private PlayerVector vector;

    public Player(Assets asset) {
        view = new View(this);
        graphic = new Graphic(asset, view);
        view.setGraphic((Graphic) graphic);
        this.asset = asset;
    }

    @Override
    public ViewInterface getView() {
        return graphic;
    }

    @Override
    public EntityInterface prepareModel() {
        return this;
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

    public boolean isDie() {
        return isDie;
    }

    public PlayerVector getVector() {
        return vector;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Vector3 getCameraNewPosition() {
        return cameraNewPosition;
    }

    @Override
    public void init(GameResources gameResources) {
        currentLive = 100;
        isMove = false;
        isJump = false;
        isGround = true;
        isDie = false;
        isAction = false;
        currentVelocityX = 0f;

        this.gameResources = gameResources;
        telegraph = new Telegraph(this, gameResources);
        gameResources.getMessageManger().addTelegraph(DESCRIPTOR, telegraph);
        vector = PlayerVector.STAY;
        MapObject playerObject = ua.org.petroff.game.engine.util.MapResolver.findObject(asset.getMap(),
                OBJECT_NAME);
        createBody(playerObject);
        setStartPlayerPostion(playerObject);
    }

    public int getCurrentLive() {
        return currentLive;
    }

    public void setStartPlayerPostion(MapObject playerObject) {
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
        Fixture bodyPlayer = body.createFixture(poly, 1);
        bodyPlayer.setUserData(new BodyDescriber(DESCRIPTOR, BodyDescriber.BODY, GroupDescriber.ALIVE));

        center.sub(0, 0.8f);
        poly.setAsBox(width / 6f, 0.05f, center, 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.density = 1;
        fixtureDef.isSensor = true;
        Fixture footSensorFixture = body.createFixture(fixtureDef);
        footSensorFixture.setUserData(new BodyDescriber(DESCRIPTOR, BodyDescriber.BODY_FOOT, GroupDescriber.ALIVE));
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

        if (isDie) {
            return;
        }

        if (isMove) {
            Vector2 velocity = body.getLinearVelocity().cpy();
            velocity.set(currentVelocityX, velocity.y);
            body.setLinearVelocity(velocity);
        }

        if (isJump && isGround) {

            body.applyForceToCenter(0, JUMPVELOCITY, true);
            isGround = false;
        }

        calculateCameraPositionForPlayer();
    }

    public void grounded() {
        isGround = true;
        view.changeState();
    }

    public void died() {
        isDie = true;
        view.changeState();
    }

    private void calculateCameraPositionForPlayer() {
        Vector3 cameraPosition = view.graphicResources.getCamera().position;
        Float deltaTime = Gdx.graphics.getDeltaTime();
        float lerp = 0.9f;
        cameraNewPosition.x = cameraPosition.x;
        cameraNewPosition.y = cameraPosition.y;

        cameraNewPosition.x += (getPosition().x - cameraPosition.x) * lerp * deltaTime;
        cameraNewPosition.y += (getPosition().y - cameraPosition.y) * lerp * deltaTime;

    }

}
