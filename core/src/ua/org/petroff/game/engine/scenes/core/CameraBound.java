package ua.org.petroff.game.engine.scenes.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class CameraBound extends OrthographicCamera {

    private BoundingBox left, right, top, bottom = null;
    private Vector3 lastPosition = new Vector3();

    public void setWorldBounds(int left, int bottom, int width, int height) {
        int top = bottom + height;
        int right = left + width;

        this.left = new BoundingBox(new Vector3(left - 2, 0, 0), new Vector3(left, top, 0));
        this.right = new BoundingBox(new Vector3(right + 1, 0, 0), new Vector3(right + 2, top, 0));
        this.top = new BoundingBox(new Vector3(0, top + 1, 0), new Vector3(right, top + 2, 0));
        this.bottom = new BoundingBox(new Vector3(0, bottom - 1, 0), new Vector3(right, bottom - 2, 0));
    }

    public void positionSafe(float x, float y) {
        lastPosition.set(position.x, position.y, 0);
        position.x = x;
        position.y = y;
        update();
        ensureBounds();
        update();
    }

    public void ensureBounds() {

        if (frustum.boundsInFrustum(left)) {
            position.set(lastPosition.x, position.y, 0);
        }

        if (frustum.boundsInFrustum(top)) {
            position.set(position.x, lastPosition.y, 0);
        }
    }

}
