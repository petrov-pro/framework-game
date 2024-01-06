package ua.org.petroff.game.engine.entities.guns.arrow;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import java.util.Map;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class View implements ViewInterface, GraphicQueueMemberInterface {

    public String graphicFrame;
    public GraphicResources graphicResources;

    private final Arrow model;
    private final Assets asset;
    private Sprite arrowRightSprite;
    private Sprite arrowLeftSprite;

    public View(Assets asset, GraphicResources graphicResources, Arrow model) {
        this.asset = asset;
        this.model = model;
        this.graphicResources = graphicResources;
        init();
    }

    private void init() {
        TextureAtlas.AtlasRegion arrowAtals = asset.getAtlas().findRegion("arrow");
        arrowRightSprite = new Sprite(arrowAtals, 0, 0, 32, 3);
        arrowRightSprite.setScale(Settings.SCALE);
        arrowRightSprite.setOriginCenter();
        arrowLeftSprite = new Sprite(arrowAtals, 0, 0, 32, 3);
        arrowLeftSprite.setScale(Settings.SCALE);
        arrowLeftSprite.setOriginCenter();
        arrowLeftSprite.flip(true, false);

    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putLast(new QueueDrawInterface() {
            @Override
            public void draw() {
                if (!model.getArrows().isEmpty()) {
                    Sprite sprite;
                    for (Body body : model.getArrows()) {
                        if ((WorldInterface.Vector) body.getUserData() == WorldInterface.Vector.RIGHT) {
                            sprite = arrowRightSprite;
                        } else {
                            sprite = arrowLeftSprite;
                        }
                        sprite.setCenter(body.getPosition().x, body.getPosition().y);
                        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
                        sprite.draw(graphicResources.getSpriteBatch());
                    }
                }
            }

        });
    }
}
