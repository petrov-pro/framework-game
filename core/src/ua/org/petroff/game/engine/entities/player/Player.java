package ua.org.petroff.game.engine.entities.player;

import ua.org.petroff.game.engine.entities.Interfaces.ActionInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.entities.BodyDescriber;
import ua.org.petroff.game.engine.entities.GroupDescriber;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.EntityNotifierInterface;
import ua.org.petroff.game.engine.entities.Interfaces.MoveEntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.TelegramDescriber;
import ua.org.petroff.game.engine.entities.guns.arrow.Telegram;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.MapResolver;

public class Player implements EntityInterface, MoveEntityInterface, EntityNotifierInterface, ActionInterface {

    public static final String OBJECT_NAME = "start player";
    public static final String DESCRIPTOR = "Player";
    public static final float FIRE_SPEED = 0.035f;
    public static final float FIRE_FORCE = 50f;
    public Body arrow;

    public enum PlayerSize {
        NORMAL, GROWN
    };

    private final GameResources gameResources;

    private Body body;
    private final int currentLive = 100;

    private static final float MAXMOVEVELOCITY = 5f;
    private static final float MAXJUMPVELOCITY = 5f;
    private static final float JUMPVELOCITY = 10f;
    private static final float MOVEVELOCITY = 0.8f;
    private final View view;
    private final Vector3 cameraNewPosition = new Vector3();

    private boolean isMove = false;
    private boolean isJump = false;
    private boolean isGround = true;
    private boolean isDie = false;
    private boolean isHit = false;

    private WorldInterface.Vector vector = WorldInterface.Vector.STAY;
    private PlayerSize playerSize = PlayerSize.NORMAL;
    private ActionInterface.Type action = ActionInterface.Type.MOVE;

    private final float bodyWidth = 1f;
    private final float bodyHeight = 1.45f;
    private Vector2 centerFoot;
    private Vector2 centerFootSize;

