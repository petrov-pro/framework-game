package ua.org.petroff.game.engine.util;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import ua.org.petroff.game.engine.Settings;

public class MapHelper {

    private final static String OBJECT_LAYER_NAME = "object_layer_name";

    public static MapObject findObject(com.badlogic.gdx.maps.Map map, String objectName) {

        String objecLayerName = map.getProperties().get(OBJECT_LAYER_NAME, String.class);
        if (objecLayerName == null) {
            throw new Error("Map is incorrect, cant find base element: " + OBJECT_LAYER_NAME);
        }
        MapObjects objects = map.getLayers().get(objecLayerName).getObjects();

        for (MapObject object : objects) {
            if (objectName.equals(object.getName())) {
                return object;
            }

        }
        throw new Error("Map is incorrect, cant find object: " + objectName);
    }

    public static float coordinateToWorld(int coordinate) {
        return coordinate * Settings.SCALE;
    }

    public static int coordinateToScreen(float coordinate) {
        return (int) (coordinate / Settings.SCALE);
    }

}
