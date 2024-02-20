package ua.org.petroff.game.engine.entities.characters.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;

public class View implements ViewInterface {

    public GraphicResources graphicResources;
    protected Assets asset;
    protected Graphic graphic;
    protected String graphicFrame;
    protected TextureRegion frameAnimation;

    protected BaseGraphic frame;

    public View(Assets asset, GraphicResources graphicResources) {
        this.asset = asset;
        this.graphicResources = graphicResources;
    }

    public boolean isFinishAction(StateInterface.State action) {
        return frame != null && !frame.isLoop() && frame.isFinish() && frame.getActionType() == action;
    }

    public static String getFrameName(StateInterface.State graphicType, WorldInterface.Vector vector) {
        return graphicType + "" + vector;
    }

}
