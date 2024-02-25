package ua.org.petroff.game.engine.entities.weapons.ranged;

import com.badlogic.gdx.physics.box2d.Body;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class Telegram {

    private static final float DEFAULT_SPEED_Y = 2f;
    private static final float DEFAULT_SPEED_X = 1f;
    private static final float DEFAULT_SPEED_ANGULAR = -0.1f;

    private WeaponInterface.Type type;
    private float x;
    private float y;
    private float forceX;
    private float forceY;
    private float angular;

    public Telegram(WeaponInterface.Type type, float x, float y, float forceX, float forceY, float angular) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.forceX = forceX;
        this.forceY = forceY;
        this.angular = angular;
    }

    public Telegram(WeaponInterface.Type type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.forceX = DEFAULT_SPEED_X;
        this.forceY = DEFAULT_SPEED_Y;
        this.angular = DEFAULT_SPEED_ANGULAR;
    }

    public Telegram(WeaponInterface.Type type, float x, float y, float forceX) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.forceX = forceX;
        this.forceY = DEFAULT_SPEED_Y;
        this.angular = DEFAULT_SPEED_ANGULAR;
    }

    public Telegram(WeaponInterface.Type type, Body body, int FIRE_DAMAGE, float positionSwordHit, float f, float f0, float f1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
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

    public WeaponInterface.Type getType() {
        return type;
    }

}
