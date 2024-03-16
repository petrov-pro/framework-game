package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.crashinvaders.vfx.effects.LensFlareEffect;
import ua.org.petroff.game.engine.interfaces.EntityInterface;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.characters.creature.CreatureInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.characters.creature.Creature;
import ua.org.petroff.game.engine.interfaces.SkinInterface;

public class Player extends Creature implements EntityInterface, StateInterface, CreatureInterface {
    
    public static final String DESCRIPTOR = "player";
    public static final float FIRE_ARROW_SPEED = 0.0035f;
    public static final float FIRE_BARE_SPEED = 0.005f;
    public static final float FIRE_ARROW_FORCE = 7f;
    public static final int FIRE_DAMAGE = 10;
    public static final int FIRE_ARROW_DAMAGE = 10;
    
    public final Weapon weapon;
    public final Ability ability;
    
    private static final float MAXMOVEVELOCITY = 5f;
    private static final float MAXJUMPVELOCITY = 5f;
    private static final float JUMPVELOCITY = 10f;
    private static final float MOVEVELOCITY = 0.8f;
    private final Vector3 cameraNewPosition = new Vector3();
    
    private StateInterface.State state = StateInterface.State.MOVE;
    
    private Telegraph telegraphHandler;
    
    public Player(int x, int y, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        super(x, y, DESCRIPTOR, asset, gameResources, graphicResources);
        this.gameResources = gameResources;
        view = new View(asset, graphicResources, (Player) this);
        new TelegramProvider(this, gameResources);
        skin = SkinInterface.Type.DEFAULT;
        telegraphHandler = new Telegraph(this);
        weapon = new Weapon(gameResources);
        ability = new Ability(this);
        //Last action
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
    public boolean handleMessage(Telegram msg) {
        return telegraphHandler.handleMessage(msg);
    }
    
    @Override
    public void decreaseLife(int amount, Vector2 placeHit, Vector2 directionHit) {
        super.decreaseLife(amount, placeHit, directionHit);
        sendPlayerStatus();
    }
    
    public Vector3 getCameraNewPosition() {
        return cameraNewPosition;
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
            ability.block(vector);
            
            return;
        }
        
        if (state == StateInterface.State.FIRE) {
            if (!view.isFinishState(StateInterface.State.FIRE)) {
                return;
            }
            
            weapon.sendFire(this);
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
        graphicResources.getVfxManager().addEffect(new LensFlareEffect());
    }
    
    @Override
    public StateInterface.State getState() {
        return state;
    }
    
    public void setState(State state) {
        this.state = state;
    }
    
    public void setVector(WorldInterface.Vector vector) {
        this.vector = vector;
    }
    
    public void sendPlayerStatus() {
        gameResources.getMessageManger().dispatchMessage(StateInterface.State.PLAYER_STATUS.telegramNumber, this);
    }
    
    public void calculateCameraPositionForPlayer() {
        //optimization
        cameraNewPosition.x = view.graphicResources.getCamera().position.x;
        cameraNewPosition.y = view.graphicResources.getCamera().position.y;
        
        cameraNewPosition.x += (getPosition().x - view.graphicResources.getCamera().position.x) * 0.9f * Gdx.graphics.getDeltaTime();
        cameraNewPosition.y += (getPosition().y - view.graphicResources.getCamera().position.y + 3) * 0.9f * Gdx.graphics.getDeltaTime();
    }
    
    @Override
    protected void createBody(GameResources gameResources) {
        super.createBody(gameResources);
        body.setBullet(true);
    }
    
}
