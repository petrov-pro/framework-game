package ua.org.petroff.game.engine.weapons;

import com.badlogic.gdx.math.Vector2;
import ua.org.petroff.game.engine.interfaces.WorldInterface;

public class Telegram {

    private final WeaponInterface.Type type;
    private final int damage;

    protected final Vector2 start;

    public Telegram(WeaponInterface.Type type, WorldInterface.Vector vector, Vector2 bodyPosition, int damage, float offsetX, float offsetY) {
        this.type = type;
        this.damage = damage;

        if (vector.equals(WorldInterface.Vector.LEFT)) {
            start = bodyPosition.sub(offsetX, -offsetY);
        } else {
            start = bodyPosition.add(offsetX, offsetY);
        }
    }

    public Vector2 getStart() {
        return start;
    }

    public int getDamage() {
        return damage;
    }

    public WeaponInterface.Type getType() {
        return type;
    }
}
