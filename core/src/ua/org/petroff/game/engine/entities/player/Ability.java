package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.characters.creature.equipment.Shield;
import ua.org.petroff.game.engine.interfaces.WorldInterface;

public class Ability {

    public enum PlayerSize {
        NORMAL, GROWN
    };

    private Ability.PlayerSize playerSize = Ability.PlayerSize.NORMAL;
    private final Player player;

    private Shield shield = null;
    private boolean hasShield = false;

    public Ability(Player player) {
        this.player = player;
        shield = new Shield(player.getBody(), player.getBodyWidth(), player.getBodyHeight());
    }

    public Ability.PlayerSize getPlayerSize() {
        return playerSize;
    }

    public void playerGrow() {
        player.getBody().setActive(true);
        playerSize = Ability.PlayerSize.GROWN;

        float bodyHeightGrow = (player.getBodyHeight() / 2) + 0.2f;
        float bodyWidthGrow = (player.getBodyWidth() / 2) + 0.15f;
        ((PolygonShape) player.getBody().getFixtureList().get(0).getShape()).setAsBox(bodyWidthGrow, bodyHeightGrow);

        ((PolygonShape) player.getBody().getFixtureList().get(1).getShape()).setAsBox(bodyWidthGrow - 0.05f, 0.05f, player.getBody().getLocalCenter().cpy()
                .sub(0, bodyHeightGrow), 0);
        player.getBody().resetMassData();
        Vector2 newPosition = player.getBody().getTransform().getPosition();
        player.getBody().setTransform(newPosition.add(0, 0.4f), 0);
    }

    public void playerNormal() {
        player.getBody().setActive(true);
        playerSize = Ability.PlayerSize.NORMAL;

        ((PolygonShape) player.getBody().getFixtureList().get(0).getShape()).setAsBox(player.getBodyWidth() / 2, player.getBodyHeight() / 2);
        ((PolygonShape) player.getBody().getFixtureList().get(1).getShape()).setAsBox((player.getBodyWidth() / 2f) - 0.05f, 0.05f, player.getBody().getLocalCenter().cpy().sub(0, player.getBodyHeight() / 2), 0);
        player.getBody().resetMassData();
        Vector2 newPosition = player.getBody().getTransform().getPosition();
        player.getBody().setTransform(newPosition.add(0, 0.4f), 0);
    }

    public boolean hasShield() {
        return hasShield;
    }

    public void setShieldStatus(boolean hasShield) {
        this.hasShield = hasShield;
    }

    public Shield getShield() {
        return shield;
    }

    public void block(WorldInterface.Vector vector) {
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

}
