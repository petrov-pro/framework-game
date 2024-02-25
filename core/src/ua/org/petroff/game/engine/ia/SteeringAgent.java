package ua.org.petroff.game.engine.ia;

import com.badlogic.gdx.ai.steer.SteerableAdapter;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class SteeringAgent extends SteerableAdapter<Vector2> {

    protected SteeringBehavior<Vector2> steeringBehavior;

    private final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());

    private final Body body;

    private float maxLinearSpeed;
    private float maxLinearAcceleration;

    public SteeringAgent(Body body) {
        this.body = body;
    }

    public void update() {
        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);
        }
    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    public SteeringAcceleration<Vector2> getSteeringOutput() {
        return steeringOutput;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

}
