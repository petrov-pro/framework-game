package ua.org.petroff.game.engine.entities.weapons.ranged;

import com.badlogic.gdx.math.Vector2;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class Telegram extends ua.org.petroff.game.engine.weapons.Telegram {

    private static final float DEFAULT_SPEED_Y = 2f;
    private static final float DEFAULT_SPEED_ANGULAR = -0.1f;

    private float forceX;
    private float forceY;
    private float angular;

    public Telegram(WeaponInterface.Type type, WorldInterface.Vector vector, Vector2 bodyPosition, int damage, float offsetX, float offsetY, float forceX, float forceY, float angular) {
        super(type, vector, bodyPosition, damage, offsetX, offsetY);
        this.forceX = vector.equals(WorldInterface.Vector.LEFT) ? -forceX : forceX;
        this.forceY = forceY;
        this.angular = angular;
    }

    public Telegram(WeaponInterface.Type type, WorldInterface.Vector vector, Vector2 bodyPosition, int damage, float offsetX, float offsetY, float forceX) {
        super(type, vector, bodyPosition, damage, offsetX, offsetY);
        this.forceX = forceX;
        this.forceY = DEFAULT_SPEED_Y;
        this.angular = DEFAULT_SPEED_ANGULAR;
        this.forceX = vector.equals(WorldInterface.Vector.LEFT) ? -forceX : forceX;
    }

    public float getForceX() {
        return forceX;
    }

    public float getForceY() {
        return forceY;
    }

    public float getAngular() {
        return angular;
    }

}
