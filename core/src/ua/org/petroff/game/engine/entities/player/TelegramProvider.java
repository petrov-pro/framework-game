package ua.org.petroff.game.engine.entities.player;

import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class TelegramProvider implements com.badlogic.gdx.ai.msg.TelegramProvider {

    private final Player player;

    public TelegramProvider(Player player, GameResources gameResources) {
        this.player = player;
        gameResources.getMessageManger().addProvider(this, StateInterface.State.PLAYER_STATUS.telegramNumber);
    }

    @Override
    public Object provideMessageInfo(int i, com.badlogic.gdx.ai.msg.Telegraph tlgrph) {
        switch (StateInterface.State.getStateBy(i)) {
            case PLAYER_STATUS:
                return player;
        }
        return null;
    }
}
