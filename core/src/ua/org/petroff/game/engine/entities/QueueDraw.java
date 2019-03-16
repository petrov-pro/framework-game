package ua.org.petroff.game.engine.entities;

import java.util.TreeMap;

public class QueueDraw<Integer, QueueDrawInterface> extends TreeMap {

    public static final int Z_INDEX_START = 1;
    public static final int Z_INDEX_END = 100;

    public QueueDrawInterface putSafe(int key, QueueDrawInterface draw) throws IndexOutOfBoundsException {

        if (get(key) == null) {
            return (QueueDrawInterface) put(key, draw);
        }

        if (key < Z_INDEX_START) {
            throw new IndexOutOfBoundsException("zIndex too small");
        }

        for (int x = key; x <= Z_INDEX_END; x++) {
            if (get(x) == null) {
                return (QueueDrawInterface) put(x, draw);
            }
        }
        throw new IndexOutOfBoundsException("zIndex incorrect");
    }

}
