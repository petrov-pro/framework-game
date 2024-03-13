package ua.org.petroff.game.engine.entities.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static ua.org.petroff.game.engine.entities.player.Player.FIRE_ARROW_DAMAGE;
import static ua.org.petroff.game.engine.entities.player.Player.FIRE_ARROW_FORCE;
import static ua.org.petroff.game.engine.entities.player.Player.FIRE_DAMAGE;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class Weapon {

    private final GameResources gameResources;
    private ArrayList<WeaponInterface.Type> slotWeapons = new ArrayList<>(Collections.unmodifiableList(Arrays.asList(WeaponInterface.Type.BARE)));
    private WeaponInterface.Type weapon = WeaponInterface.Type.BARE;
    private int ammo = 0;

    public Weapon(GameResources gameResources) {
        this.gameResources = gameResources;
    }

    public ArrayList<WeaponInterface.Type> getSlotWeapons() {
        return slotWeapons;
    }

    public WeaponInterface.Type getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponInterface.Type weapon) {
        this.weapon = weapon;
    }

    public void addAmmo(int ammo) {
        this.ammo = this.ammo + ammo;
    }

    public void sendFire(Player player) {
        switch (weapon) {
            case BARE:
                gameResources.getMessageManger().dispatchMessage(
                        StateInterface.State.FIRE.telegramNumber,
                        new ua.org.petroff.game.engine.entities.weapons.melee.Telegram(
                                WeaponInterface.Type.BARE,
                                player.getVector(),
                                player.getBody().getPosition().cpy(),
                                FIRE_DAMAGE,
                                player.getBodyWidth() / 2,
                                0.1f,
                                0.2f
                        )
                );
                break;
            case BOW:
                gameResources.getMessageManger().dispatchMessage(StateInterface.State.FIRE.telegramNumber, new ua.org.petroff.game.engine.entities.weapons.ranged.Telegram(
                        WeaponInterface.Type.BOW,
                        player.getVector(),
                        player.getBody().getPosition().cpy(),
                        FIRE_ARROW_DAMAGE,
                        player.getBodyWidth() / 2,
                        0.1f,
                        FIRE_ARROW_FORCE
                ));
                break;
            case SWORD:
                System.out.println("Wednesday");
                break;

        }
    }

}
