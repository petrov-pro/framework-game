package ua.org.petroff.game.engine.entities.player;

import ua.org.petroff.game.engine.entities.Interfaces.ActionInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Map;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.scenes.core.CameraBound;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class View extends ua.org.petroff.game.engine.entities.characters.base.View implements ViewInterface, QueueDrawInterface, GraphicQueueMemberInterface {

    private final Player model;
    private Player.PlayerSize currentPlayerSize;
    private final int zIndex = 2;

    private boolean drawGroundEffect = false;

    public View(Assets asset, GraphicResources graphicResources, Player model) {
        super(asset, graphicResources);
        this.model = model;
        graphic = new Graphic(asset, graphicResources, model);
    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putSafe(zIndex, this);
    }

    @Override
    public void draw() {
        changeSize();
        String graphicFrame = getFrameName(model.getAction(), model.getVector());
        Gdx.app.log("info", graphicFrame);
        frame = graphic.graphics.get(graphicFrame);
        TextureRegion frameAnimation = frame.prepareGraphic();
        graphic.sprite.setRegion(frameAnimation);
        graphic.sprite.setCenter(model.getPosition().x, model.getPosition().y);
        graphic.sprite.draw(graphicResources.getSpriteBatch());

        groundedEffect();
        ((CameraBound) graphicResources.getCamera()).positionSafe(model.getCameraNewPosition());
    }

    private void changeSize() {

        if (currentPlayerSize != model.getPlayerSize()) {
            if (model.getPlayerSize().equals(Player.PlayerSize.NORMAL)) {
                graphic.sprite.setScale(Settings.SCALE);
            } else {
                graphic.sprite.setScale(Settings.SCALE + 0.011f);
            }

        }
        currentPlayerSize = model.getPlayerSize();

    }

    private void groundedEffect() {
        if (currentPlayerSize.equals(Player.PlayerSize.GROWN)) {
            if (model.isGround() && drawGroundEffect) {
                ((Graphic) graphic).effect.start();
                float heidhtHalf = (graphic.sprite.getHeight() / 2) * Settings.SCALE;
                ((Graphic) graphic).effect.setPosition(model.getPosition().x, model.getPosition().y - heidhtHalf + 0.2f);
                drawGroundEffect = false;
            }
            if (!((Graphic) graphic).effect.isComplete()) {
                ((Graphic) graphic).effect.draw(graphicResources.getSpriteBatch(), Gdx.graphics.getDeltaTime());
            }
        }

    }

}
