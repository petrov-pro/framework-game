package ua.org.petroff.game.engine.entities.weapons.melee;

import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Pool;
import java.util.ArrayList;
import ua.org.petroff.game.engine.interfaces.EntityInterface;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.weapons.WeaponInterface;
import ua.org.petroff.game.engine.weapons.WeaponListener;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.util.Assets;

public class MeleeWeaponFactory implements EntityInterface, Telegraph {

    public static final String DESCRIPTOR = "meleeWeapon";
    public static final int MAX = 500;

    private final ArrayList<MeleeWeapon> weapons = new ArrayList<>();
    private final Pool<MeleeWeapon> meleeWeaponPool = new Pool<MeleeWeapon>() {
        @Override
        protected MeleeWeapon newObject() {
            return new MeleeWeapon();
        }
    };

    private GameResources gameResources;

    public MeleeWeaponFactory(Assets asset, GameResources gameResources) {
        gameResources.getWorldContactListener().addUniqListener(new WeaponListener(gameResources));
        gameResources.getMessageManger().addListener(this, StateInterface.State.FIRE.telegramNumber);
        this.gameResources = gameResources;
    }

    @Override
    public void update() {
        for (MeleeWeapon meleeWeapon : weapons) {
            meleeWeapon.update();
            meleeWeaponPool.free(meleeWeapon);
        }
        weapons.clear();
    }

    private void shoot(Telegram telegram) {
        MeleeWeapon meleeWeapon = meleeWeaponPool.obtain();
        meleeWeapon.init(gameResources, telegram.getStart(), telegram.getFinish(), telegram.getDamage());
        weapons.add(meleeWeapon);
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
