package ua.org.petroff.game.engine.entities.guns.arrow;

public class Telegram {

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
        this.forceX = 130;
        this.forceY = 30;
        this.angular = -0.1f;
    }

    public Telegram(float x, float y, float forceX) {
        this.x = x;
        this.y = y;
        this.forceX = forceX;
        this.forceY = 30;
        this.angular = -0.1f;
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
