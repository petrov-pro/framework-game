package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.scenes.core.CameraBound;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class View implements QueueDrawInterface {

    public enum GraphicType {
        MOVELEFT, MOVERIGHT, STAY, JUMPLEFT, JUMPRIGHT, STAYJUMP, DIED
    };
    public View.GraphicType graphicFrame;
    public boolean isLoopAnimation = true;
    public float speedAnimation = 1f;
    public GraphicResources graphicResources;

    private final Player model;
    private float stateTime = 0;
    private Graphic graphic;

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
        if (graphicFrame == GraphicType.STAY) {
            graphicResources.getSpriteBatch().draw(graphic.player, model.getPosition().x, model.getPosition().y, 2, 2);
        } else {
            stateTime += Gdx.graphics.getDeltaTime();
            Object graphic = this.graphic.graphics.get(graphicFrame);
            graphicResources.getSpriteBatch()
                    .draw((TextureRegion) ((Animation) graphic).getKeyFrame(stateTime * speedAnimation, isLoopAnimation),
                            model.getPosition().x, model.getPosition().y, 2, 2);
        }
        ((CameraBound) graphicResources.getCamera()).positionSafe(model.getCameraNewPosition());
    }

    private void handlerGrpahicFrame() {

        if (model.isDie()) {
            graphicFrame = GraphicType.DIED;
            isLoopAnimation = false;
            speedAnimation = 1f;
            return;
        }

        if (model.isGround()) {
            isLoopAnimation = true;
            speedAnimation = 1f;
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
            speedAnimation = 0.3f;
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
