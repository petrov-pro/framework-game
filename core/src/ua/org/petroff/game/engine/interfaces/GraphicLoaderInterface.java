package ua.org.petroff.game.engine.interfaces;

import com.badlogic.gdx.graphics.g2d.Sprite;
import ua.org.petroff.game.engine.characters.base.GraphicElement;
import ua.org.petroff.game.engine.util.Assets;

import java.util.HashMap;

public interface GraphicLoaderInterface {

    public HashMap<String, GraphicElement> loadAnimation(Sprite sprite, Assets asset, String regionName, float velocityFireAnimation);

}
