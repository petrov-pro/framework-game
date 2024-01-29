package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.MoveEntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.characters.base.creature.CreatureInterface;
import ua.org.petroff.game.engine.entities.guns.arrow.Telegram;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.characters.base.creature.Creature;
import ua.org.petroff.game.engine.entities.characters.enemies.Enemy;
import ua.org.petroff.game.engine.entities.guns.GunInterface;

public class Player extends Creature implements EntityInterface, MoveEntityInterface, StateInterface, CreatureInterface {

    public static final String DESCRIPTOR = "player";
    public static final float FIRE_SPEED = 0.035f;
    public static final float FIRE_FORCE = 7f;

    public enum PlayerSize {
        NORMAL, GROWN
    };

    private static final float MAXMOVEVELOCITY = 5f;
    private static final float MAXJUMPVELOCITY = 5f;
    private static final float JUMPVELOCITY = 10f;
    private static final float MOVEVELOCITY = 0.8f;
    private final Vector3 cameraNewPosition = new Vector3();
    private Vector2 centerFootSize;

    private PlayerSize playerSize = PlayerSize.NORMAL;
    private StateInterface.State action = StateInterface.State.MOVE;

    public Player(int x, int y, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        super(x, y, DESCRIPTOR, asset, gameResources, graphicResources);
        this.gameResources = gameResources;
        view = new View(asset, graphicResources, (Player) this);
        new TelegramProvider(this, gameResources);
        sendPlayerStatus();

    }

    public static Player getInstance(Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        MapObject playerObject = ua.org.petroff.game.engine.util.MapResolver.findObject(asset.getMap(),
                DESCRIPTOR);
        int x = playerObject.getProperties().get("x", Float.class).intValue();
        int y = playerObject.getProperties().get("y", Float.class).intValue();
        return new Player(x, y, asset, gameResources, graphicResources);
    }

    @Override
    public boolean handleMessage(com.badlogic.gdx.ai.msg.Telegram msg) {

        switch (StateInterface.State.getStateBy(msg.message)) {

            case HIT:
                decreaseLive(((GunInterface) msg.extraInfo).getDamage(), ((GunInterface) msg.extraInfo).getPlaceHit());
                action = State.HIT;
                vector = WorldInterface.Vector.STAY;
                break;

            case DIED:
                died();
                break;

            case GROUND:
                ground((boolean) msg.extraInfo);
                break;

            case CREATURE_COLLISION:
                ground(true);
                if (msg.extraInfo instanceof StateInterface && ((StateInterface) msg.extraInfo).getState() == State.DIED) {
                    action = State.MOVE;

                    return true;
                }

                if (msg.extraInfo instanceof Enemy) {
                    decreaseLive(5, body.getPosition());
                    action = State.HIT;
                    vector = WorldInterface.Vector.STAY;
                }

                break;
        }

        return true;
    }

    @Override
    public void decreaseLive(int amount, Vector2 placeHit) {
        super.decreaseLive(amount, placeHit);
        sendPlayerStatus();
    }

    public PlayerSize getPlayerSize() {
        return playerSize;
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
    public void fire(boolean active) {
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

        if (action == State.HIT && !view.isFinishAction(action)) {
            vector = WorldInterface.Vector.STAY;

            return;
        } else if (action == State.HIT && view.isFinishAction(action)) {
            view.resetState(action);
            action = State.MOVE;
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

//      handler if body have vertical velocity
        if (action != StateInterface.State.JUMP && !onGround && body.getLinearVelocity().y <= -3f) {
            action = StateInterface.State.JUMP;
        }

        if (action == StateInterface.State.JUMP && onGround && body.getLinearVelocity().y < MAXJUMPVELOCITY) {
            body.applyLinearImpulse(0, JUMPVELOCITY, body.getPosition().x, body.getPosition().y, true);
            //for force
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
    public void ground(boolean isGround) {
        super.ground(isGround);
        if (isGround && action == State.JUMP) {
            action = State.MOVE;
        }
    }

    @Override
    public void died() {
        super.died();

        gameResources.getMessageManger().dispatchMessage(StateInterface.State.PLAYER_DEAD.telegramNumber);
        action = StateInterface.State.DIED;
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

    @Override
    protected void createBody(GameResources gameResources) {
        super.createBody(gameResources);
        centerFootSize = centerFoot.cpy();
    }

    private void sendPlayerStatus() {
        gameResources.getMessageManger().dispatchMessage(StateInterface.State.PLAYER_STATUS.telegramNumber, new PlayerTelegram(currentLive));
    }

}
