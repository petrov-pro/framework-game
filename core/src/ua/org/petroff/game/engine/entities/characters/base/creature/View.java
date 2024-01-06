package ua.org.petroff.game.engine.entities.characters.base.creature;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Map;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewNotifierInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class View extends ua.org.petroff.game.engine.entities.characters.base.View implements QueueDrawInterface, ViewNotifierInterface, GraphicQueueMemberInterface, ViewInterface {

    protected Creature model;

    public View(
            Assets asset,
            GraphicResources graphicResources,
            Creature model,
            String regionName,
            float velocityFireAnimation
    ) {
        super(asset, graphicResources);
        graphic = new Graphic(asset, graphicResources, regionName, velocityFireAnimation);
        this.model = model;

    }

    @Override
    public void draw() {
        String graphicFrame = getFrameName(model.getAction(), model.getVector());
        frame = graphic.graphics.get(graphicFrame);
        TextureRegion frameAnimation = frame.prepareGraphic();
        graphic.sprite.setRegion(frameAnimation);
        graphic.sprite.setCenter(model.getBody().getPosition().x, model.getBody().getPosition().y);
        graphic.sprite.draw(graphicResources.getSpriteBatch());

    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putLast(this);
    }
}
