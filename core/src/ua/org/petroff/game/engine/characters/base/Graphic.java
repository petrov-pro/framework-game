package ua.org.petroff.game.engine.characters.base;

import com.badlogic.gdx.graphics.g2d.Sprite;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

import java.util.HashMap;

public class Graphic {


    public final HashMap<String, HashMap<String, GraphicElement>> graphics = new HashMap();
    public Sprite sprite = new Sprite();

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
