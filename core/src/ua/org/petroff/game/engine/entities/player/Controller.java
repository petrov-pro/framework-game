package ua.org.petroff.game.engine.entities.player;

import ua.org.petroff.game.engine.interfaces.ActionEntityInterface;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.interfaces.ViewNotifierInterface;
import ua.org.petroff.game.engine.interfaces.WorldInterface;

public class Controller implements ActionEntityInterface {

    private final Player player;

    public Controller(Player player) {
        this.player = player;
    }

    @Override
    public void left(boolean active) {
        if (!active && player.getVector() == WorldInterface.Vector.LEFT) {
            player.setVector(WorldInterface.Vector.STAY);
        } else if (active) {
            player.setVector(WorldInterface.Vector.LEFT);
        }
    }

    @Override
    public void right(boolean active) {
        if (!active && player.getVector() == WorldInterface.Vector.RIGHT) {
            player.setVector(WorldInterface.Vector.STAY);
        } else if (active) {
            player.setVector(WorldInterface.Vector.RIGHT);
        }
    }

    @Override
    public void jump(boolean active) {
        if (active) {
            player.setState(StateInterface.State.JUMP);
        }
    }

    @Override
    public void fire(boolean active) {
        if (active) {
            player.setState(StateInterface.State.FIRE);
        } else {
            player.setState(StateInterface.State.MOVE);
            ((ViewNotifierInterface) player.getView()).resetState(StateInterface.State.FIRE);
        }
    }

    @Override
    public void ability(boolean active) {
        if (!active) {
            return;
        }

        if (player.ability.getPlayerSize().equals(Ability.PlayerSize.NORMAL)) {
            player.ability.playerGrow();
        } else {
            player.ability.playerNormal();
        }
    }

    @Override
    public void slot(int number) {
        if (player.weapon.getSlotWeapons().size() > number) {
            player.weapon.setWeapon(player.weapon.getSlotWeapons().get(number));
            player.sendPlayerStatus();
        }
    }

    @Override
    public void block(boolean active) {
        if (!player.ability.hasShield()) {
            return;
        }

        if (!active) {
            player.ability.getShield().hide();
            player.setState(StateInterface.State.MOVE);

            return;
        }
        player.setState(StateInterface.State.BLOCK);
    }

}
