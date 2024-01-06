package ua.org.petroff.game.engine.entities.characters.enemies.skeleton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.TelegramDescriber;
import ua.org.petroff.game.engine.entities.characters.base.creature.Creature;
import ua.org.petroff.game.engine.entities.characters.base.creature.View;
import ua.org.petroff.game.engine.entities.guns.arrow.Telegram;
import ua.org.petroff.game.engine.entities.ia.Box2dLocation;
import ua.org.petroff.game.engine.entities.ia.SteeringAgent;
import ua.org.petroff.game.engine.entities.player.Player;
import static ua.org.petroff.game.engine.entities.player.Player.FIRE_FORCE;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class Skeleton extends Creature {

    protected SteeringAgent creatureAI;

    public static final String OBJECT_NAME = "enemy";
    public static final String DESCRIPTOR = "Skeleton";
    public static final float FIRE_SPEED = 0.035f;

    protected static final float MOVEVELOCITY = 2f;
    protected static final float MAXMOVEVELOCITY = 20f;
    protected static final float MAXJUMPVELOCITY = 5f;
    protected static final float JUMPVELOCITY = 10f;

    public Skeleton(Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        super(asset, gameResources, graphicResources);
        initAI(gameResources);
        view = new View(asset, graphicResources, this, DESCRIPTOR, FIRE_SPEED);
    }

    @Override
    public void update() {
        stateMachine.update();
        Gdx.app.log("INFO", stateMachine.getCurrentState().toString());
    }

    public void move() {
        creatureAI.update();

        if (!creatureAI.getSteeringOutput().linear.isZero()) {
            //move
            body.applyForceToCenter(creatureAI.getSteeringOutput().linear.x, 0, true);
            isMove = true;

            if (creatureAI.getSteeringOutput().linear.x > 0) {
                vector = WorldInterface.Vector.RIGHT;
            } else {
                vector = WorldInterface.Vector.LEFT;
            }
        } else {
            isMove = false;
            vector = WorldInterface.Vector.STAY;
        }
    }

    public void stay() {
        vector = WorldInterface.Vector.STAY;
        isMove = false;
    }

    public boolean canFire() {
        float range = target.getPosition().x - body.getPosition().x;
        return (range >= -5 && range <= 5);
    }

    public void fire() {
        float x = body.getPosition().x;
        float y = body.getPosition().y;
        float forceX;
        if (creatureAI.getSteeringOutput().linear.x > 0) {
            vector = WorldInterface.Vector.RIGHT;
            x += 0.5f;
            forceX = +FIRE_FORCE;
        } else {
            vector = WorldInterface.Vector.LEFT;
            x -= 0.5f;
            forceX = -FIRE_FORCE;
        }
        if (view.isFinishAction(Type.FIRE)) {
            isHit = false;
            gameResources.getMessageManger().dispatchMessage(TelegramDescriber.FIRE, new Telegram(x, y, forceX));
        } else {
            isHit = true;
        }
    }

    private void initAI(GameResources gameResources) {
        Player player = (Player) gameResources.findModel(Player.class);
        target = new Box2dLocation(player.getPosition());
        creatureAI = new SteeringAgent(body);
        creatureAI.setMaxLinearSpeed(MOVEVELOCITY);
        creatureAI.setMaxLinearAcceleration(MAXMOVEVELOCITY);
        final Seek<Vector2> seekSB = new Seek<>(creatureAI, target);
        creatureAI.setSteeringBehavior(seekSB);

        stateMachine = new DefaultStateMachine<>(this, CreatureState.STAY);
    }

}
