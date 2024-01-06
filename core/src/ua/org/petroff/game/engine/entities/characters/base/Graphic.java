package ua.org.petroff.game.engine.entities.characters.base;

import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.HashMap;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class Graphic {

    public final HashMap<String, BaseGraphic> graphics = new HashMap();
    public Sprite sprite;

    protected Assets asset;
    protected GraphicResources graphicResources;
    protected String regionName;

    public Graphic(Assets asset, GraphicResources graphicResources) {
        this.asset = asset;
        this.graphicResources = graphicResources;
    }

}
