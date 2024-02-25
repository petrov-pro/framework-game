package ua.org.petroff.game.engine.entities.weapons.hand;

import com.badlogic.gdx.ai.msg.Telegraph;
import java.util.ArrayList;
import ua.org.petroff.game.engine.interfaces.EntityInterface;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.weapons.WeaponInterface;
import ua.org.petroff.game.engine.weapons.WeaponListener;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.util.Assets;

public class HandWeaponFactory implements EntityInterface, Telegraph {

    public static final String DESCRIPTOR = "handWeapon";
    public static final int MAX = 500;

    private final ArrayList<HandWeapon> weapons = new ArrayList<>();

    public HandWeaponFactory(Assets asset, GameResources gameResources) {
        gameResources.getWorldContactListener().addUniqListener(new WeaponListener(gameResources));
        gameResources.getMessageManger().addListener(this, StateInterface.State.FIRE.telegramNumber);
    }

    @Override
    public void update() {
        weapons.forEach(HandWeapon::destroy);
        weapons.clear();
    }

    private void shoot(Telegram telegram) {
        weapons.add(new HandWeapon(telegram.getBody(), telegram.getPositionHitX(), telegram.getPositionHitY(), telegram.getX(), telegram.getY(), telegram.getDamage()));
    }

    @Override
    public boolean handleMessage(com.badlogic.gdx.ai.msg.Telegram tlgrm) {
        if (StateInterface.State.getStateBy(tlgrm.message) == StateInterface.State.FIRE
                && tlgrm.extraInfo instanceof Telegram
                && WeaponInterface.handWeapon.contains(((Telegram) tlgrm.extraInfo).getType())) {
            shoot((Telegram) tlgrm.extraInfo);
        }
        return true;
    }

}
