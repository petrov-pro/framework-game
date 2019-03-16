package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Screen;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import ua.org.petroff.game.engine.entities.EntityInterface;
import ua.org.petroff.game.engine.entities.MoveEntityInterface;
import ua.org.petroff.game.engine.entities.player.Player;
import ua.org.petroff.game.engine.scenes.Interface.ContainerInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
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
    private GraphicResources graphicResources;
    private GameResources gameResources;

    public Level1Container(ManagerScenes manageScene) {
        this.manageScene = manageScene;
    }

    @Override
    public void load() {
        graphicResources = new GraphicResources();
        gameResources = new GameResources();
        entities = new HashMap<>();
        assets = new Assets();
        loadEntities();
        screen = new Level1Screen(entities, graphicResources);
        controller = new Level1Controller(manageScene, screen, (MoveEntityInterface) entities.get(Player.DESCRIPTOR));
        loadEntitiesResources();
    }

    private void loadEntities() {
        entities.put(Player.DESCRIPTOR, new Player(this.assets));
        entities.put(Map.DESCRIPTOR, new Map(this.assets));

    }

    @Override
    public void init() {
        initEntitiesResources();
        controller.bindControl();
    }

    private void loadEntitiesResources() {
        for (EntityInterface entity : entities.values()) {
            entity.getView().loadResources();
        }
    }

    private void initEntitiesResources() {
        
        List<EntityInterface> entitiesOrdered = new ArrayList<>(entities.values());
        Collections.sort(entitiesOrdered, new Comparator<EntityInterface>() {
            @Override
            public int compare(EntityInterface entity1, EntityInterface entity2) {
                if (entity1.getZIndex() == entity2.getZIndex()) {
                    throw new Error("Equal index");
                }
                return entity1.getZIndex() - entity2.getZIndex();
            }
        });

        for (EntityInterface entity : entitiesOrdered) {
            entity.init(gameResources);
            entity.getView().init(graphicResources);
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
