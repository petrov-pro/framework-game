package ua.org.petroff.game.engine.entities.weapons.hand;

import com.badlogic.gdx.physics.box2d.Body;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class Telegram {

    private WeaponInterface.Type type;
    private Body body;
    private float x;
    private float y;
    private float positionHitX;
    private float positionHitY;
    private int damage;

    public Telegram(WeaponInterface.Type type, Body body, int damage, float positionHitX, float positionHitY, float x, float y) {
        this.type = type;
        this.body = body;
        this.x = x;
        this.y = y;
        this.positionHitX = positionHitX;
        this.positionHitY = positionHitY;
        this.damage = damage;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public WeaponInterface.Type getType() {
        return type;
    }

    public Body getBody() {
        return body;
    }

    public float getPositionHitX() {
        return positionHitX;
    }

    public float getPositionHitY() {
        return positionHitY;
    }

    public int getDamage() {
        return damage;
    }

}
