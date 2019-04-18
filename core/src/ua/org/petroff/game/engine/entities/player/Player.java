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

    public enum PlayerVector {
        LEFT, RIGHT, STAY
    };

    public enum PlayerSize {
        NORMAL, GROWN
    };

    private final int zIndex = 3;
    private final Assets asset;
    private Body body;
    private int currentLive;

    private static final float MAXMOVEVELOCITY = 5f;
    private static final float MAXJUMPVELOCITY = 5f;
    private static final float JUMPVELOCITY = 10f;
    private static final float MOVEVELOCITY = 0.8f;
    private GameResources gameResources;
    private final View view;
    private final ViewInterface graphic;
    private final Vector3 cameraNewPosition = new Vector3();
    private Telegraph telegraph;

    private boolean isMove;
    private boolean isJump;
    private boolean isGround;
    private boolean isDie;
    private boolean isAction;

    private PlayerVector vector;
    private PlayerSize playerSize;

    private float bodyWidth;
    private float bodyHeight;
    private Vector2 centerFoot;
    private Vector2 position2;

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

    public PlayerSize getPlayerSize() {
        return playerSize;
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
        playerSize = PlayerSize.NORMAL;
        vector = PlayerVector.STAY;

        this.gameResources = gameResources;
        telegraph = new Telegraph(this, gameResources);

        gameResources.getMessageManger().addTelegraph(DESCRIPTOR, telegraph);

        MapObject playerObject = ua.org.petroff.game.engine.util.MapResolver.findObject(asset.getMap(),
                OBJECT_NAME);
        createBody();
        setStartPlayerPostion(playerObject);
    }

    public int getCurrentLive() {
        return currentLive;
    }

    public void setStartPlayerPostion(MapObject playerObject) {
        int x = playerObject.getProperties().get("x", Float.class).intValue();
        int y = playerObject.getProperties().get("y", Float.class).intValue();
        Vector2 position = new Vector2(MapResolver.coordinateToWorld(x), MapResolver.coordinateToWorld(y));
        position2 = position.cpy();
        body.setTransform(position, 0);
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        body = gameResources.getWorld().createBody(bodyDef);

        PolygonShape poly = new PolygonShape();
        bodyWidth = (MapResolver.coordinateToWorld(32) / 2);
        bodyHeight = (MapResolver.coordinateToWorld(64) / 2);
        poly.setAsBox(bodyWidth, bodyHeight);
        Fixture bodyPlayer = body.createFixture(poly, 1);
        bodyPlayer.setUserData(new BodyDescriber(DESCRIPTOR, BodyDescriber.BODY, GroupDescriber.ALIVE));

        centerFoot = bodyPlayer.getBody().getWorldCenter();
        poly.setAsBox(bodyWidth / 1.3f, 0.05f, centerFoot.sub(0, 0.73f), 0);
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
        isMove = true;
        vector = PlayerVector.LEFT;
    }

    @Override
    public void right() {
        isMove = true;
        vector = PlayerVector.RIGHT;
    }

    @Override
    public void stop(Player.Actions action) {

        switch (action) {
            case MOVE:
                vector = PlayerVector.STAY;
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
    public void hit() {
        view.graphicResources.position = position2;
    }

    @Override
    public void update() {

        if (isDie) {
            return;
        }

        if (isJump && isGround && body.getLinearVelocity().y < MAXJUMPVELOCITY) {
            body.applyLinearImpulse(0, JUMPVELOCITY, body.getPosition().x, body.getPosition().y, true);
            isGround = false;
        }

        if (isMove) {
            if (vector.equals(Player.PlayerVector.LEFT) && body.getLinearVelocity().x > -MAXMOVEVELOCITY) {
                body.applyLinearImpulse(-MOVEVELOCITY, 0, body.getPosition().x, body.getPosition().y, true);
            } else if (vector.equals(Player.PlayerVector.RIGHT) && body.getLinearVelocity().x < MAXMOVEVELOCITY) {
                body.applyLinearImpulse(MOVEVELOCITY, 0, body.getPosition().x, body.getPosition().y, true);
            }

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

    private void playerGrow() {
        body.setActive(true);
        playerSize = PlayerSize.GROWN;
        Gdx.app.log("Mass after", " kg: " + body.getMass());
        ((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(bodyWidth * 1.3f, bodyHeight * 1.3f);

        ((PolygonShape) body.getFixtureList().get(1).getShape()).setAsBox(bodyWidth * 1.2f, 0.05f, centerFoot.cpy().sub(0, 0.23f), 0);
        body.getFixtureList().get(0).setDensity(0.9f);
        body.resetMassData();
        Gdx.app.log("Mass after", " kg: " + body.getMass());
        Vector2 newPosition = body.getTransform().getPosition();
        body.setTransform(newPosition.add(0, 0.2f), 0);
    }

}
