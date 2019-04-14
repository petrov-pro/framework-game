package ua.org.petroff.game.engine.entities.Interfaces;

import ua.org.petroff.game.engine.entities.player.Player;

public interface MoveEntityInterface {

    public void left();

    public void right();

    public void stop(Player.Actions action);

    public void jump();

    public void hit();

}
