package ua.org.petroff.game.engine.entities.equipments.box;

import ua.org.petroff.game.engine.entities.equipments.Equipment;
import ua.org.petroff.game.engine.entities.map.SurfaceInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class Box extends Equipment implements SurfaceInterface{

    public static final String DESCRIPTOR = "box";

    public Box(int x, int y, int bodyWidth, int bodyHeight, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        super(x, y, bodyWidth, bodyHeight, asset, gameResources, graphicResources);
        view = new View(asset, graphicResources, this);
    }

}
