package ua.org.petroff.game.engine.entities.Interfaces;

import java.util.Map;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public interface GraphicQueueMemberInterface {

    public Map<Integer, QueueDrawInterface> prepareDraw(Map<Integer, QueueDrawInterface> queueDraw);


}
