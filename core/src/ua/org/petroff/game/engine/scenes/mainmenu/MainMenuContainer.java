package ua.org.petroff.game.engine.scenes.mainmenu;

import com.badlogic.gdx.Screen;
import ua.org.petroff.game.engine.scenes.Interface.ContainerInterface;
import ua.org.petroff.game.engine.scenes.Interface.ControllerInterface;
import ua.org.petroff.game.engine.scenes.Interface.ScreenLoadResourceInterface;
import ua.org.petroff.game.engine.scenes.core.ManagerScenes;
import ua.org.petroff.game.engine.util.Assets;

public class MainMenuContainer implements ContainerInterface {

    public static final String DESCRIPTOR = "MainMenu";

    private MainMenuScreen screen;
    private Assets assets;
    private final ManagerScenes manageScene;
    private MainMenuController controller;

    public MainMenuContainer(ManagerScenes manageScene) {
        this.manageScene = manageScene;
    }

    @Override
    public void load() {
        ((ScreenLoadResourceInterface) screen).load();
        controller.bindControl();
    }

    @Override
    public void init() {
        this.assets = new Assets();
        this.screen = new MainMenuScreen(assets);
        this.controller = new MainMenuController(manageScene, screen);

    }

    @Override
    public Assets getAssets() {
        return this.assets;
    }

    @Override
    public String getSceneName() {
        return "Super Game";
    }

    @Override
    public Screen getView() {
        return (Screen) this.screen;
    }

}
