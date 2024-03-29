package ua.org.petroff.game.engine.entities.weapons.ranged;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.util.Map;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.interfaces.ViewInterface;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class View extends ua.org.petroff.game.engine.characters.base.View implements ViewInterface, GraphicQueueMemberInterface {

    private final RangedWeaponFactory model;
    private Sprite arrowRightSprite;
    private Sprite arrowLeftSprite;
    private Sprite sprite;

    public View(Assets asset, GraphicResources graphicResources, RangedWeaponFactory model) {
        super(asset, graphicResources);
        this.model = model;
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
                    for (Arrow arrow : model.getArrows()) {
                        if (arrow.getVector() == WorldInterface.Vector.RIGHT) {
                            sprite = arrowRightSprite;
                        } else {
                            sprite = arrowLeftSprite;
                        }
                        sprite.setCenter(arrow.getBody().getPosition().x, arrow.getBody().getPosition().y);
                        sprite.setRotation((float) Math.toDegrees(arrow.getBody().getAngle()));
                        sprite.draw(graphicResources.getSpriteBatch());
                    }
                }
            }

        });
    }
}
