package ua.org.petroff.game.engine.entities.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import ua.org.petroff.game.engine.entities.hud.HUD;
import static ua.org.petroff.game.engine.entities.player.Player.FIRE_ARROW_DAMAGE;
import static ua.org.petroff.game.engine.entities.player.Player.FIRE_ARROW_FORCE;
import static ua.org.petroff.game.engine.entities.player.Player.FIRE_DAMAGE;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class Weapon {

    private final GameResources gameResources;
    private ArrayList<WeaponInterface.Type> slotWeapons = new ArrayList<>(Collections.unmodifiableList(Arrays.asList(WeaponInterface.Type.BARE)));
    private HashMap<WeaponInterface.Type, Integer> ammo = new HashMap<>();

    private WeaponInterface.Type weapon = WeaponInterface.Type.BARE;

    public Weapon(GameResources gameResources) {
        this.gameResources = gameResources;
    }

    public ArrayList<WeaponInterface.Type> getSlotWeapons() {
        return slotWeapons;
    }

    public HashMap<WeaponInterface.Type, Integer> getAllAmmo() {
        return ammo;
    }

    public WeaponInterface.Type getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponInterface.Type weapon) {
        this.weapon = weapon;
    }

    public void addAmmo(WeaponInterface.Type weaponNew, int ammoToAdd) {
        ammo.merge(weaponNew, ammoToAdd, Integer::sum);
    }

    public void sendFire(Player player) {

        if (WeaponInterface.rangedWeapon.contains(weapon)) {
            if (ammo.getOrDefault(weapon, 0) <= 0) {
                return;
            }
            ammo.merge(weapon, -1, Integer::sum);
            player.sendPlayerStatus();
        }

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

    public void addToSlot(WeaponInterface.Type weaponNew, int ammoAdd) {
        if (slotWeapons.contains(weaponNew)) {
            return;
        }

        int slot = slotWeapons.indexOf(weapon);
        if (slot == 0) {
            slot = 1;
        }

        slotWeapons.add(slot, weaponNew);
        addAmmo(weaponNew, ammoAdd);

        if (slotWeapons.size() >= HUD.COUNTSLOT) {
            slotWeapons.remove(HUD.COUNTSLOT);
        }
    }

    public Integer getAmmo() {
        return ammo.getOrDefault(weapon, 0);
    }

}
