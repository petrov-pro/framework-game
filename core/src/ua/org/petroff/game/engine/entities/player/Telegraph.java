package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import ua.org.petroff.game.engine.entities.TelegramDescriber;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class Telegraph implements com.badlogic.gdx.ai.msg.Telegraph, TelegramProvider {

    private final GameResources gameResources;
    private final Player player;

    public Telegraph(Player player, GameResources gameResources) {
        this.player = player;
        this.gameResources = gameResources;
        gameResources.getMessageManger().addListener(this, TelegramDescriber.PLAYER_STATUS);
        gameResources.getMessageManger().addProvider(this, TelegramDescriber.PLAYER_STATUS);
    }

    @Override
    public boolean handleMessage(Telegram tlgrm) {
        //for future
        return true;
    }

    @Override
    public Object provideMessageInfo(int i, com.badlogic.gdx.ai.msg.Telegraph tlgrph) {
        switch (i) {
            case TelegramDescriber.PLAYER_STATUS:
                return new PlayerTelegram(player.getCurrentLive());
        }
        return null;
    }
}
