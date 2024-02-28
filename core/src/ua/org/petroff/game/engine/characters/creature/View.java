package ua.org.petroff.game.engine.characters.creature;

import com.badlogic.gdx.math.Vector2;
import java.util.HashMap;
import java.util.Map;
import ua.org.petroff.game.engine.interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.interfaces.SkinInterface;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.interfaces.ViewInterface;
import ua.org.petroff.game.engine.interfaces.ViewNotifierInterface;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.characters.base.GraphicElement;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.characters.visual.effects.ViewHitInterface;

public class View extends ua.org.petroff.game.engine.characters.base.View implements QueueDrawInterface, ViewNotifierInterface, GraphicQueueMemberInterface, ViewInterface {

    protected Creature model;
    protected ViewHitInterface blood;
    private HashMap<String, GraphicElement> graphics;

    public View(
            Assets asset,
            GraphicResources graphicResources,
            Creature model,
            ua.org.petroff.game.engine.characters.base.Graphic grpahic,
            ViewHitInterface blood
    ) {
        super(asset, graphicResources);
        graphic = grpahic;
        this.model = model;
        this.blood = blood;

    }

    @Override
    public void draw() {
        graphics = graphic.graphics.get(prepareSkinType());
        frame = graphics
                .getOrDefault(
                        getFrameName(model.getState(), model.getVector()),
                        graphics.get(
                                getFrameName(StateInterface.State.MOVE, WorldInterface.Vector.STAY)
                        )
                );

        graphic.sprite.setRegion(frame.prepareGraphic());
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
        for (GraphicElement graphic : graphic.graphics.get(prepareSkinType()).values()) {
            if (action == graphic.getActionType()) {
                graphic.setStateTime(0f);
            }
        }
    }

    protected String prepareSkinType() {
        return model.getSkinType().toString();
    }
}
