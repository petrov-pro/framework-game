package ua.org.petroff.game.engine.entities.weapons;

import com.badlogic.gdx.math.Vector2;

public interface WeaponInterface {

    public int getDamage();

    public Vector2 getPlaceHit();

    public WeaponInterface setDirectionHit(Vector2 vectorHit);

    public Vector2 getDirectionHit();

}
