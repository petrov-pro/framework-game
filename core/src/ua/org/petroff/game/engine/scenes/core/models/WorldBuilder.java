package ua.org.petroff.game.engine.scenes.core.models;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.TextureMapObject;

public class WorldBuilder {

    public void generate(Map map) {
        MapObjects objects = map.getLayers().get("obj").getObjects();

        for (MapObject object : objects) {

            if (object instanceof TextureMapObject) {
                continue;
            }

            String name = object.getProperties().get("name", String.class);
            String type = object.getProperties().get("type", String.class);
            String className = object.getProperties().get("class", String.class);
        }
    }

}
