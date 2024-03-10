package ua.org.petroff.game.engine.entities.weapons.melee;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Pool;
import ua.org.petroff.game.engine.characters.creature.CreatureInterface;
import ua.org.petroff.game.engine.equipment.Shield;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class MeleeWeapon implements WeaponInterface, RayCastCallback, Telegraph, Pool.Poolable {

    private int damage = 30;
    private Vector2 vectorHit;

    private GameResources gameResources;
    private Vector2 startPosition;
    private Vector2 finishPosition;
    private Fixture creature;
    private Vector2 point;

    public MeleeWeapon() {
    }

    public MeleeWeapon(GameResources gameResources, Vector2 startPosition, Vector2 finishPosition, int damage) {
        this.damage = damage;
        this.gameResources = gameResources;
        this.startPosition = startPosition;
        this.finishPosition = finishPosition;
    }

    public void init(GameResources gameResources, Vector2 startPosition, Vector2 finishPosition, int damage) {
        this.damage = damage;
        this.gameResources = gameResources;
        this.startPosition = startPosition;
        this.finishPosition = finishPosition;
    }

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

    public void update() {
        gameResources.getWorld().rayCast(this, startPosition, finishPosition);
        if (creature != null) {
            gameResources.getMessageManger().dispatchMessage(
                    this,
                    (Telegraph) creature.getBody().getUserData(),
                    StateInterface.State.HIT.telegramNumber,
                    setDirectionHit(
                            point.cpy()
                    ),
                    false
            );
        }
    }

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

        if (fixture.getUserData() instanceof CreatureInterface) {
            creature = fixture;
            this.point = point;

            return -1f;
        }

        if (fixture.getUserData() instanceof Shield && fixture.isSensor()) {
            return -1f;
        }

        if (fixture.getUserData() instanceof Shield && !fixture.isSensor()) {
            creature = null;
            this.point = null;
            return 0f;
        }

        return 0f;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return true;
    }

    @Override
    public void reset() {
        damage = 0;
        gameResources = null;
        startPosition = null;
        finishPosition = null;
        creature = null;
        point = null;
        vectorHit = null;
    }

}
