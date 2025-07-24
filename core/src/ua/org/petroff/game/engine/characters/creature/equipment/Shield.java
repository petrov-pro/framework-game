package ua.org.petroff.game.engine.characters.creature.equipment;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Shield {

    private Fixture shieldRight;
    private Fixture shieldLeft;
    private boolean isActive = false;

    public Shield(Body body, float x, float y) {
        createShield(body, x, y);
    }

    public void left() {
        shieldRight.setSensor(true);
        shieldLeft.setSensor(false);
        isActive = true;
    }

    public void right() {
        shieldRight.setSensor(false);
        shieldLeft.setSensor(true);
        isActive = true;
    }

    public void hide() {
        shieldRight.setSensor(true);
        shieldLeft.setSensor(true);
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    private void createShield(Body body, float x, float y) {
        PolygonShape poly = new PolygonShape();
        poly.setAsBox(x - 0.9f, y - 1, body.getLocalCenter().cpy().sub(-(x - 0.3f), y - 1.5f), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.isSensor = true;

        shieldRight = body.createFixture(fixtureDef);
        shieldRight.setUserData(this);

        poly.setAsBox(x - 0.9f, y - 1, body.getLocalCenter().cpy().sub((x - 0.3f), y - 1.5f), 0);
        shieldLeft = body.createFixture(fixtureDef);
        shieldLeft.setUserData(this);

        poly.dispose();
    }

}
