package ua.org.petroff.game.engine.characters.enemies;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import ua.org.petroff.game.engine.interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.enemies.simple.DirtyManBow;
import ua.org.petroff.game.engine.entities.enemies.simple.DirtyManSword;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class EnemyFactory {

    public static final String DESCRIPTOR = "enemy";

    public static void addEnemy(Array<EntityInterface> entities, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        ArrayList<MapObject> enemies = ua.org.petroff.game.engine.util.MapResolver.findObjects(asset.getMap(),
                DESCRIPTOR);

        for (MapObject enemy : enemies) {
            String enemyType = enemy.getProperties().get("type", String.class);
            int x = enemy.getProperties().get("x", Float.class).intValue();
            int y = enemy.getProperties().get("y", Float.class).intValue();

            switch (enemyType) {

                case DirtyManBow.DESCRIPTOR:
                    entities.add(new DirtyManBow(x, y, asset, gameResources, graphicResources));
                    break;

                case DirtyManSword.DESCRIPTOR:
                    entities.add(new DirtyManSword(x, y, asset, gameResources, graphicResources));
                    break;

            }

        }
    }

}
