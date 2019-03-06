package ua.org.petroff.game.engine.entities;

import java.util.Map;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public interface GraphicQueueMemberInterface {

    public Map<Integer, QueueDrawInterface> prepareDraw(GraphicResources graphicResources, Map<Integer, QueueDrawInterface> queueDraw);

    public void share(GraphicResources graphicResources);

}
