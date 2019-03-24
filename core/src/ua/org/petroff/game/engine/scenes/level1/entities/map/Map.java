package ua.org.petroff.game.engine.scenes.level1.entities.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import ua.org.petroff.game.engine.Settings;
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
    
    private final float friction = 2f;
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
        createPhysicMap();
        MapObject cameraObject = MapResolver.findObject(asset.getMap(), OBJECT_NAME);
        cameraPosition = new Vector2(MapResolver.coordinateToWorld(cameraObject.getProperties().get("x", Float.class).intValue()),
                MapResolver.coordinateToWorld(cameraObject.getProperties().get("y", Float.class).intValue()));
        createGround(gameResources);
    }
    
    public Vector2 getCameraPosition() {
        return cameraPosition;
    }
    
    private void createPhysicMap() {
        World world = new World(new Vector2(0, gravitation), true);
        Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
        WorldContactListener worldContactListener = new WorldContactListener();
        world.setContactListener(worldContactListener);
        gameResources.setWorld(world);
        gameResources.setWorldContactListener(worldContactListener);
        gameResources.setDebugRenderer(debugRenderer);
    }
    
    private void createGround(GameResources gameResources) {
        
        ArrayList<MapObject> groundObjects = ua.org.petroff.game.engine.util.MapResolver.findObject(asset.getMap(),
                "surface");
        
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        
        for (MapObject groundObject : groundObjects) {
            
            Float friction = groundObject.getProperties().get("friction", Float.class);
            
            if (null == friction) {
                friction = this.friction;
            }
            
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
            
            FixtureDef groundFixture = new FixtureDef();
            groundFixture.density = 1f;
            groundFixture.shape = chain;
            groundFixture.restitution = 0f;
            groundFixture.friction = friction;
            
            body.createFixture(groundFixture);
            chain.dispose();
        }
        
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
