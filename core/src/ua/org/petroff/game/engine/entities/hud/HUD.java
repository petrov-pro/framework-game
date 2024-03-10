package ua.org.petroff.game.engine.entities.hud;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import ua.org.petroff.game.engine.characters.creature.CreatureInterface;
import ua.org.petroff.game.engine.entities.player.Player;
import ua.org.petroff.game.engine.interfaces.EntityInterface;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.interfaces.SupplierViewInterface;
import ua.org.petroff.game.engine.interfaces.ViewInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class HUD implements EntityInterface, SupplierViewInterface, Telegraph {

    public static final String DESCRIPTOR = "hud";
    public Integer currentLive = 0;

    private final View view;

    public HUD(Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        view = new View(asset, graphicResources, this);
        gameResources.getMessageManger().addListeners(this, StateInterface.State.PLAYER_STATUS.telegramNumber, StateInterface.State.PLAYER_DEAD.telegramNumber);
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    @Override
    public void update() {

        if (currentLive < 0) {
            currentLive = 0;
        }
    }

    @Override
    public boolean handleMessage(Telegram tlgrm) {
        switch (StateInterface.State.getStateBy(tlgrm.message)) {
            case PLAYER_DEAD:
                currentLive = 0;
                break;
            case PLAYER_STATUS:
                Player player = ((Player) tlgrm.extraInfo);
                currentLive = player.getCurrentLive();
                view.drawSlots(player.getSlotWeapons(), player.getWeapon());
                break;
        }

        return true;
    }

}
