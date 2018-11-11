package ua.org.petroff.game.engine.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import java.util.HashMap;
import ua.org.petroff.game.engine.scenes.mainmenu.MainMenuHandler;

public class ManagerScenes {

    private final Game game;
    private HashMap<String, HandlerInterface> scenesMap;
    private LoadingScreen loadingScreen;

    public ManagerScenes(Game game) {
        this.game = game;
        init();
    }

    public void load(String sceneName) {
        HandlerInterface scene = scenesMap.get(sceneName);
        scene.load();
        processLoad(scene);
    }

    public void reLoad(String sceneName) {
        HandlerInterface scene = scenesMap.get(sceneName);
        scene.reLoad();
        processLoad(scene);
    }

    private void processLoad(HandlerInterface scene) {
        if (scene.getAssets().isUploaded()) {
            game.setScreen(scene.getScreen());
        } else {
            loadingScreen.setReturnScene(scene);
            game.setScreen(loadingScreen);
        }
    }

    private void init() {
        scenesMap = new HashMap();
        loadingScreen = new LoadingScreen(this);
        scenesMap.put(MainMenuHandler.DESCRIPTOR, new MainMenuHandler(this));
    }

    public void setScreen(Screen screen) {
        game.setScreen(screen);
    }

}
