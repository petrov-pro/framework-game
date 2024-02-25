package ua.org.petroff.game.engine.characters.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import ua.org.petroff.game.engine.scenes.core.DebugWorld;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class Ray implements RayCastCallback {

    public static final float GAP_LENGHT = 10f;

    private final GameResources gameResources;
    private boolean isTouch = false;
    private final float stopLenght = (1 / GAP_LENGHT) * 5;

    public Ray(GameResources gameResources) {
        this.gameResources = gameResources;
    }

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        if (fraction > stopLenght) {
            isTouch = true;
        } else {
            isTouch = false;
        }

        return 0f;
    }

    public void update(Vector2 startRay) {
        gameResources.getWorld().rayCast(this, startRay, startRay.cpy().add(0, -GAP_LENGHT));
    }

    public boolean isTouch() {
        return isTouch;
    }

    public void reset() {
        isTouch = false;
    }
}
