package ua.org.petroff.game.engine.entities.characters.base.creature;

import java.util.Map;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewNotifierInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.entities.characters.base.BaseGraphic;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.entities.characters.base.visual.effects.ViewHitInterface;

public class View extends ua.org.petroff.game.engine.entities.characters.base.View implements QueueDrawInterface, ViewNotifierInterface, GraphicQueueMemberInterface, ViewInterface {

    protected Creature model;
    protected ViewHitInterface blood;

    public View(
            Assets asset,
            GraphicResources graphicResources,
            Creature model,
            ua.org.petroff.game.engine.entities.characters.base.Graphic grpahic,
            ViewHitInterface blood
    ) {
        super(asset, graphicResources);
        graphic = grpahic;
        this.model = model;
        this.blood = blood;

    }

    @Override
    public void draw() {
        graphicFrame = getFrameName(model.getState(), model.getVector());
        frame = graphic.graphics
                .get(model.getSkinType())
                .getOrDefault(graphicFrame, graphic.getDefaultActionFrame());

        frameAnimation = frame.prepareGraphic();
        graphic.sprite.setRegion(frameAnimation);
        graphic.sprite.setCenter(model.getBody().getPosition().x, model.getBody().getPosition().y);
        graphic.sprite.draw(graphicResources.getSpriteBatch());
        blood.drawHit((model.getState() == StateInterface.State.HIT), model.getPlaceHit());
    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putSafe(ua.org.petroff.game.engine.entities.player.View.ZINDEX + 1, this);
    }

    @Override
    public void resetState(StateInterface.State action) {
        for (BaseGraphic graphic : graphic.graphics.get(model.getSkinType()).values()) {
            if (action == graphic.getActionType()) {
                graphic.setStateTime(0f);
            }
        }
    }
}
