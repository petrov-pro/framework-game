package ua.org.petroff.game.engine.util;

import com.badlogic.gdx.Gdx;
import java.util.HashMap;
import java.util.Map;

public class Timer {

    private static final Map<String, Float> elapsedTime = new HashMap<>();

    public static boolean run(String nameTimer, float $targetSec) {
        float currentTime = elapsedTime.getOrDefault(nameTimer, 0f);
        currentTime += Gdx.graphics.getDeltaTime();
        elapsedTime.put(nameTimer, currentTime);
        if (currentTime >= $targetSec) {
            reset(nameTimer);

            return true;
        }
        return false;
    }

    public static float getElapsedTime(String nameTimer) {
        return elapsedTime.getOrDefault(nameTimer, 0f);
    }

    // Call this method to reset the elapsed time to zero
    public static void reset(String nameTimer) {
        elapsedTime.put(nameTimer, 0f);
    }
}
