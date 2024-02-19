package ua.org.petroff.game.engine.util;

import java.util.Random;

public class RandomGenerate {

    public static int generate(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }

        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
