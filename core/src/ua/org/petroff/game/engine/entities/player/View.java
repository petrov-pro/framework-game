package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.scenes.core.CameraBound;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.MapResolver;

public class View implements QueueDrawInterface {

    public enum GraphicType {
        MOVELEFT, MOVERIGHT, STAY, JUMPLEFT, JUMPRIGHT, STAYJUMP, DIED
    };
    public View.GraphicType graphicFrame;
    private boolean isLoopAnimation = true;
    public GraphicResources graphicResources;

    private final Player model;
    private float stateTime = 0;
    private Graphic graphic;
    private float sizeX;
    private float sizeY;
    private Player.PlayerSize currentPlayerSize;

    public View(Player model) {
        this.model = model;
        graphicFrame = GraphicType.STAY;
    }

    public void setGraphic(Graphic graphic) {
        this.graphic = graphic;
    }

    @Override
    public void draw() {

        handlerGrpahicFrame();
        Object frame = graphic.graphics.get(graphicFrame);
        if (graphicFrame == GraphicType.STAY) {
            graphic.sprite.setRegion((TextureRegion) frame);
        } else {
            stateTime += Gdx.graphics.getDeltaTime();
            TextureRegion frameAnimation = (TextureRegion) ((Animation) frame).getKeyFrame(stateTime, isLoopAnimation);
            graphic.sprite.setRegion(frameAnimation);
        }

        graphic.sprite.setCenter(model.getPosition().x, model.getPosition().y);
        graphic.sprite.draw(graphicResources.getSpriteBatch());
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

    private void handlerGrpahicFrame() {

        changeSize();

        if (model.isDie()) {
            graphicFrame = GraphicType.DIED;
            isLoopAnimation = false;
            return;
        }

        if (model.isGround()) {
            isLoopAnimation = true;
            switch (model.getVector()) {
                case RIGHT:
                    graphicFrame = GraphicType.MOVERIGHT;
                    break;
                case LEFT:
                    graphicFrame = GraphicType.MOVELEFT;
                    break;
                default:
                    graphicFrame = GraphicType.STAY;
                    break;
            }
        } else {
            isLoopAnimation = false;
            switch (model.getVector()) {
                case RIGHT:
                    graphicFrame = GraphicType.JUMPRIGHT;
                    break;
                case LEFT:
                    graphicFrame = GraphicType.JUMPLEFT;
                    break;
                default:
                    graphicFrame = GraphicType.STAYJUMP;
                    break;
            }
        }
    }

    public void changeState() {
        stateTime = 0;
    }

}
