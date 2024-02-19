package ua.org.petroff.game.engine.entities.cloud;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import java.util.Map;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class View extends ua.org.petroff.game.engine.entities.characters.base.View implements ViewInterface, GraphicQueueMemberInterface {

    private final CloudFactory model;
    private Sprite sprite;
    private Array<TextureAtlas.AtlasRegion> cloudAtals;

    public View(Assets asset, GraphicResources graphicResources, CloudFactory model) {
        super(asset, graphicResources);
        this.model = model;
        init();
    }

    private void init() {
        cloudAtals = asset.getAtlas().findRegions("cloud");
        sprite = new Sprite(cloudAtals.first());
        sprite.setScale(Settings.SCALE / 1.5f);
        sprite.setOriginCenter();
    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putSafe(1, new QueueDrawInterface() {
            @Override
            public void draw() {
                if (!model.getClouds().isEmpty()) {
                    for (Cloud cloud : model.getClouds()) {
                        sprite.setRegion(cloudAtals.get(cloud.getType()));
                        sprite.setCenter(cloud.getBody().getPosition().x, cloud.getBody().getPosition().y);
                        sprite.draw(graphicResources.getSpriteBatch());
                    }
                }
            }

        });
    }
}
