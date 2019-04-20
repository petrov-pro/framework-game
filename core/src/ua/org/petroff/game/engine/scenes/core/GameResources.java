package ua.org.petroff.game.engine.scenes.core;

import com.badlogic.gdx.physics.box2d.World;
import ua.org.petroff.game.engine.scenes.level1.entities.map.MessageManger;
import ua.org.petroff.game.engine.scenes.level1.entities.map.WorldContactListener;

public class GameResources {

    private World world;

    private WorldContactListener worldContactListener;
    private MessageManger messageManger;
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

    public WorldContactListener getWorldContactListener() {
        return worldContactListener;
    }

    public void setWorldContactListener(WorldContactListener worldContactListener) {
        this.worldContactListener = worldContactListener;
    }

}
