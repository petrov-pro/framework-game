package ua.org.petroff.game.engine.entities.guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import ua.org.petroff.game.engine.entities.Interfaces.GroundedInterface;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.Listener;
import ua.org.petroff.game.engine.entities.characters.base.creature.CreatureInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.entities.map.Surface;

public class GunListener extends Listener {

    public GunListener(GameResources gameResources) {
        super(gameResources);
    }

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        if (shouldContact(contact.getFixtureA())) {
            handleContact(contact, contact.getFixtureA().getUserData(), userDataB);

            return;
        }

        if (shouldContact(contact.getFixtureB())) {
            handleContact(contact, contact.getFixtureB().getUserData(), userDataA);
        }
    }

    private boolean shouldContact(Fixture gun) {
        return gun.getUserData() instanceof GunInterface
                && gun.isSensor();

    }

    private void handleContact(Contact contact, Object gunData, Object entityData) {
        if (entityData instanceof CreatureInterface) {
            Vector2 positionA = contact.getFixtureA().getBody().getPosition();
            Vector2 positionB = contact.getFixtureB().getBody().getPosition();
            gameResources.getMessageManger().dispatchMessage(
                    this,
                    (Telegraph) entityData,
                    StateInterface.State.HIT.telegramNumber,
                    ((GunInterface) gunData).setDirectionHit(
                            positionB.cpy().sub(positionA).nor()
                    ),
                    false
            );
        } else if (entityData instanceof Surface
                && gunData instanceof GroundedInterface) {
            gameResources.getMessageManger().dispatchMessage(
                    this,
                    (Telegraph) gunData,
                    StateInterface.State.GROUND.telegramNumber,
                    true,
                    false
            );
        }
    }

}
