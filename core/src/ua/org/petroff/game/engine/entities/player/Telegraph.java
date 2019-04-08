package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import ua.org.petroff.game.engine.entities.TelegramDescriber;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class Telegraph implements com.badlogic.gdx.ai.msg.Telegraph, TelegramProvider {

    private final GameResources gameResources;
    private final Player player;
    private final PlayerTelegram playerTelegram;

    public Telegraph(Player player, GameResources gameResources) {
        this.player = player;
        this.gameResources = gameResources;
        gameResources.getMessageManger().addProvider(this, TelegramDescriber.PLAYER_STATUS);
        playerTelegram = new PlayerTelegram(player.getCurrentLive());
    }

    @Override
    public boolean handleMessage(Telegram tlgrm) {
        switch (tlgrm.message) {
            case TelegramDescriber.DEAD:
                player.died();
                gameResources.getMessageManger().dispatchMessage(TelegramDescriber.PLAYER_DEAD);
                break;
        }
        return true;
    }

    @Override
    public Object provideMessageInfo(int i, com.badlogic.gdx.ai.msg.Telegraph tlgrph) {
        switch (i) {
            case TelegramDescriber.PLAYER_STATUS:
                return playerTelegram;
        }
        return null;
    }
}
