package ua.org.petroff.game.engine.entities.weapons.arrow;

public class Telegram {

    private static final float DEFAULT_SPEED_Y = 2f;
    private static final float DEFAULT_SPEED_X = 1f;
    private static final float DEFAULT_SPEED_ANGULAR = -0.1f;

    private float x;
    private float y;
    private float forceX;
    private float forceY;
    private float angular;

    public Telegram(float x, float y, float forceX, float forceY, float angular) {
        this.x = x;
        this.y = y;
        this.forceX = forceX;
        this.forceY = forceY;
        this.angular = angular;
    }

    public Telegram(float x, float y) {
        this.x = x;
        this.y = y;
        this.forceX = DEFAULT_SPEED_X;
        this.forceY = DEFAULT_SPEED_Y;
        this.angular = DEFAULT_SPEED_ANGULAR;
    }

    public Telegram(float x, float y, float forceX) {
        this.x = x;
        this.y = y;
        this.forceX = forceX;
        this.forceY = DEFAULT_SPEED_Y;
        this.angular = DEFAULT_SPEED_ANGULAR;
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

}
