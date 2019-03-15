package ua.org.petroff.game.engine.scenes.level1.entities.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.EntityInterface;
import ua.org.petroff.game.engine.entities.ViewInterface;
import ua.org.petroff.game.engine.util.Assets;

public class Map implements EntityInterface {

    public static final String OBJECT_NAME = "start camera position";
    public static final String DESCRIPTOR = "map level 1";
    public View view;
    private final Assets asset;
    private Vector2 cameraPosition;

    public Map(Assets asset) {
        view = new View(asset, this);
        this.asset = asset;
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    @Override
    public void init() {
        MapObject cameraObject = ua.org.petroff.game.engine.util.Map.findObject(asset.getMap(), OBJECT_NAME);
        cameraPosition = new Vector2(cameraObject.getProperties().get("x", Float.class) / Settings.UNIT_SCALE, cameraObject.getProperties().get("y", Float.class) / Settings.UNIT_SCALE);
    }

    public Vector2 getCameraPosition() {
        return cameraPosition;
    }

}
