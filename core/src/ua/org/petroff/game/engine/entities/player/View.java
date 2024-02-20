package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import java.util.Map;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.scenes.core.CameraBound;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.characters.base.visual.effects.Blood;

public class View extends ua.org.petroff.game.engine.entities.characters.base.creature.View implements ViewInterface, QueueDrawInterface, GraphicQueueMemberInterface {

    private Player.PlayerSize currentPlayerSize;
    public static final int ZINDEX = 50;

    private boolean canDrawGroundEffect = true;

    public View(Assets asset, GraphicResources graphicResources, Player model) {
        super(
                asset,
                graphicResources,
                model,
                new Graphic(asset, graphicResources, model),
                new Blood(asset, graphicResources)
        );
    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putSafe(ZINDEX, this);
    }

    @Override
    public void draw() {
        changeSize();
        super.draw();
        groundedEffect();
        ((CameraBound) graphicResources.getCamera()).positionSafe(((Player) model).getCameraNewPosition());
    }

    private void changeSize() {
        if (currentPlayerSize != ((Player) model).getPlayerSize()) {
            if (((Player) model).getPlayerSize().equals(Player.PlayerSize.NORMAL)) {
                graphic.sprite.setScale(Settings.SCALE);
            } else {
                graphic.sprite.setScale(Settings.SCALE + 0.011f);
            }

        }
        currentPlayerSize = ((Player) model).getPlayerSize();

    }

    private void groundedEffect() {
        if (!currentPlayerSize.equals(Player.PlayerSize.GROWN)) {
            return;
        }

        if (model.getState() == StateInterface.State.JUMP && ((Graphic) graphic).effect.isComplete()) {
            canDrawGroundEffect = true;

            return;
        }

        if (model.isGrounded() && canDrawGroundEffect && ((Graphic) graphic).effect.isComplete()) {
            ((Graphic) graphic).effect.start();
            float heidhtHalf = (graphic.sprite.getHeight() / 2) * Settings.SCALE;
            ((Graphic) graphic).effect.setPosition(model.getPosition().x, model.getPosition().y - heidhtHalf + 0.2f);
            canDrawGroundEffect = false;
        }

        if (!((Graphic) graphic).effect.isComplete()) {
            ((Graphic) graphic).effect.draw(graphicResources.getSpriteBatch(), Gdx.graphics.getDeltaTime());
        }

    }

}
