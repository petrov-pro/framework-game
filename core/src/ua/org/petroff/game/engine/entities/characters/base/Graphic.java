package ua.org.petroff.game.engine.entities.characters.base;

import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.HashMap;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class Graphic {

    /*
    900x1000 = bow
    900-1100x1000 = sword
    
    */
    public final HashMap<String, BaseGraphic> graphics = new HashMap();
    public Sprite sprite;

    protected Assets asset;
    protected GraphicResources graphicResources;
    protected String regionName;
    protected Static defaultActionFrame;

    public Graphic(Assets asset, GraphicResources graphicResources) {
        this.asset = asset;
        this.graphicResources = graphicResources;
    }

    public Static getDefaultActionFrame() {
        return defaultActionFrame;
    }

}
