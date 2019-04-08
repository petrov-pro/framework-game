package ua.org.petroff.game.engine.entities.hud;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class Telegraph implements com.badlogic.gdx.ai.msg.Telegraph, TelegramProvider {

    private final GameResources gameResources;
    private final HUD model;

    public Telegraph(HUD model, GameResources gameResources) {
        this.model = model;
        this.gameResources = gameResources;
        gameResources.getMessageManger().addListener(this, 0);

    }

    @Override
    public boolean handleMessage(Telegram tlgrm) {
        return true;
    }

    @Override
    public Object provideMessageInfo(int i, com.badlogic.gdx.ai.msg.Telegraph tlgrph) {
        return null;
    }

}
