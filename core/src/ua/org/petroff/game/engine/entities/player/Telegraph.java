package ua.org.petroff.game.engine.entities.player;

import ua.org.petroff.game.engine.characters.enemies.Enemy;
import ua.org.petroff.game.engine.entities.equipments.AmmoInterface;
import ua.org.petroff.game.engine.entities.equipments.PotionInterface;
import ua.org.petroff.game.engine.entities.equipments.ammo.Ammo;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class Telegraph implements com.badlogic.gdx.ai.msg.Telegraph {

    private final Player player;

    public Telegraph(Player player) {
        this.player = player;
    }

    @Override
    public boolean handleMessage(com.badlogic.gdx.ai.msg.Telegram msg) {

        switch (StateInterface.State.getStateBy(msg.message)) {

            case HIT:
                player.decreaseLife(((WeaponInterface) msg.extraInfo).getDamage(),
                        ((WeaponInterface) msg.extraInfo).getPlaceHit(),
                        ((WeaponInterface) msg.extraInfo).getDirectionHit()
                );
                player.setState(StateInterface.State.HIT);

                player.setVector(WorldInterface.Vector.STAY);
                break;

            case DIED:
                player.died();
                break;

            case GROUND:
                player.ground((boolean) msg.extraInfo);
                break;

            case EQUIPMENT:
                if (msg.extraInfo instanceof PotionInterface) {
                    player.changeLife(((PotionInterface) msg.extraInfo).getValue());
                } else if (msg.extraInfo instanceof AmmoInterface) {
                    AmmoInterface ammo = (AmmoInterface) msg.extraInfo;
                    player.weapon.addAmmo(ammo.getWeaponType(), ammo.getAmmo());
                } else if (msg.extraInfo instanceof ua.org.petroff.game.engine.entities.equipments.WeaponInterface) {
                    ua.org.petroff.game.engine.entities.equipments.WeaponInterface weaponNew = (ua.org.petroff.game.engine.entities.equipments.WeaponInterface) msg.extraInfo;
                    player.weapon.addToSlot(
                            weaponNew.getWeaponType(),
                            Ammo.getAmmo(weaponNew.getWeaponType())
                    );
                } else if (msg.extraInfo instanceof ua.org.petroff.game.engine.entities.equipments.shield.Shield && !player.ability.hasShield()) {
                    player.ability.setShieldStatus(true);
                }
                player.sendPlayerStatus();
                break;

            case CREATURE_COLLISION:
                player.ground(true);
                if (msg.extraInfo instanceof StateInterface && ((StateInterface) msg.extraInfo).getState() == StateInterface.State.DIED) {
                    player.setState(StateInterface.State.MOVE);

                    return true;
                }

                if (msg.extraInfo instanceof Enemy) {
                    player.decreaseLife(5, player.getBody().getPosition());
                    player.sendPlayerStatus();
                    player.setState(StateInterface.State.HIT);
                    player.setVector(WorldInterface.Vector.STAY);
                }

                break;
        }

        return true;
    }
}
