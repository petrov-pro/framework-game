package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.Interfaces.MoveEntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.entities.hud.HUD;
import ua.org.petroff.game.engine.entities.player.Player;
import ua.org.petroff.game.engine.scenes.Interface.ContainerInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.scenes.core.ManagerScenes;
import ua.org.petroff.game.engine.scenes.level1.entities.map.GameWorld;
import ua.org.petroff.game.engine.entities.surface.Surface;
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
    private Array<QueueDrawInterface> drawings;
    private Array<EntityInterface> models;

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
        drawings = new Array();
        models = new Array();
        screen = new Level1Screen(models, drawings, graphicResources);
        controller = new Level1Controller(manageScene, screen, (MoveEntityInterface) entities.get(Player.DESCRIPTOR));
        loadEntitiesResources();
    }

    @Override
    public void init() {
        initEntitiesResources();
        controller.bindControl();
    }

    private void loadEntities() {
        entities.put(Player.DESCRIPTOR, new Player(assets));
        entities.put(Surface.DESCRIPTOR, new Surface(assets));
        entities.put(HUD.DESCRIPTOR, new HUD(assets));
        entities.put(GameWorld.DESCRIPTOR, new GameWorld(assets));

    }

    private void loadEntitiesResources() {
        for (EntityInterface entity : entities.values()) {
            try {
                entity.getView().loadResources();
            } catch (UnsupportedOperationException UNOE) {
            }

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

        Map<Integer, QueueDrawInterface> queueDraw = prepareModels(entitiesOrdered);

        prepareDrawings(queueDraw);

    }

    public Map<Integer, QueueDrawInterface> prepareModels(List<EntityInterface> entitiesOrdered) {
        Map<Integer, QueueDrawInterface> queueDraw = new QueueDraw<>();
        for (EntityInterface entity : entitiesOrdered) {
            entity.init(gameResources);
            try {
                entity.getView().init(graphicResources);
                ((GraphicQueueMemberInterface) entity.getView()).prepareDraw(queueDraw);
            } catch (UnsupportedOperationException UNOE) {
            }

            try {
                EntityInterface model = entity.prepareModel();
                models.add(entity);
            } catch (UnsupportedOperationException UNOE) {
            }

        }

        return queueDraw;
    }

    private void prepareDrawings(Map<Integer, QueueDrawInterface> queueDraw) {
        for (QueueDrawInterface entity : queueDraw.values()) {
            drawings.add(entity);
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
