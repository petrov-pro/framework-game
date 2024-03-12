package ua.org.petroff.game.engine.entities.equipments;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import ua.org.petroff.game.engine.entities.map.SurfaceInterface;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.Timer;

public class EquipmentUsing extends Equipment implements SurfaceInterface, Telegraph, EquipmentInterface {

    private boolean isUsed = false;

    public EquipmentUsing(int x, int y, int bodyWidth, int bodyHeight, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        super(x, y, bodyWidth, bodyHeight, asset, gameResources, graphicResources);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == StateInterface.State.EQUIPMENT.telegramNumber && !isUsed) {
            isUsed = true;
            bodyEquipment.setSensor(true);
            body.applyForceToCenter(0, 100f, true);
        }

        return true;
    }

    @Override
    public void update() {
        super.update();
        if (isUsed && body != null && Timer.run(this.toString(), 1)) {
            gameResources.getWorld().destroyBody(body);
            body = null;
        }
    }

    @Override
    public boolean isUsed() {
        return isUsed;
    }

}
