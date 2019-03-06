package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.math.Vector2;
import ua.org.petroff.game.engine.entities.EntityInterface;
import ua.org.petroff.game.engine.entities.ViewInterface;
import ua.org.petroff.game.engine.util.Assets;

public class Player implements EntityInterface {

    public static final String DESCRIPTOR = "Player";
    public Actions state;
    private final Vector2 position;

    public enum Actions {
        MOVELEFT, MOVERIGHT, JUMP, STAY
    };
    public View view;

    public Player(Assets asset) {
        view = new View(asset, this);
        state = Actions.STAY;
        position = new Vector2(0, 15);
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    public Vector2 getPosition() {
        return position;
    }

}