    public Player(Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        this.gameResources = gameResources;

        view = new View(asset, graphicResources, this);
        createBody();
        setStartPlayerPostion(asset);
        new Telegraph(this, gameResources);
        gameResources.getMessageManger().dispatchMessage(TelegramDescriber.PLAYER_STATUS, new PlayerTelegram(currentLive));
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    public PlayerSize getPlayerSize() {
        return playerSize;
    }

    @Override
    public boolean isGround() {
        return isGround;
    }

    @Override
    public boolean isDie() {
        return isDie;
    }

    @Override
    public boolean isHit() {
        return isHit;
    }

    @Override
    public boolean isMove() {
        return isMove;
    }

    public WorldInterface.Vector getVector() {
        return vector;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Vector3 getCameraNewPosition() {
        return cameraNewPosition;
    }

    public int getCurrentLive() {
        return currentLive;
    }

    private void setStartPlayerPostion(Assets asset) {
        MapObject playerObject = ua.org.petroff.game.engine.util.MapResolver.findObject(asset.getMap(),
                OBJECT_NAME);
        int x = playerObject.getProperties().get("x", Float.class).intValue();
        int y = playerObject.getProperties().get("y", Float.class).intValue();
        Vector2 position = new Vector2(MapResolver.coordinateToWorld(x), MapResolver.coordinateToWorld(y));
        body.setTransform(position, 0);
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        body = gameResources.getWorld().createBody(bodyDef);

        Filter filterLight = new Filter();
        filterLight.categoryBits = (short) 1;
        filterLight.groupIndex = (short) 0;
        filterLight.maskBits = (short) 0;

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(bodyWidth / 2, bodyHeight / 2);
        Fixture bodyPlayer = body.createFixture(poly, 1);
        bodyPlayer.setUserData(new BodyDescriber(DESCRIPTOR, BodyDescriber.BODY, GroupDescriber.ALIVE));

        centerFoot = bodyPlayer.getBody().getWorldCenter();
        centerFootSize = centerFoot.cpy();
        poly.setAsBox((bodyWidth / 2f) - 0.05f, 0.05f, centerFoot.cpy().sub(0, bodyHeight / 2), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.density = 1;
        fixtureDef.isSensor = true;
        Fixture footSensorFixture = body.createFixture(fixtureDef);
        footSensorFixture.setFilterData(filterLight);
        footSensorFixture.setUserData(new BodyDescriber(DESCRIPTOR, BodyDescriber.BODY_FOOT, GroupDescriber.ALIVE, this));
        poly.dispose();

        gameResources.getWorldContactListener().addListener(new Listener(this));
    }

    @Override
    public void left(boolean active) {
        isMove = active;
        action = ActionInterface.Type.MOVE;
        if (!active) {
            vector = WorldInterface.Vector.STAY;
            return;
        }

        vector = WorldInterface.Vector.LEFT;
    }

    @Override
    public void right(boolean active) {
        isMove = active;
        action = ActionInterface.Type.MOVE;
        if (!active) {
            vector = WorldInterface.Vector.STAY;
            return;
        }
        vector = WorldInterface.Vector.RIGHT;

    }

    @Override
    public void jump(boolean active) {
        action = ActionInterface.Type.JUMP;
        isJump = active;
    }

    @Override
    public void hit(boolean active) {
        isHit = active;

        if (!active) {
            view.resetState(ActionInterface.Type.FIRE);
        }
        action = ActionInterface.Type.FIRE;
    }

    @Override
    public void ability(boolean active) {
        if (active) {
            playerResize();
        }
    }

    @Override
    public void update() {

        if (isDie) {
            return;
        }

        if (isHit && view.isFinishAction(ActionInterface.Type.FIRE)) {
            float x = body.getPosition().x;
            float y = body.getPosition().y;
            float forceX;
            if (vector.equals(WorldInterface.Vector.RIGHT)) {
                x += 0.5f;
                forceX = +FIRE_FORCE;
            } else {
                x -= 0.5f;
                forceX = -FIRE_FORCE;
            }
            isHit = false;
            gameResources.getMessageManger().dispatchMessage(TelegramDescriber.FIRE, new Telegram(x, y, forceX));

            return;
        } else if (isHit) {
            return;
        }

        //if action jump and player stay on the ground
        if (isJump && isGround && body.getLinearVelocity().y < MAXJUMPVELOCITY) {
            body.applyLinearImpulse(0, JUMPVELOCITY, body.getPosition().x, body.getPosition().y, true);
            if (body.getLinearVelocity().y > 1.60f) {
                isGround = false;
            }
        }

        if (isMove) {
            Gdx.app.log("info", "move");
            if (vector.equals(WorldInterface.Vector.LEFT) && body.getLinearVelocity().x > -MAXMOVEVELOCITY) {
                body.applyLinearImpulse(-MOVEVELOCITY, 0, body.getPosition().x, body.getPosition().y, true);
            } else if (vector.equals(WorldInterface.Vector.RIGHT) && body.getLinearVelocity().x < MAXMOVEVELOCITY) {
                body.applyLinearImpulse(MOVEVELOCITY, 0, body.getPosition().x, body.getPosition().y, true);
            }

        }

        calculateCameraPositionForPlayer();
    }

    public void grounded() {
        isGround = true;
        view.resetState(ActionInterface.Type.JUMP);
    }

    @Override
    public void died() {
        gameResources.getMessageManger().dispatchMessage(TelegramDescriber.PLAYER_DEAD);
        isDie = true;
        action = ActionInterface.Type.DIED;
        vector = WorldInterface.Vector.STAY;
    }

    public Body getBody() {
        return body;
    }

    public ActionInterface.Type getAction() {
        return action;
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

    private void playerResize() {
        if (playerSize.equals(PlayerSize.NORMAL)) {
            playerGrow();
        } else {
            playerNormal();
        }
    }

    private void playerGrow() {
        body.setActive(true);
        playerSize = PlayerSize.GROWN;

        float bodyHeightGrow = (bodyHeight / 2) + 0.2f;
        float bodyWidthGrow = (bodyWidth / 2) + 0.15f;
        ((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(bodyWidthGrow, bodyHeightGrow);

        ((PolygonShape) body.getFixtureList().get(1).getShape()).setAsBox(bodyWidthGrow - 0.05f, 0.05f, centerFootSize.cpy()
                .sub(0, bodyHeightGrow), 0);
        body.resetMassData();
        Vector2 newPosition = body.getTransform().getPosition();
        body.setTransform(newPosition.add(0, 0.4f), 0);
    }

    private void playerNormal() {
        body.setActive(true);
        playerSize = PlayerSize.NORMAL;

        ((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(bodyWidth / 2, bodyHeight / 2);
        ((PolygonShape) body.getFixtureList().get(1).getShape()).setAsBox((bodyWidth / 2f) - 0.05f, 0.05f, centerFootSize.cpy().sub(0, bodyHeight / 2), 0);
        body.resetMassData();
        Vector2 newPosition = body.getTransform().getPosition();
        body.setTransform(newPosition.add(0, 0.4f), 0);
    }

}
