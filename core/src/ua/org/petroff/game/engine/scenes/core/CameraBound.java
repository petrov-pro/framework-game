package ua.org.petroff.game.engine.scenes.core;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class CameraBound extends OrthographicCamera {

    private BoundingBox left, right, top, bottom = null;
    private final Vector3 lastPosition = new Vector3();
    

    public void setWorldBounds(int width, int height) {

        this.left = new BoundingBox(new Vector3(0, 0, 0), new Vector3(0, height, 0));
        this.right = new BoundingBox(new Vector3(width, 0, 0), new Vector3(width, height, 0));
        this.top = new BoundingBox(new Vector3(0, height, 0), new Vector3(height, height, 0));
        this.bottom = new BoundingBox(new Vector3(0, 0, 0), new Vector3(height, 0, 0));
    }

    public void positionSafe(Vector3 newPosition) {
        lastPosition.set(position.cpy());
        position.set(newPosition);
        update();
        ensureBounds();
        update();
    }

    public void ensureBounds() {

        if (frustum.boundsInFrustum(left) || frustum.boundsInFrustum(right)) {
            position.set(lastPosition.x, position.y, 0);
        }

        if (frustum.boundsInFrustum(bottom) || frustum.boundsInFrustum(top)) {
            position.set(position.x, lastPosition.y, 0);
        }
    }

}
