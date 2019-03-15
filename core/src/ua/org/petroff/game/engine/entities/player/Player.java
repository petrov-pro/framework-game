package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.EntityInterface;
import ua.org.petroff.game.engine.entities.MoveEntityInterface;
import ua.org.petroff.game.engine.entities.ViewInterface;
import static ua.org.petroff.game.engine.scenes.level1.entities.map.Map.OBJECT_NAME;
import ua.org.petroff.game.engine.util.Assets;

public class Player implements EntityInterface, MoveEntityInterface {

    public static final String OBJECT_NAME = "start player";
    public static final String DESCRIPTOR = "Player";
    public Actions state;
    private Vector2 position;
    private final Assets asset;

    public enum Actions {
        MOVELEFT, MOVERIGHT, JUMP, STAY
    };
    public View view;

    public Player(Assets asset) {
        view = new View(asset, this);
        state = Actions.STAY;
        this.asset = asset;
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void init() {
        MapObject playerObject = ua.org.petroff.game.engine.util.Map.findObject(asset.getMap(), OBJECT_NAME);
        position = new Vector2(playerObject.getProperties().get("x", Float.class) / Settings.UNIT_SCALE, playerObject.getProperties().get("y", Float.class) / Settings.UNIT_SCALE);
    }

    @Override
    public void left() {
        position.add(-1, 0);
    }

    @Override
    public void right() {
        position.add(1, 0);
    }

    @Override
    public void stop() {
        position.add(0, 0);
    }

}
