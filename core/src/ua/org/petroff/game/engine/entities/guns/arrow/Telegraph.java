package ua.org.petroff.game.engine.entities.guns.arrow;

import com.badlogic.gdx.ai.msg.Telegram;
import ua.org.petroff.game.engine.entities.TelegramDescriber;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class Telegraph implements com.badlogic.gdx.ai.msg.Telegraph {

    private final GameResources gameResources;
    private final Arrow arrow;

    public Telegraph(Arrow arrow, GameResources gameResources) {
        this.arrow = arrow;
        this.gameResources = gameResources;
        gameResources.getMessageManger().addListener(this, TelegramDescriber.FIRE);
    }

    @Override
    public boolean handleMessage(Telegram tlgrm) {
        switch (tlgrm.message) {
            case TelegramDescriber.FIRE:
                ua.org.petroff.game.engine.entities.guns.arrow.Telegram telegram = (ua.org.petroff.game.engine.entities.guns.arrow.Telegram) tlgrm.extraInfo;
                arrow.shoot(telegram.getX(), telegram.getY(), telegram.getAngular(), telegram.getForceX(), telegram.getForceY());
                break;
        }
        return true;
    }

}
