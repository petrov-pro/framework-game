package ua.org.petroff.game.engine.entities.characters.enemies.simple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.characters.base.creature.View;
import ua.org.petroff.game.engine.entities.characters.base.visual.effects.Blood;
import ua.org.petroff.game.engine.entities.characters.enemies.Enemy;
import ua.org.petroff.game.engine.entities.characters.enemies.EnemySwordGraphic;
import ua.org.petroff.game.engine.entities.weapons.melee.Sword;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.Timer;

public class DirtyManSword extends Enemy {

    public static final String DESCRIPTOR = "dirty_man_sword";
    public static final float FIRE_SPEED = 0.00005f;

    private Fixture swordHit;
    private final Sword sword = new Sword();

    public DirtyManSword(int x, int y, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        super(x, y, asset, gameResources, graphicResources, DESCRIPTOR);
        view = new View(
                asset,
                graphicResources,
                this,
                new EnemySwordGraphic(asset, graphicResources, DESCRIPTOR, FIRE_SPEED),
                new Blood(asset, graphicResources)
        );
        hitRange = 1.5f;
    }

    @Override
    public void update() {
        super.update();

        if (swordHit != null && Timer.run(this.toString(), 0.1f)) {
            body.destroyFixture(swordHit);
            Timer.reset(this.toString());
            swordHit = null;

        }
    }

    @Override
    public boolean fire() {
        boolean canFire = super.fire();

        if (canFire && swordHit == null) {
            PolygonShape poly = new PolygonShape();
            float positionSwordHit = (vector == WorldInterface.Vector.LEFT) ? (bodyWidth - 0.1f) : -(bodyWidth - 0.1f);
            poly.setAsBox((bodyWidth / 2f) - 0.2f, 0.1f, body.getLocalCenter().cpy().sub(positionSwordHit, -0.2f), 0);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = poly;
            fixtureDef.isSensor = true;

            swordHit = body.createFixture(fixtureDef);
            swordHit.setUserData(sword);
            poly.dispose();
        }

        return canFire;
    }

    @Override
    public boolean withinReachFire() {
        if (!Timer.runReset(this.toString() + "fire", 1)) {
            return false;
        }
        return super.withinReachFire();
    }

}
