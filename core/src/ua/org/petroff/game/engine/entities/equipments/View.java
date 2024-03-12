package ua.org.petroff.game.engine.entities.equipments;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import java.util.Map;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class View extends ua.org.petroff.game.engine.characters.base.View implements ViewInterface, GraphicQueueMemberInterface {

    private Sprite sprite;
    private Equipment model;
    private Animation equipmentAnimation;
    private float stateTime;

    public View(Assets asset, GraphicResources graphicResources, Equipment model, String regionName) {
        super(asset, graphicResources);
        this.model = model;
        init(regionName);
    }

    private void init(String regionName) {
        Array<TextureAtlas.AtlasRegion> equipmentAtals = asset.getAtlas().findRegions(regionName);

        sprite = new Sprite();
        sprite.setBounds(0, 0, 32, 32);
        sprite.setScale(Settings.SCALE);
        sprite.setOriginCenter();
        equipmentAnimation = new Animation(0.1f, equipmentAtals);
    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putLast(new QueueDrawInterface() {
            @Override
            public void draw() {
                if (model.getBody() == null) {
                    return;
                }
                stateTime += Gdx.graphics.getDeltaTime();
                sprite.setRegion((TextureRegion) equipmentAnimation.getKeyFrame(stateTime, true));
                sprite.setCenter(model.getBody().getPosition().x, model.getBody().getPosition().y);
                sprite.setRotation((float) Math.toDegrees(model.getBody().getAngle()));
                sprite.draw(graphicResources.getSpriteBatch());
            }

        });
    }
}
