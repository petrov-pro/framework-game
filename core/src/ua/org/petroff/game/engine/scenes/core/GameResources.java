package ua.org.petroff.game.engine.scenes.core;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import ua.org.petroff.game.engine.scenes.level1.entities.map.WorldContactListener;

public class GameResources {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private WorldContactListener worldContactListener;

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Box2DDebugRenderer getDebugRenderer() {
        return debugRenderer;
    }

    public void setDebugRenderer(Box2DDebugRenderer debugRenderer) {
        this.debugRenderer = debugRenderer;
    }

    public WorldContactListener getWorldContactListener() {
        return worldContactListener;
    }

    public void setWorldContactListener(WorldContactListener worldContactListener) {
        this.worldContactListener = worldContactListener;
    }

}
