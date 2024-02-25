package ua.org.petroff.game.engine.interfaces;

public interface MoveEntityInterface {

    public void left(boolean active);

    public void right(boolean active);

    public void jump(boolean active);

    public void fire(boolean active);

    public void ability(boolean active);

    public void slot(int number);

}
