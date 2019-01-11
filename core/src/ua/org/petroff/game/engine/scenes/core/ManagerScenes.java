package ua.org.petroff.game.engine.scenes.core;

import ua.org.petroff.game.engine.scenes.Interface.ScreenLoadResourceInterface;
import ua.org.petroff.game.engine.scenes.Interface.ContainerInterface;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import java.util.HashMap;
import ua.org.petroff.game.engine.scenes.level1.Level1Container;
import ua.org.petroff.game.engine.scenes.mainmenu.MainMenuContainer;

public class ManagerScenes {

    private final Game game;
    private HashMap<String, ContainerInterface> scenesMap;
    private LoadingScreen loadingScreen;

    public ManagerScenes(Game game) {
        this.game = game;
        init();
    }

    public void load(String sceneName) {
        ContainerInterface scene = scenesMap.get(sceneName);
        loadingScreen.setReturnScene(scene);
        game.setScreen(loadingScreen);
    }

    public void quit() {
        Gdx.app.exit();
    }

    private void init() {
        scenesMap = new HashMap();
        loadingScreen = new LoadingScreen(this);
        scenesMap.put(MainMenuContainer.DESCRIPTOR, new MainMenuContainer(this));
        scenesMap.put(Level1Container.DESCRIPTOR, new Level1Container(this));
    }

    public void setScreen(Screen screen) {
        game.setScreen(screen);
    }

}
