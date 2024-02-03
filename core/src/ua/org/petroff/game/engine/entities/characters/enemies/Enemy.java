package ua.org.petroff.game.engine.entities.characters.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.characters.base.creature.Creature;
import ua.org.petroff.game.engine.entities.ia.Box2dLocation;
import ua.org.petroff.game.engine.entities.ia.SteeringAgent;
import ua.org.petroff.game.engine.entities.player.Player;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;

public class Enemy extends Creature {

    protected float maxMoveVelocity = 3f;
    protected float maxMoveAccVelocity = 20f;
    protected float maxJumpVelocity = 5f;
    protected float jumpVelocity = 10f;
    protected float stuckThresholdTime = 1f;
    protected float epsilonThreshold = 0.0001f;
    protected float stuckTime = 0f;
    protected float hitRange = 5;

    protected SteeringAgent creatureAI;
    protected StateMachine<Enemy, EnemyState> stateMachine;
    protected Vector2 previousLinearVelocity = new Vector2();
    protected Vector2 rayDirection = new Vector2(-1, -5);
    private final GapRay gapRay;

    public Enemy(int x, int y, Assets asset, GameResources gameResources, GraphicResources graphicResources, String descriptor) {
        super(x, y, descriptor, asset, gameResources, graphicResources);
        initAI(gameResources);
        gapRay = new GapRay(gameResources);
    }

    public void move() {
        if (!creatureAI.getSteeringOutput().linear.isZero()) {
            if (body.getLinearVelocity().len() < maxMoveVelocity) {
                body.applyForceToCenter(creatureAI.getSteeringOutput().linear.x, 0, true);
            }

            if (creatureAI.getSteeringOutput().linear.x > 0) {
                vector = WorldInterface.Vector.RIGHT;
                gapRay.update(body.getPosition().cpy().add(1, 0f));
            } else {
                gapRay.update(body.getPosition().cpy().sub(1, 0f));
                vector = WorldInterface.Vector.LEFT;
            }
        } else {
            vector = WorldInterface.Vector.STAY;
        }
    }

    public void stay() {
        vector = WorldInterface.Vector.STAY;
    }

    public boolean withinReachFire() {
        float range = target.getPosition().x - body.getPosition().x;
        return (range >= -hitRange && range <= hitRange);
    }

    public boolean isStuck() {

        if (gapRay.isGap()) {
            gapRay.reset();

            return true;
        }

        Vector2 currentLinearVelocity = body.getLinearVelocity();

        if (currentLinearVelocity.epsilonEquals(previousLinearVelocity, epsilonThreshold)) {
            stuckTime += Gdx.graphics.getDeltaTime();
        } else {
            stuckTime = 0;
        }

        previousLinearVelocity.set(currentLinearVelocity);

        if (stuckTime > stuckThresholdTime) {
            stuckTime = 0;

            return true;
        }

        return false;
    }

    public boolean fire() {
        if (creatureAI.getSteeringOutput().linear.x > 0) {
            vector = WorldInterface.Vector.RIGHT;
        } else {
            vector = WorldInterface.Vector.LEFT;
        }

        if (view.isFinishAction(State.FIRE)) {
            view.resetState(StateInterface.State.FIRE);

            return true;
        }

        return false;
    }

    public void jump() {
        if (onGround && body.getLinearVelocity().y < maxJumpVelocity) {
            float xMinimal = 5f * (creatureAI.getSteeringOutput().linear.x > 0 ? 1 : -1);
            float x = (Math.abs(body.getLinearVelocity().x) <= 0.2f) ? xMinimal : 0f;

            vector = (creatureAI.getSteeringOutput().linear.x > 0) ? WorldInterface.Vector.RIGHT : WorldInterface.Vector.LEFT;

            body.applyLinearImpulse(x, jumpVelocity, body.getPosition().x, body.getPosition().y, true);
            onGround = false;
        }
    }

    @Override
    public void update() {
        stateMachine.update();
        creatureAI.update();
    }

    @Override
    public StateInterface.State getState() {
        return State.valueOf(stateMachine.getCurrentState().toString());
    }

    public StateMachine<Enemy, EnemyState> getStateMachine() {
        return stateMachine;
    }

    public void changeState(EnemyState state) {
        if (stateMachine.getGlobalState() == EnemyState.DIED) {
            return;
        }
        stateMachine.changeState(state);
    }

    @Override
    public boolean handleMessage(com.badlogic.gdx.ai.msg.Telegram msg) {
        return stateMachine.handleMessage(msg);
    }

    private void initAI(GameResources gameResources) {
        Player player = (Player) gameResources.findModel(Player.class);
        target = new Box2dLocation(player.getPosition());
        creatureAI = new SteeringAgent(body);
        creatureAI.setMaxLinearAcceleration(maxMoveAccVelocity);
        final Seek<Vector2> seekSB = new Seek<>(creatureAI, target);
        creatureAI.setSteeringBehavior(seekSB);

        stateMachine = new DefaultStateMachine<>(this, EnemyState.STAY);
    }

}
