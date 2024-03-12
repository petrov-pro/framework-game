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
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.characters.creature.CreatureInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.characters.creature.Creature;
import ua.org.petroff.game.engine.characters.enemies.Enemy;
import ua.org.petroff.game.engine.characters.creature.equipment.Shield;
import ua.org.petroff.game.engine.entities.equipments.PotionInterface;
import ua.org.petroff.game.engine.entities.hud.HUD;
import ua.org.petroff.game.engine.interfaces.SkinInterface;
import ua.org.petroff.game.engine.weapons.WeaponInterface;
import ua.org.petroff.game.engine.interfaces.ActionEntityInterface;

public class Player extends Creature implements EntityInterface, ActionEntityInterface, StateInterface, CreatureInterface {

    public static final String DESCRIPTOR = "player";
    public static final float FIRE_ARROW_SPEED = 0.0035f;
    public static final float FIRE_BARE_SPEED = 0.005f;
    public static final float FIRE_ARROW_FORCE = 7f;
    public static final int FIRE_DAMAGE = 10;
    public static final int FIRE_ARROW_DAMAGE = 10;

    public enum PlayerSize {
        NORMAL, GROWN
    };

    private static final float MAXMOVEVELOCITY = 5f;
    private static final float MAXJUMPVELOCITY = 5f;
    private static final float JUMPVELOCITY = 10f;
    private static final float MOVEVELOCITY = 0.8f;
    private final Vector3 cameraNewPosition = new Vector3();

    private PlayerSize playerSize = PlayerSize.NORMAL;
    private StateInterface.State state = StateInterface.State.MOVE;

    private ArrayList<WeaponInterface.Type> slotWeapons = new ArrayList<>(Collections.unmodifiableList(Arrays.asList(WeaponInterface.Type.BARE)));
    private WeaponInterface.Type weapon = WeaponInterface.Type.BARE;
    private Shield shield = null;
    private boolean hasShield = false;

    public Player(int x, int y, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        super(x, y, DESCRIPTOR, asset, gameResources, graphicResources);
        this.gameResources = gameResources;
        view = new View(asset, graphicResources, (Player) this);
        new TelegramProvider(this, gameResources);
        sendPlayerStatus();
        skin = SkinInterface.Type.DEFAULT;
        shield = new Shield(body, bodyWidth, bodyHeight);
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
                decreaseLife(((WeaponInterface) msg.extraInfo).getDamage(),
                        ((WeaponInterface) msg.extraInfo).getPlaceHit(),
                        ((WeaponInterface) msg.extraInfo).getDirectionHit()
                );
                state = State.HIT;
                vector = WorldInterface.Vector.STAY;
                break;

            case DIED:
                died();
                break;

            case GROUND:
                ground((boolean) msg.extraInfo);
                break;

            case EQUIPMENT:
                if (msg.extraInfo instanceof PotionInterface) {
                    changeLife(((PotionInterface) msg.extraInfo).getValue());
                } else if (msg.extraInfo instanceof ua.org.petroff.game.engine.entities.equipments.WeaponInterface
                        && !slotWeapons.contains(((ua.org.petroff.game.engine.entities.equipments.WeaponInterface) msg.extraInfo).getWeaponType())) {
                    int slot = slotWeapons.indexOf(weapon);
                    if (slot == 0) {
                        slot = 1;
                    }

                    slotWeapons.add(slot, ((ua.org.petroff.game.engine.entities.equipments.WeaponInterface) msg.extraInfo).getWeaponType());

                    if (slotWeapons.size() >= HUD.COUNTSLOT) {
                        slotWeapons.remove(HUD.COUNTSLOT);
                    }
                } else if (msg.extraInfo instanceof ua.org.petroff.game.engine.entities.equipments.shield.Shield && !hasShield) {
                    hasShield = true;
                }
                sendPlayerStatus();
                break;

            case CREATURE_COLLISION:
                ground(true);
                if (msg.extraInfo instanceof StateInterface && ((StateInterface) msg.extraInfo).getState() == State.DIED) {
                    state = State.MOVE;

                    return true;
                }

                if (msg.extraInfo instanceof Enemy) {
                    decreaseLife(5, body.getPosition());
                    sendPlayerStatus();
                    state = State.HIT;
                    vector = WorldInterface.Vector.STAY;
                }

                break;
        }

