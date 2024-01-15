package ua.org.petroff.game.engine.entities.characters.base.creature;

import com.badlogic.gdx.Gdx;
import java.util.Map;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
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
        graphicFrame = getFrameName(model.getState(), model.getVector());
        frame = graphic.graphics.getOrDefault(graphicFrame, graphic.getDefaultActionFrame());
        frameAnimation = frame.prepareGraphic();
        graphic.sprite.setRegion(frameAnimation);
        graphic.sprite.setCenter(model.getBody().getPosition().x, model.getBody().getPosition().y);
        graphic.sprite.draw(graphicResources.getSpriteBatch());
        drawHit();

    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putLast(this);
    }

    private void drawHit() {
        if (model.getState() == StateInterface.State.HIT && ((Graphic) graphic).effectBlood.isComplete()) {
            ((Graphic) graphic).effectBlood.setPosition(model.getPlaceHit().x, model.getPlaceHit().y);
            ((Graphic) graphic).effectBlood.start();
        }

        if (!((Graphic) graphic).effectBlood.isComplete()) {
            ((Graphic) graphic).effectBlood.draw(graphicResources.getSpriteBatch(), Gdx.graphics.getDeltaTime());
        }
    }
}
