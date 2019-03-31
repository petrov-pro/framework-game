package ua.org.petroff.game.engine.scenes.level1.entities.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.MapResolver;

public class Map implements EntityInterface {

    public static final String OBJECT_NAME = "start camera position";
    public static final String DESCRIPTOR = "map level 1";
    public GameResources gameResources;
    public View view;

    private final int gravitation = -10;
    private final Assets asset;
    private Vector2 cameraPosition;
    private final int zIndex = 1;

    public Map(Assets asset) {
        view = new View(asset, this);
        this.asset = asset;
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    @Override
    public void init(GameResources gameResources) {
        this.gameResources = gameResources;
        Surface surface = new Surface(asset, gameResources);
        com.badlogic.gdx.maps.Map map = asset.getMap();

        createPhysicWorld();

        createCameraPosition(map);

        surface.create(map);

    }

    public Vector2 getCameraPosition() {
        return cameraPosition;
    }

    private void createCameraPosition(com.badlogic.gdx.maps.Map map) {
        MapObject cameraObject = MapResolver.findObject(map, OBJECT_NAME);

        cameraPosition = new Vector2(MapResolver.coordinateToWorld(cameraObject.getProperties().get("x", Float.class).intValue()),
                MapResolver.coordinateToWorld(cameraObject.getProperties().get("y", Float.class).intValue()));
    }

    private void createPhysicWorld() {
        World world = new World(new Vector2(0, gravitation), true);
        Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
        WorldContactListener worldContactListener = new WorldContactListener();
        world.setContactListener(worldContactListener);
        gameResources.setWorld(world);
        gameResources.setWorldContactListener(worldContactListener);
        gameResources.setDebugRenderer(debugRenderer);
    }

    @Override
    public void update() {
        gameResources.getWorld().step(1 / 60f, 6, 2);
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

}
