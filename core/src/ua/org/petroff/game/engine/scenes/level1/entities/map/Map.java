
package ua.org.petroff.game.engine.scenes.level1.entities.map;

import ua.org.petroff.game.engine.entities.EntityInterface;
import ua.org.petroff.game.engine.entities.ViewInterface;
import ua.org.petroff.game.engine.util.Assets;

public class Map implements EntityInterface {

    public static final String DESCRIPTOR = "map level 1";
    public View view;

    public Map(Assets asset) {
        view = new View(asset);
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

}
