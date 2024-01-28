package ua.org.petroff.game.engine.util;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import java.util.ArrayList;
import ua.org.petroff.game.engine.Settings;

public class MapResolver {

    private final static String OBJECT_LAYER_NAME = "object_layer_name";

    public static <T> T findObject(com.badlogic.gdx.maps.Map map, String objectName) {

        String objecLayerName = map.getProperties().get(OBJECT_LAYER_NAME, String.class);
        if (objecLayerName == null) {
            throw new Error("Map is incorrect, cant find base element: " + OBJECT_LAYER_NAME);
        }
        MapObjects objects = map.getLayers().get(objecLayerName).getObjects();

        for (MapObject object : objects) {
            if (objectName.equals(object.getName())) {
                return (T) object;
            }
        }

        throw new Error("Map is incorrect, cant find object: " + objectName);

    }

    public static <T> ArrayList<T> findObjects(com.badlogic.gdx.maps.Map map, String objectName) {

        String objecLayerName = map.getProperties().get(OBJECT_LAYER_NAME, String.class);
        if (objecLayerName == null) {
            throw new Error("Map is incorrect, cant find base element: " + OBJECT_LAYER_NAME);
        }
        MapObjects objects = map.getLayers().get(objecLayerName).getObjects();

        ArrayList<T> findedObject = new ArrayList();

        for (MapObject object : objects) {
            if (objectName.equals(object.getName())) {
                findedObject.add((T) object);
            }
        }

        if (findedObject.isEmpty()) {
            throw new Error("Map is incorrect, cant find object: " + objectName);
        }

        return findedObject;

    }

    public static float coordinateToWorld(int coordinate) {
        return coordinate * Settings.SCALE;
    }

    public static int coordinateToScreen(float coordinate) {
        return (int) (coordinate / Settings.SCALE);
    }

}
