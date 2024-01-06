package ua.org.petroff.game.engine.scenes.mainmenu;

import com.badlogic.gdx.Screen;
import ua.org.petroff.game.engine.scenes.Interface.ContainerInterface;
import ua.org.petroff.game.engine.scenes.Interface.ScreenLoadResourceInterface;
import ua.org.petroff.game.engine.scenes.core.ManagerScenes;
import ua.org.petroff.game.engine.util.Assets;

public class MainMenuContainer implements ContainerInterface {

    public static final String DESCRIPTOR = "MainMenu";

    private final MainMenuScreen screen;
    private final Assets assets;
    private final MainMenuController controller;

    public MainMenuContainer(ManagerScenes manageScene) {
        this.assets = new Assets();
        this.screen = new MainMenuScreen(assets);
        this.controller = new MainMenuController(manageScene, screen);
    }

    @Override
    public void start() {
        ((ScreenLoadResourceInterface) screen).load();
        controller.bindControl();
    }

    @Override
    public void loadShareResources() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
