package ua.org.petroff.game.engine.entities.hud;

import com.badlogic.gdx.Gdx;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.player.Player;
import ua.org.petroff.game.engine.entities.player.PlayerTelegram;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.util.Assets;

public class HUD implements EntityInterface {

    public static final String DESCRIPTOR = "HUD";
    public Integer worldTimer;
    public float timeCount;
    public Integer score;
    public Integer currentLive;

    private final View view;
    private final Assets asset;
    private final int zIndex = 4;

    public HUD(Assets asset) {
        view = new View(asset, this);
        this.asset = asset;
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    @Override
    public void init(GameResources gameResources) {
        worldTimer = 0;
        timeCount = 0;
        score = 0;
        currentLive = 0;
        Telegraph telegraph = new Telegraph(this, gameResources);
        gameResources.getMessageManger().addTelegraph(DESCRIPTOR, telegraph);
    }

    @Override
    public void update() {
        timeCount += Gdx.graphics.getDeltaTime();
        if (timeCount >= 1) {
            worldTimer++;
            timeCount = 0;
        }
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    @Override
    public EntityInterface prepareModel() {
        return this;
    }

    public void playerDied() {
        currentLive = 0;
    }
    
    public void updatePlayerStatus(PlayerTelegram playerTelegram) {
        currentLive = playerTelegram.getCurrentLive();
    }

}
