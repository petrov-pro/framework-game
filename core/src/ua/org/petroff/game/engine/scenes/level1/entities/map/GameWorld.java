package ua.org.petroff.game.engine.scenes.level1.entities.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.scenes.level1.entities.map.surface.Surface;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.MapResolver;

public class GameWorld implements EntityInterface {

    public static final String OBJECT_NAME = "start camera position";
    public static final String DESCRIPTOR = "map level 1";
    public View view;

    private static final int GRAVITATION = -10;
    private Vector2 cameraPosition;

    public final GameResources gameResources;

    public GameWorld(Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        this.gameResources = gameResources;
        com.badlogic.gdx.maps.Map map = asset.getMap();
        createPhysicWorld(map);
        createMessageDispatcher();
        createCameraPosition(map);
        view = new View(asset, graphicResources, this);
        new Surface(asset, gameResources, graphicResources);
    }

    public Vector2 getCameraPosition() {
        return cameraPosition;
    }

    @Override
    public void update() {
        gameResources.getWorld().step(1 / 60f, 6, 2);
        gameResources.getMessageManger().update();
        GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    private void createCameraPosition(com.badlogic.gdx.maps.Map map) {
        MapObject cameraObject = MapResolver.findObject(map, OBJECT_NAME);

        cameraPosition = new Vector2(MapResolver.coordinateToWorld(cameraObject.getProperties().get("x", Float.class).intValue()),
                MapResolver.coordinateToWorld(cameraObject.getProperties().get("y", Float.class).intValue()));
    }

    private void createMessageDispatcher() {
        MessageManager messageManger = MessageManager.getInstance();
        gameResources.setMessageManger(messageManger);
    }

    private void createPhysicWorld(com.badlogic.gdx.maps.Map map) {
        World world = new World(new Vector2(0, GRAVITATION), true);

        WorldContactListener worldContactListener = new WorldContactListener();
        world.setContactListener(worldContactListener);
        gameResources.setWorld(world);
        gameResources.setWorldContactListener(worldContactListener);

        int width = map.getProperties().get("width", Integer.class);
        int height = map.getProperties().get("height", Integer.class);
        gameResources.setWorldHeight(height);
        gameResources.setWorldWidth(width);
    }
}
