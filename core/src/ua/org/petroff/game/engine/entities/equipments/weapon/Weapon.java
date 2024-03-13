package ua.org.petroff.game.engine.entities.equipments.weapon;

import ua.org.petroff.game.engine.entities.equipments.View;
import ua.org.petroff.game.engine.entities.equipments.EquipmentUsing;
import ua.org.petroff.game.engine.entities.equipments.WeaponInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class Weapon extends EquipmentUsing implements WeaponInterface {

    public static final String DESCRIPTOR = "weapon";

    private final static int BODYWIDTH = 14;
    private final static int BODYHEIGHT = 14;

    private String objectType;

    public Weapon(int x, int y, Assets asset, GameResources gameResources, GraphicResources graphicResources, String objectType) {
        super(x, y, BODYWIDTH, BODYHEIGHT, asset, gameResources, graphicResources);
        this.objectType = objectType;
        view = new View(asset, graphicResources, this, objectType);
    }

    @Override
    public ua.org.petroff.game.engine.weapons.WeaponInterface.Type getWeaponType() {

        switch (objectType) {

            case "bow":
                return ua.org.petroff.game.engine.weapons.WeaponInterface.Type.BOW;

            case "sword":
                return ua.org.petroff.game.engine.weapons.WeaponInterface.Type.SWORD;

        }
        return ua.org.petroff.game.engine.weapons.WeaponInterface.Type.BARE;
    }

    @Override
    public int getAmmo() {
        if (objectType == "bow") {
            return 10;
        }

        return 0;
    }

}
