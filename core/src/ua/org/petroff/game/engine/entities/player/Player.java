package ua.org.petroff.game.engine.entities.player;

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
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.EntityNotifierInterface;
import ua.org.petroff.game.engine.entities.Interfaces.MoveEntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.characters.base.creature.CreatureInterface;
import ua.org.petroff.game.engine.entities.guns.arrow.Telegram;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.MapResolver;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.guns.GunInterface;

public class Player implements EntityInterface, MoveEntityInterface, EntityNotifierInterface, StateInterface, CreatureInterface {

    public static final String OBJECT_NAME = "start player";
    public static final String DESCRIPTOR = "player";
    public static final float FIRE_SPEED = 0.035f;
    public static final float FIRE_FORCE = 50f;
    public Body arrow;

    public enum PlayerSize {
        NORMAL, GROWN
    };

    private final GameResources gameResources;

    private Body body;
    private int currentLive = 100;

    private static final float MAXMOVEVELOCITY = 5f;
    private static final float MAXJUMPVELOCITY = 5f;
    private static final float JUMPVELOCITY = 10f;
    private static final float MOVEVELOCITY = 0.8f;
    private final View view;
    private final Vector3 cameraNewPosition = new Vector3();

    private boolean onGround = true;

    private WorldInterface.Vector vector = WorldInterface.Vector.STAY;
    private PlayerSize playerSize = PlayerSize.NORMAL;
    private StateInterface.State action = StateInterface.State.MOVE;

    private final float bodyWidth = 1f;
    private final float bodyHeight = 1.45f;
    private Vector2 centerFoot;
    private Vector2 centerFootSize;

    public Player(Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        this.gameResources = gameResources;

        view = new View(asset, graphicResources, this);
        createBody();
        setStartPlayerPostion(asset);
        new TelegramProvider(this, gameResources);
        sendPlayerStatus();

    }

    @Override
    public boolean handleMessage(com.badlogic.gdx.ai.msg.Telegram msg) {

        switch (StateInterface.State.getStateBy(msg.message)) {

            case HIT:
                decreaseLive(((GunInterface) msg.extraInfo).getDamage(), ((GunInterface) msg.extraInfo).getPlaceHit());
                break;

            case DIED:
                died();
                break;

            case GROUND:
                grounded();
                break;
        }

        return true;
    }

    public int getCurrentLive() {
        return currentLive;
    }

    @Override
    public void decreaseLive(int amount, Vector2 placeHit) {
        currentLive = currentLive - amount;
        sendPlayerStatus();
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    public PlayerSize getPlayerSize() {
        return playerSize;
    }

    @Override
    public boolean isGrounded() {
        return onGround;
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

    @Override
    public void left(boolean active) {
        if (!active) {
            vector = WorldInterface.Vector.STAY;

            return;
        }
        vector = WorldInterface.Vector.LEFT;
    }

    @Override
    public void right(boolean active) {
        if (!active) {
            vector = WorldInterface.Vector.STAY;

            return;
        }
        vector = WorldInterface.Vector.RIGHT;
    }

    @Override
    public void jump(boolean active) {
        if (active) {
            action = StateInterface.State.JUMP;
        }
    }

    @Override
    public void hit(boolean active) {
        if (active) {
            action = StateInterface.State.FIRE;
        } else {
            action = StateInterface.State.MOVE;
            view.resetState(StateInterface.State.FIRE);
        }
    }

    @Override
    public void ability(boolean active) {
        if (!active) {
            return;
        }

        if (playerSize.equals(PlayerSize.NORMAL)) {
            playerGrow();
        } else {
            playerNormal();
        }
    }

    @Override
    public void update() {
        if (action == State.DIED || currentLive <= 0) {
            died();

            return;
        }

        if (action == StateInterface.State.FIRE) {
            if (view.isFinishAction(StateInterface.State.FIRE)) {
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
                gameResources.getMessageManger().dispatchMessage(StateInterface.State.FIRE.telegramNumber, new Telegram(x, y, forceX));
                action = StateInterface.State.MOVE;
                view.resetState(StateInterface.State.FIRE);
            } else {
                return;
            }
        }

        //handler if body have vertical velocity
        if (Math.abs(body.getLinearVelocity().y) >= 3f) {
            onGround = false;
            action = StateInterface.State.JUMP;
        }

        if (action == StateInterface.State.JUMP && onGround && body.getLinearVelocity().y < MAXJUMPVELOCITY) {
            body.applyLinearImpulse(0, JUMPVELOCITY, body.getPosition().x, body.getPosition().y, true);
            onGround = false;
        }

        if (vector.equals(WorldInterface.Vector.LEFT) && body.getLinearVelocity().x > -MAXMOVEVELOCITY) {
            body.applyLinearImpulse(-MOVEVELOCITY, 0, body.getPosition().x, body.getPosition().y, true);
        } else if (vector.equals(WorldInterface.Vector.RIGHT) && body.getLinearVelocity().x < MAXMOVEVELOCITY) {
            body.applyLinearImpulse(MOVEVELOCITY, 0, body.getPosition().x, body.getPosition().y, true);
        }

        calculateCameraPositionForPlayer();
    }

    @Override
    public void grounded() {
        onGround = true;

        if (action == StateInterface.State.JUMP) {
            action = StateInterface.State.MOVE;
            view.resetState(StateInterface.State.JUMP);
        }
    }

    @Override
    public void died() {
        gameResources.getMessageManger().dispatchMessage(StateInterface.State.PLAYER_DEAD.telegramNumber);
        action = StateInterface.State.DIED;
        vector = WorldInterface.Vector.STAY;
        currentLive = 0;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public StateInterface.State getState() {
        return action;
    }

    private void calculateCameraPositionForPlayer() {
        //optimization
        cameraNewPosition.x = view.graphicResources.getCamera().position.x;
        cameraNewPosition.y = view.graphicResources.getCamera().position.y;

        cameraNewPosition.x += (getPosition().x - view.graphicResources.getCamera().position.x) * 0.9f * Gdx.graphics.getDeltaTime();
        cameraNewPosition.y += (getPosition().y - view.graphicResources.getCamera().position.y) * 0.9f * Gdx.graphics.getDeltaTime();
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
        body.setUserData(this);

        Filter filterLight = new Filter();
        filterLight.categoryBits = (short) 1;
        filterLight.groupIndex = (short) 0;
        filterLight.maskBits = (short) 0;

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(bodyWidth / 2, bodyHeight / 2);
        Fixture bodyPlayer = body.createFixture(poly, 1);
        bodyPlayer.setFilterData(filterLight);

        centerFoot = bodyPlayer.getBody().getWorldCenter();
        centerFootSize = centerFoot.cpy();
        poly.setAsBox((bodyWidth / 2f) - 0.05f, 0.05f, centerFoot.cpy().sub(0, bodyHeight / 2), 0);
        FixtureDef fixtureFootDef = new FixtureDef();
        fixtureFootDef.shape = poly;
        fixtureFootDef.density = 1;
        fixtureFootDef.isSensor = true;
        body.createFixture(fixtureFootDef);
        poly.dispose();

    }

    private void sendPlayerStatus() {
        gameResources.getMessageManger().dispatchMessage(StateInterface.State.PLAYER_STATUS.telegramNumber, new PlayerTelegram(currentLive));
    }

}
