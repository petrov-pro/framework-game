package ua.org.petroff.game.engine.entities.hud;

import com.badlogic.gdx.ai.msg.Telegram;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.player.PlayerTelegram;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class Telegraph implements com.badlogic.gdx.ai.msg.Telegraph {

    private final HUD hud;

    public Telegraph(HUD model, GameResources gameResources) {
        this.hud = model;
        gameResources.getMessageManger().addListeners(this, StateInterface.State.PLAYER_STATUS.telegramNumber, StateInterface.State.PLAYER_DEAD.telegramNumber);
    }

    @Override
    public boolean handleMessage(Telegram tlgrm) {
        switch (StateInterface.State.getStateBy(tlgrm.message)) {
            case PLAYER_DEAD:
                hud.playerDied();
                break;
            case PLAYER_STATUS:
                hud.updatePlayerStatus((PlayerTelegram) tlgrm.extraInfo);
                break;
        }

        return true;
    }
}
