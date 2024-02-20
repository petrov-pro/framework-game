package ua.org.petroff.game.engine.entities.weapons.melee;

import com.badlogic.gdx.math.Vector2;
import ua.org.petroff.game.engine.entities.weapons.WeaponInterface;

public class Sword implements WeaponInterface {

    private final int damage = 30;
    private Vector2 vectorHit;

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public Vector2 getPlaceHit() {
        return null;
    }

    @Override
    public WeaponInterface setDirectionHit(Vector2 vectorHit) {
        this.vectorHit = vectorHit;

        return this;
    }

    @Override
    public Vector2 getDirectionHit() {
        return vectorHit;
    }

}
