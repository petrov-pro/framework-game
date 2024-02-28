package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import ua.org.petroff.game.engine.interfaces.EntityInterface;
import ua.org.petroff.game.engine.interfaces.MoveEntityInterface;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.characters.creature.CreatureInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.characters.creature.Creature;
import ua.org.petroff.game.engine.characters.enemies.Enemy;
import ua.org.petroff.game.engine.interfaces.SkinInterface;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class Player extends Creature implements EntityInterface, MoveEntityInterface, StateInterface, CreatureInterface {

    public static final String DESCRIPTOR = "player";
    public static final float FIRE_ARROW_SPEED = 0.0035f;
    public static final float FIRE_BARE_SPEED = 0.005f;
    public static final float FIRE_ARROW_FORCE = 7f;
    public static final int FIRE_DAMAGE = 10;

    public enum PlayerSize {
        NORMAL, GROWN
    };

    private static final float MAXMOVEVELOCITY = 5f;
    private static final float MAXJUMPVELOCITY = 5f;
    private static final float JUMPVELOCITY = 10f;
    private static final float MOVEVELOCITY = 0.8f;
    private final Vector3 cameraNewPosition = new Vector3();

    private PlayerSize playerSize = PlayerSize.NORMAL;
    private StateInterface.State action = StateInterface.State.MOVE;

    private ArrayList<WeaponInterface.Type> slotWeapons = new ArrayList<>(Collections.unmodifiableList(Arrays.asList(WeaponInterface.Type.BARE, WeaponInterface.Type.BOW)));
    private WeaponInterface.Type weapon = WeaponInterface.Type.BARE;

    public Player(int x, int y, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        super(x, y, DESCRIPTOR, asset, gameResources, graphicResources);
        this.gameResources = gameResources;
        view = new View(asset, graphicResources, (Player) this);
        new TelegramProvider(this, gameResources);
        sendPlayerStatus();
        skin = SkinInterface.Type.DEFAULT;
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
                decreaseLive(((WeaponInterface) msg.extraInfo).getDamage(),
                        ((WeaponInterface) msg.extraInfo).getPlaceHit(),
                        ((WeaponInterface) msg.extraInfo).getDirectionHit()
                );
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
                    sendPlayerStatus();
                    action = State.HIT;
                    vector = WorldInterface.Vector.STAY;
                }

                break;
        }

        return true;
    }

    @Override
    public void decreaseLive(int amount, Vector2 placeHit, Vector2 directionHit) {
        super.decreaseLive(amount, placeHit, directionHit);
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
        if (!active && vector == WorldInterface.Vector.LEFT) {
            vector = WorldInterface.Vector.STAY;
        } else if (active) {
            vector = WorldInterface.Vector.LEFT;
        }
    }

    @Override
    public void right(boolean active) {
        if (!active && vector == WorldInterface.Vector.RIGHT) {
            vector = WorldInterface.Vector.STAY;
        } else if (active) {
            vector = WorldInterface.Vector.RIGHT;
        }
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
    public void slot(int number) {
        if (slotWeapons.size() > number) {
            weapon = slotWeapons.get(number);
        }
    }

    @Override
    public void update() {
        if (action == State.DIED || live <= 0) {
            died();

            return;
        }

        if (action == State.HIT) {
            if (!view.isFinishAction(action)) {
                vector = WorldInterface.Vector.STAY;

                return;
            }

            view.resetState(action);
            action = State.MOVE;
        }

        if (action == StateInterface.State.FIRE) {
            if (!view.isFinishAction(StateInterface.State.FIRE)) {
                return;
            }

            sendFire();
            action = StateInterface.State.MOVE;
            view.resetState(StateInterface.State.FIRE);
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

    public WeaponInterface.Type getWeapon() {
        return weapon;
    }

    private void sendFire() {
        float x = body.getPosition().x;
        float y = body.getPosition().y;
        float forceX;
        float positionHitX;
        if (vector.equals(WorldInterface.Vector.RIGHT)) {
            x += 0.5f;
            forceX = +FIRE_ARROW_FORCE;
            positionHitX = -(bodyWidth - 0.35f);
        } else {
            x -= 0.5f;
            forceX = -FIRE_ARROW_FORCE;
            positionHitX = (bodyWidth - 0.35f);
        }

        switch (weapon) {
            case BARE:
                gameResources.getMessageManger().dispatchMessage(
                        StateInterface.State.FIRE.telegramNumber,
                        new ua.org.petroff.game.engine.entities.weapons.hand.Telegram(WeaponInterface.Type.BARE, body, FIRE_DAMAGE, positionHitX, -0.2f, 0.08f, 0.1f)
                );
                break;
            case BOW:
                gameResources.getMessageManger().dispatchMessage(StateInterface.State.FIRE.telegramNumber, new ua.org.petroff.game.engine.entities.weapons.ranged.Telegram(WeaponInterface.Type.BOW, x, y, forceX));
                break;
            case SWORD:
                System.out.println("Wednesday");
                break;

        }
    }

    private void calculateCameraPositionForPlayer() {
        //optimization
        cameraNewPosition.x = view.graphicResources.getCamera().position.x;
        cameraNewPosition.y = view.graphicResources.getCamera().position.y;

        cameraNewPosition.x += (getPosition().x - view.graphicResources.getCamera().position.x) * 0.9f * Gdx.graphics.getDeltaTime();
        cameraNewPosition.y += (getPosition().y - view.graphicResources.getCamera().position.y + 3) * 0.9f * Gdx.graphics.getDeltaTime();
    }

    private void playerGrow() {
        body.setActive(true);
        playerSize = PlayerSize.GROWN;

        float bodyHeightGrow = (bodyHeight / 2) + 0.2f;
        float bodyWidthGrow = (bodyWidth / 2) + 0.15f;
        ((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(bodyWidthGrow, bodyHeightGrow);

        ((PolygonShape) body.getFixtureList().get(1).getShape()).setAsBox(bodyWidthGrow - 0.05f, 0.05f, body.getLocalCenter().cpy()
                .sub(0, bodyHeightGrow), 0);
        body.resetMassData();
        Vector2 newPosition = body.getTransform().getPosition();
        body.setTransform(newPosition.add(0, 0.4f), 0);
    }

    private void playerNormal() {
        body.setActive(true);
        playerSize = PlayerSize.NORMAL;

        ((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(bodyWidth / 2, bodyHeight / 2);
        ((PolygonShape) body.getFixtureList().get(1).getShape()).setAsBox((bodyWidth / 2f) - 0.05f, 0.05f, body.getLocalCenter().cpy().sub(0, bodyHeight / 2), 0);
        body.resetMassData();
        Vector2 newPosition = body.getTransform().getPosition();
        body.setTransform(newPosition.add(0, 0.4f), 0);
    }

    @Override
    protected void createBody(GameResources gameResources) {
        super.createBody(gameResources);
        body.setBullet(true);
    }

    private void sendPlayerStatus() {
        gameResources.getMessageManger().dispatchMessage(StateInterface.State.PLAYER_STATUS.telegramNumber, new PlayerTelegram(live));
    }

}
