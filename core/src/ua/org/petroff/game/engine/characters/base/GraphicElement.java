package ua.org.petroff.game.engine.characters.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.interfaces.StateInterface;

abstract public class GraphicElement {

    protected boolean isLoop;
    protected boolean isFinish = false;
    protected float stateTime;
    protected StateInterface.State action;
    protected WorldInterface.Vector vector;

    public GraphicElement(StateInterface.State action, WorldInterface.Vector vector, boolean isLoop) {
        this.stateTime = 0f;
        this.isLoop = isLoop;
        this.action = action;
        this.vector = vector;
    }

    public GraphicElement(StateInterface.State action, boolean isLoop) {
        this.stateTime = 0f;
        this.isLoop = isLoop;
        this.action = action;
    }

    public abstract TextureRegion prepareGraphic();

    public boolean isLoop() {
        return isLoop;
    }

    public void setLoop(boolean isLoop) {
        this.isLoop = isLoop;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public WorldInterface.Vector getVector() {
        return vector;
    }

    public StateInterface.State getActionType() {
        return action;
    }

}
