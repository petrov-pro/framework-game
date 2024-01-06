package ua.org.petroff.game.engine.entities.hud;

import com.badlogic.gdx.ai.msg.Telegram;
import ua.org.petroff.game.engine.entities.TelegramDescriber;
import ua.org.petroff.game.engine.entities.player.PlayerTelegram;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class Telegraph implements com.badlogic.gdx.ai.msg.Telegraph {

    private final HUD hud;

    public Telegraph(HUD model, GameResources gameResources) {
        this.hud = model;
        gameResources.getMessageManger().addListeners(this, TelegramDescriber.PLAYER_STATUS, TelegramDescriber.PLAYER_DEAD);
    }

    @Override
    public boolean handleMessage(Telegram tlgrm) {
        switch (tlgrm.message) {
            case TelegramDescriber.PLAYER_DEAD:
                hud.playerDied();
                break;
            case TelegramDescriber.PLAYER_STATUS:
                hud.updatePlayerStatus((PlayerTelegram) tlgrm.extraInfo);
                break;
        }

        return true;
    }
}
