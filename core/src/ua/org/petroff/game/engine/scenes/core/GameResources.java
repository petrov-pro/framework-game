package ua.org.petroff.game.engine.scenes.core;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import ua.org.petroff.game.engine.scenes.level1.entities.map.MessageManger;
import ua.org.petroff.game.engine.scenes.level1.entities.map.WorldContactListener;

public class GameResources {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private WorldContactListener worldContactListener;
    private MessageManger messageManger;
    private FPSLogger fps = new FPSLogger();
    private int worldWidth;
    private int worldHeight;

    public MessageManger getMessageManger() {
        return messageManger;
    }

    public void setMessageManger(MessageManger messageManger) {
        this.messageManger = messageManger;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public void setWorldWidth(int worldWidth) {
        this.worldWidth = worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public void setWorldHeight(int worldHeight) {
        this.worldHeight = worldHeight;
    }

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

    public void debugPhysic(Matrix4 combined) {
        fps.log();
        debugRenderer.render(world, combined);
    }

}
