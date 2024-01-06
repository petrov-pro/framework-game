package ua.org.petroff.game.engine.entities.hud;

import com.badlogic.gdx.Gdx;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.player.PlayerTelegram;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class HUD implements EntityInterface {

    public static final String DESCRIPTOR = "HUD";
    public Integer worldTimer = 0;
    public float timeCount = 0;
    public Integer score = 0;
    public Integer currentLive = 0;

    private final View view;

    public HUD(Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        view = new View(asset, graphicResources, this);
        new Telegraph(this, gameResources);
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    @Override
    public void update() {
        timeCount += Gdx.graphics.getDeltaTime();
        if (timeCount >= 1) {
            worldTimer++;
            timeCount = 0;
        }
    }

    public void playerDied() {
        currentLive = 0;
    }

    public void updatePlayerStatus(PlayerTelegram playerTelegram) {
        currentLive = playerTelegram.getCurrentLive();
    }

}
