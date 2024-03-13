package ua.org.petroff.game.engine.entities.equipments.ammo;

import ua.org.petroff.game.engine.entities.equipments.AmmoInterface;
import ua.org.petroff.game.engine.entities.equipments.View;
import ua.org.petroff.game.engine.entities.equipments.EquipmentUsing;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class Ammo extends EquipmentUsing implements AmmoInterface {

    public static final String DESCRIPTOR = "ammo";

    private final static int BODYWIDTH = 14;
    private final static int BODYHEIGHT = 14;

    private final ua.org.petroff.game.engine.weapons.WeaponInterface.Type weapon;

    public Ammo(int x, int y, Assets asset, GameResources gameResources, GraphicResources graphicResources, String objectType) {
        super(x, y, BODYWIDTH, BODYHEIGHT, asset, gameResources, graphicResources);
        view = new View(asset, graphicResources, this, DESCRIPTOR);
        weapon = ua.org.petroff.game.engine.weapons.WeaponInterface.Type.valueOf(objectType);
    }

    @Override
    public int getAmmo() {
        return getAmmo(weapon);
    }

    @Override
    public WeaponInterface.Type getWeaponType() {
        return weapon;
    }

    public static int getAmmo(ua.org.petroff.game.engine.weapons.WeaponInterface.Type weaponType) {
        switch (weaponType) {
            case BOW:
                return 10;
        }

        return 0;
    }

}
