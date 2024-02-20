package ua.org.petroff.game.engine.entities.weapons.arrow;

import com.badlogic.gdx.ai.msg.Telegram;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class Telegraph implements com.badlogic.gdx.ai.msg.Telegraph {

    private final GameResources gameResources;
    private final ArrowFactory arrow;

    public Telegraph(ArrowFactory arrow, GameResources gameResources) {
        this.arrow = arrow;
        this.gameResources = gameResources;
        gameResources.getMessageManger().addListener(this, StateInterface.State.FIRE.telegramNumber);
    }

    @Override
    public boolean handleMessage(Telegram tlgrm) {
        switch (StateInterface.State.getStateBy(tlgrm.message)) {
            case FIRE:
                ua.org.petroff.game.engine.entities.weapons.arrow.Telegram telegram = (ua.org.petroff.game.engine.entities.weapons.arrow.Telegram) tlgrm.extraInfo;
                arrow.shoot(telegram.getX(), telegram.getY(), telegram.getAngular(), telegram.getForceX(), telegram.getForceY());
                break;
        }
        return true;
    }

}
