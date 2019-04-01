package ua.org.petroff.game.engine.scenes.level1.entities.map;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegraph;
import java.util.HashMap;

public class MessageManger extends MessageDispatcher {

    private final HashMap<String, Telegraph> telegraphs = new HashMap();

    public void addTelegraph(String key, Telegraph telegraph) {
        telegraphs.put(key, telegraph);
    }

    public Telegraph getTelegraph(String key) {
        return telegraphs.get(key);
    }
}
