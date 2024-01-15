package ua.org.petroff.game.engine.util;

import com.badlogic.gdx.Gdx;

public class Timer {

    private static float elapsedTime;

    public static boolean run(float $targetSec) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (elapsedTime >= $targetSec) {
            reset();

            return true;
        }
        return false;
    }

    public static float getElapsedTime() {
        return elapsedTime;
    }

    // Call this method to reset the elapsed time to zero
    public static void reset() {
        elapsedTime = 0f;
    }
}
