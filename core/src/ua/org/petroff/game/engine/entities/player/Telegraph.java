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
        gameResources.getMessageManger().addListener(this, 0);

    }

    @Override
    public boolean handleMessage(Telegram tlgrm) {
        switch (tlgrm.message) {
            case TelegramDescriber.DEAD:
                player.died();
                break;
        }
        return true;
    }

    @Override
    public Object provideMessageInfo(int i, com.badlogic.gdx.ai.msg.Telegraph tlgrph) {
        return null;
    }

}
