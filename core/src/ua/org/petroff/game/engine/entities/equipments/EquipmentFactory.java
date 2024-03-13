package ua.org.petroff.game.engine.entities.equipments;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import ua.org.petroff.game.engine.entities.equipments.ammo.Ammo;
import ua.org.petroff.game.engine.entities.equipments.box.Box;
import ua.org.petroff.game.engine.entities.equipments.health.Health;
import ua.org.petroff.game.engine.entities.equipments.shield.Shield;
import ua.org.petroff.game.engine.entities.equipments.weapon.Weapon;
import ua.org.petroff.game.engine.interfaces.EntityInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class EquipmentFactory {

    public static final String DESCRIPTOR = "equipment";

    public static void addEquipment(Array<EntityInterface> entities, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        ArrayList<MapObject> equipments = ua.org.petroff.game.engine.util.MapResolver.findObjects(asset.getMap(),
                DESCRIPTOR);

        for (MapObject equipment : equipments) {
            String equipmentType = equipment.getProperties().get("type", String.class);
            int x = equipment.getProperties().get("x", Float.class).intValue();
            int y = equipment.getProperties().get("y", Float.class).intValue();

            switch (equipmentType) {
                case Box.DESCRIPTOR:
                    int bodyWidth = equipment.getProperties().get("width", Float.class).intValue();
                    int bodyHeight = equipment.getProperties().get("height", Float.class).intValue();
                    entities.add(new Box(x, y, bodyWidth, bodyHeight, asset, gameResources, graphicResources));

                    break;

                case Health.DESCRIPTOR:
                    entities.add(new Health(x, y, asset, gameResources, graphicResources));
                    break;

                case Shield.DESCRIPTOR:
                    entities.add(new Shield(x, y, asset, gameResources, graphicResources));
                    break;

                case Weapon.DESCRIPTOR:
                    String subType = equipment.getProperties().get("sub_type", String.class);
                    entities.add(new Weapon(x, y, asset, gameResources, graphicResources, subType));
                    break;
                    
                case Ammo.DESCRIPTOR:
                    String subTypeAmmo = equipment.getProperties().get("sub_type", String.class);
                    entities.add(new Ammo(x, y, asset, gameResources, graphicResources, subTypeAmmo));
                    break;
            }

        }

        gameResources.getWorldContactListener().addListener(new EquipmentListener(gameResources));
    }

}
