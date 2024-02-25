package ua.org.petroff.game.engine.weapons;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Arrays;

public interface WeaponInterface {

    public enum Type {

        BOW, SWORD, BARE;

    }

    public static final ArrayList<Type> rangedWeapon = new ArrayList<>(Arrays.asList(Type.BOW));
    public static final ArrayList<Type> handWeapon = new ArrayList<>(Arrays.asList(Type.BARE, Type.SWORD));

    public int getDamage();

    public Vector2 getPlaceHit();

    public WeaponInterface setDirectionHit(Vector2 vectorHit);

    public Vector2 getDirectionHit();

}
