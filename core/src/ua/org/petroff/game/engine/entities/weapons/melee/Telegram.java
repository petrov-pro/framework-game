package ua.org.petroff.game.engine.entities.weapons.melee;

import com.badlogic.gdx.math.Vector2;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class Telegram extends ua.org.petroff.game.engine.weapons.Telegram {

    private final Vector2 finish;

    public Telegram(WeaponInterface.Type type, WorldInterface.Vector vector, Vector2 bodyPosition, int damage, float offsetX, float offsetY, float lenght) {
        super(type, vector, bodyPosition, damage, offsetX, offsetY);
        if (vector.equals(WorldInterface.Vector.LEFT)) {
            finish = start.cpy().sub(lenght, -offsetY);
        } else {
            finish = start.cpy().add(lenght, offsetY);
        }
    }

    public Vector2 getFinish() {
        return finish;
    }

}
