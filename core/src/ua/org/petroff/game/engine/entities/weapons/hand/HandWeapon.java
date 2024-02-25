package ua.org.petroff.game.engine.entities.weapons.hand;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class HandWeapon implements WeaponInterface {

    private int damage = 30;
    private Vector2 vectorHit;
    private Body body;
    private Fixture swordSensor;

    public HandWeapon(Body body, float positionHit, float positionHitY, float x, float y) {
        this.body = body;
        createSensorFire(body, positionHit, positionHitY, x, y);
    }

    public HandWeapon(Body body, float positionHit, float positionHitY, float x, float y, int damage) {
        this.damage = damage;
        this.body = body;
        createSensorFire(body, positionHit, positionHitY, x, y);
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

    private void createSensorFire(Body body, float positionHitX, float positionHitY, float x, float y) {
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(x, y, body.getLocalCenter().cpy().sub(positionHitX, positionHitY), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.isSensor = true;

        swordSensor = body.createFixture(fixtureDef);
        swordSensor.setUserData(this);
        poly.dispose();
    }

    public void destroy() {
        body.destroyFixture(swordSensor);
    }

}
