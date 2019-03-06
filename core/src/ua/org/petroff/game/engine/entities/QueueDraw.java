package ua.org.petroff.game.engine.entities;

import java.util.TreeMap;

public class QueueDraw<Integer, QueueDrawInterface> extends TreeMap {

    public QueueDrawInterface putSafe(Integer key, QueueDrawInterface draw) {
        return (QueueDrawInterface) put(key, draw);
    }

}
