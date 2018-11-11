package ua.org.petroff.game.engine.scenes;

import com.badlogic.gdx.Screen;
import ua.org.petroff.game.engine.util.Assets;

/**
 *
 * @author petroff
 */
public interface HandlerInterface {

    public Screen getScreen();

    public String getSceneName();

    public Assets getAssets();

    public void load();

    public void reLoad();

}