        return true;
    }

    @Override
    public void decreaseLife(int amount, Vector2 placeHit, Vector2 directionHit) {
        super.decreaseLife(amount, placeHit, directionHit);
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
            state = StateInterface.State.JUMP;
        }
    }

    @Override
    public void fire(boolean active) {
        if (active) {
            state = StateInterface.State.FIRE;
        } else {
            state = StateInterface.State.MOVE;
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
            sendPlayerStatus();
        }
    }

    @Override
    public void block(boolean active) {
        if (!hasShield) {
            return;
        }

        if (!active) {
            shield.hide();
            state = StateInterface.State.MOVE;

            return;
        }
        state = StateInterface.State.BLOCK;
    }

    @Override
    public void update() {
        Gdx.app.log("DEBUG", state.toString() + ":" + vector.toString());
        if (state == State.DIED || life <= 0) {
            died();

            return;
        }

        if (state == State.HIT) {
            if (!view.isFinishState(state)) {
                vector = WorldInterface.Vector.STAY;

                return;
            }

            view.resetState(state);
            state = State.MOVE;
        }

        if (state == StateInterface.State.BLOCK) {
            block();

            return;
        }

        if (state == StateInterface.State.FIRE) {
            if (!view.isFinishState(StateInterface.State.FIRE)) {
                return;
            }

            sendFire();
            state = StateInterface.State.MOVE;
            view.resetState(StateInterface.State.FIRE);
        }

//      handler if body have vertical velocity
        if (state != StateInterface.State.JUMP && !onGround && body.getLinearVelocity().y <= -3f) {
            state = StateInterface.State.JUMP;
        }

        if (state == StateInterface.State.JUMP && onGround && body.getLinearVelocity().y < MAXJUMPVELOCITY) {
            body.applyLinearImpulse(0, JUMPVELOCITY, body.getPosition().x, body.getPosition().y, true);
            //for force apply
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
        if (isGround && state == State.JUMP) {
            state = State.MOVE;
        }
    }

    @Override
    public void died() {
        super.died();

        gameResources.getMessageManger().dispatchMessage(StateInterface.State.PLAYER_DEAD.telegramNumber);
        state = StateInterface.State.DIED;
    }

    @Override
    public StateInterface.State getState() {
        return state;
    }

    public WeaponInterface.Type getWeapon() {
        return weapon;
    }

    public ArrayList<WeaponInterface.Type> getSlotWeapons() {
        return slotWeapons;
    }

    public boolean hasShield() {
        return hasShield;
    }

    @Override
    protected void createBody(GameResources gameResources) {
        super.createBody(gameResources);
        body.setBullet(true);
    }

    private void sendFire() {
        switch (weapon) {
            case BARE:
                gameResources.getMessageManger().dispatchMessage(
                        StateInterface.State.FIRE.telegramNumber,
                        new ua.org.petroff.game.engine.entities.weapons.melee.Telegram(
                                WeaponInterface.Type.BARE,
                                vector,
                                body.getPosition().cpy(),
                                FIRE_DAMAGE,
                                bodyWidth / 2,
                                0.1f,
                                0.2f
                        )
                );
                break;
            case BOW:
                gameResources.getMessageManger().dispatchMessage(StateInterface.State.FIRE.telegramNumber, new ua.org.petroff.game.engine.entities.weapons.ranged.Telegram(
                        WeaponInterface.Type.BOW,
                        vector,
                        body.getPosition().cpy(),
                        FIRE_ARROW_DAMAGE,
                        bodyWidth / 2,
                        0.1f,
                        FIRE_ARROW_FORCE
                ));
                break;
            case SWORD:
                System.out.println("Wednesday");
                break;

        }
    }

    private void block() {
        if (!hasShield) {
            return;
        }

        switch (vector) {
            case RIGHT:
                shield.right();
                break;
            case LEFT:
                shield.left();
                break;
            default:
                shield.hide();
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

    private void sendPlayerStatus() {
        gameResources.getMessageManger().dispatchMessage(StateInterface.State.PLAYER_STATUS.telegramNumber, this);
    }

}
