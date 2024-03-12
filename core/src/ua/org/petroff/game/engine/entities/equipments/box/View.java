package ua.org.petroff.game.engine.entities.equipments.box;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.util.Map;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.entities.equipments.Equipment;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class View extends ua.org.petroff.game.engine.characters.base.View implements ViewInterface, GraphicQueueMemberInterface {

    private Sprite sprite;
    private Equipment model;

    public View(Assets asset, GraphicResources graphicResources, Equipment model) {
        super(asset, graphicResources);
        this.model = model;
        init();
    }

    private void init() {
        TextureAtlas.AtlasRegion equipmentAtals = asset.getAtlas().findRegion(Box.DESCRIPTOR);
        sprite = new Sprite(equipmentAtals, 0, 0, 32, 32);
        sprite.setScale(Settings.SCALE);
        sprite.setOriginCenter();
    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putLast(new QueueDrawInterface() {
            @Override
            public void draw() {
                sprite.setCenter(model.getBody().getPosition().x, model.getBody().getPosition().y);
                sprite.setRotation((float) Math.toDegrees(model.getBody().getAngle()));
                sprite.draw(graphicResources.getSpriteBatch());
            }

        });
    }
}
