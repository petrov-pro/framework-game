package ua.org.petroff.game.engine.scenes.mainmenu;

import com.badlogic.gdx.Screen;
import ua.org.petroff.game.engine.scenes.ManagerScenes;
import ua.org.petroff.game.engine.scenes.HandlerInterface;
import ua.org.petroff.game.engine.scenes.ScreenLoadResourceInterface;
import ua.org.petroff.game.engine.util.Assets;

public class MainMenuHandler implements HandlerInterface {

    public static final String DESCRIPTOR = "MainMenu";

    private Screen screen;
    private Assets assets;
    private final ManagerScenes manageScene;
    private MainMenuController controller;

    public MainMenuHandler(ManagerScenes manageScene) {
        this.manageScene = manageScene;
    }

    @Override
    public Screen getScreen() {
        return screen;
    }

    @Override
    public void load() {
        if (assets == null && screen == null) {
            processLoading();
        }
    }

    @Override
    public void reLoad() {
        processLoading();
    }

    private void processLoading() {
        assets = new Assets();
        controller = new MainMenuController(manageScene);
        screen = new MainMenuScreen(assets,controller);
        ((ScreenLoadResourceInterface) screen).load();
    }

    @Override
    public Assets getAssets() {
        return assets;
    }

    @Override
    public String getSceneName() {
        return "Super Game";
    }

}
