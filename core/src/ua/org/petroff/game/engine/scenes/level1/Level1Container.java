package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Screen;
import java.util.HashMap;
import ua.org.petroff.game.engine.entities.EntityInterface;
import ua.org.petroff.game.engine.entities.MoveEntityInterface;
import ua.org.petroff.game.engine.entities.player.Player;
import ua.org.petroff.game.engine.scenes.Interface.ContainerInterface;
import ua.org.petroff.game.engine.scenes.Interface.ScreenLoadResourceInterface;
import ua.org.petroff.game.engine.scenes.core.ManagerScenes;
import ua.org.petroff.game.engine.scenes.level1.entities.map.Map;
import ua.org.petroff.game.engine.util.Assets;

public class Level1Container implements ContainerInterface {

    public static final String DESCRIPTOR = "Level1";

    private Level1Screen screen;
    private Assets assets;
    private final ManagerScenes manageScene;
    private Level1Controller controller;
    private HashMap<String, EntityInterface> entities;

    public Level1Container(ManagerScenes manageScene) {
        this.manageScene = manageScene;

    }

    @Override
    public void load() {
        entities = new HashMap<>();
        assets = new Assets();
        setEntities();
        screen = new Level1Screen();
        controller = new Level1Controller(manageScene, screen, (MoveEntityInterface) entities.get(Player.DESCRIPTOR));
        ((ScreenLoadResourceInterface) screen).load();
        loadEntitiesResources();
        screen.setEntities(entities);

    }

    private void setEntities() {
        entities.put(Map.DESCRIPTOR, new Map(this.assets));
        entities.put(Player.DESCRIPTOR, new Player(this.assets));

    }

    @Override
    public void init() {
        initEntitiesResources();
        ((ScreenLoadResourceInterface) screen).init();
        controller.bindControl();
    }

    private void loadEntitiesResources() {
        for (EntityInterface entity : entities.values()) {
            entity.getView().loadResources();
        }
    }

    private void initEntitiesResources() {
        for (EntityInterface entity : entities.values()) {
            entity.init();
            entity.getView().init();
        }
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

}
