package ua.org.petroff.game.engine.entities.equipments.health;

import ua.org.petroff.game.engine.entities.equipments.EquipmentUsing;
import ua.org.petroff.game.engine.entities.equipments.PotionInterface;
import ua.org.petroff.game.engine.entities.equipments.View;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class Health extends EquipmentUsing implements PotionInterface {

    public static final String DESCRIPTOR = "health";
    private final static int BODYWIDTH = 10;
    private final static int BODYHEIGHT = 14;

    private final int valueLife = 10;

    public Health(int x, int y, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        super(x, y, BODYWIDTH, BODYHEIGHT, asset, gameResources, graphicResources);
        view = new View(asset, graphicResources, this, DESCRIPTOR);
    }

    @Override
    public int getValue() {
        return valueLife;
    }

}
