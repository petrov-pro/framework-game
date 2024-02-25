package ua.org.petroff.game.engine.scenes.core;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import ua.org.petroff.game.engine.interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.map.WorldContactListener;

public class GameResources {

    private World world;

    private WorldContactListener worldContactListener;
    private MessageManager messageManger;
    private int worldWidth;
    private int worldHeight;
    private Array<EntityInterface> models;

    public MessageManager getMessageManger() {
        return messageManger;
    }

    public void setMessageManger(MessageManager messageManger) {
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

    public Array<EntityInterface> getModels() {
        return models;
    }

    public void setModels(Array<EntityInterface> models) {
        this.models = models;
    }

    public EntityInterface findModel(Class modelE) {
        for (EntityInterface model : models) {
            if (modelE.isInstance(model)) {
                return model;
            }
        }
        throw new Error("Cant find model: " + modelE.getName());
    }

}
