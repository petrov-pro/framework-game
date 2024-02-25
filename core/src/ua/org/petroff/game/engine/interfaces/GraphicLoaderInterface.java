package ua.org.petroff.game.engine.interfaces;

import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.HashMap;
import ua.org.petroff.game.engine.characters.base.GraphicElement;
import ua.org.petroff.game.engine.util.Assets;

public interface GraphicLoaderInterface {

    public HashMap<String, GraphicElement> loadAnimation(Sprite sprite, Assets asset, String regionName, float velocityFireAnimation);

}
