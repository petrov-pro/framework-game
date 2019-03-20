package ua.org.petroff.game.engine.scenes.level1.entities.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.EntityInterface;
import ua.org.petroff.game.engine.entities.ViewInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.MapHelper;

public class Map implements EntityInterface {

    private final int zIndex = 1;
    public static final String OBJECT_NAME = "start camera position";
    public static final String DESCRIPTOR = "map level 1";
    private final int gravitation = -10;
    public View view;
    private final Assets asset;
    private Vector2 cameraPosition;
    public GameResources gameResources;

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
        createPhysicMap();
        MapObject cameraObject = MapHelper.findObject(asset.getMap(), OBJECT_NAME);
        cameraPosition = new Vector2(MapHelper.coordinateToWorld(cameraObject.getProperties().get("x", Float.class).intValue()),
                MapHelper.coordinateToWorld(cameraObject.getProperties().get("y", Float.class).intValue()));
        createGround(gameResources);
    }

    public Vector2 getCameraPosition() {
        return cameraPosition;
    }

    private void createPhysicMap() {
        World world = new World(new Vector2(0, gravitation), true);
        Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
        gameResources.setWorld(world);
        gameResources.setDebugRenderer(debugRenderer);
    }

    private void createGround(GameResources gameResources) {

        MapObject groundObject = ua.org.petroff.game.engine.util.MapHelper.findObject(asset.getMap(),
                "ground");

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = gameResources.getWorld().createBody(bodyDef);

        float[] vertices = ((PolylineMapObject) groundObject).getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] * Settings.SCALE;
            worldVertices[i].y = vertices[i * 2 + 1] * Settings.SCALE;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);

        body.createFixture(chain, 1);
        chain.dispose();
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
