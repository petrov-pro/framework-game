package ua.org.petroff.game.engine.entities.equipments.shield;

import ua.org.petroff.game.engine.entities.equipments.View;
import ua.org.petroff.game.engine.entities.equipments.EquipmentUsing;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class Shield extends EquipmentUsing {

    public static final String DESCRIPTOR = "shield";

    private final static int BODYWIDTH = 14;
    private final static int BODYHEIGHT = 14;

    public Shield(int x, int y, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        super(x, y, BODYWIDTH, BODYHEIGHT, asset, gameResources, graphicResources);
        view = new View(asset, graphicResources, this, DESCRIPTOR);
    }

}
