package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Screen;
import ua.org.petroff.game.engine.scenes.Interface.ContainerInterface;
import ua.org.petroff.game.engine.scenes.Interface.ControllerInterface;
import ua.org.petroff.game.engine.scenes.core.ManagerScenes;
import ua.org.petroff.game.engine.util.Assets;

public class Level1Container implements ContainerInterface {

    public static final String DESCRIPTOR = "Level1";

    private Level1Screen screen;
    private Assets assets;
    private final ManagerScenes manageScene;
    private Level1Controller controller;

    public Level1Container(ManagerScenes manageScene) {
        this.manageScene = manageScene;
    }

    @Override
    public void load() {
        this.assets = new Assets();
        this.screen = new Level1Screen(assets);
        this.controller = new Level1Controller(manageScene, screen);

    }

    @Override
    public Assets getAssets() {
        return this.assets;
    }

    @Override
    public String getSceneName() {
        return "UnderWorld";
    }

    @Override
    public Screen getView() {
        return (Screen) this.screen;
    }

    @Override
    public ControllerInterface getController() {
        return this.controller;
    }

}
