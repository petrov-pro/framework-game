package ua.org.petroff.game.engine.entities.characters.base;

import ua.org.petroff.game.engine.entities.Interfaces.ActionInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewNotifierInterface;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class View implements ViewNotifierInterface {

    public GraphicResources graphicResources;
    protected Assets asset;
    protected Graphic graphic; 

    protected BaseGraphic frame;

    public View(Assets asset, GraphicResources graphicResources) {
        this.asset = asset;
        this.graphicResources = graphicResources;
    }

    @Override
    public void resetState(ActionInterface.Type action) {
        for (BaseGraphic graphic : graphic.graphics.values()) {
            if (action == graphic.getActionType()) {
                graphic.setStateTime(0f);
            }
        }
    }

    public boolean isFinishAction(ActionInterface.Type action) {
        return !frame.isLoop() && frame.isFinish() && frame.getActionType() == action;
    }

    public static String getFrameName(ActionInterface.Type graphicType, WorldInterface.Vector vector) {
        return graphicType + "" + vector;
    }

}
