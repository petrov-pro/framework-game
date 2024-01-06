package ua.org.petroff.game.engine.entities.Interfaces;

public interface ActionInterface {

    public enum Type {
        MOVE, JUMP, DIED, FIRE
    };

    public boolean isGround();

    public boolean isDie();

    public boolean isHit();

    public boolean isMove();

    public void died();
}
