package ua.org.petroff.game.engine.entities.guns;

import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.Listener;
import ua.org.petroff.game.engine.entities.characters.base.creature.CreatureInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;

public class GunListener extends Listener {

    public GunListener(GameResources gameResources) {
        super(gameResources);
    }

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        if (shouldContact(contact.getFixtureA())) {
            handleContact(userDataA, userDataB);

            return;
        }

        if (shouldContact(contact.getFixtureB())) {
            handleContact(userDataB, userDataA);
        }
    }

    private boolean shouldContact(Fixture gun) {
        return gun.getBody().getUserData() instanceof GunInterface
                && gun.isSensor();

    }

    private void handleContact(Object gunData, Object entityData) {
        if (entityData instanceof CreatureInterface) {
            gameResources.getMessageManger().dispatchMessage(
                    this,
                    (Telegraph) entityData,
                    StateInterface.State.HIT.telegramNumber,
                    gunData,
                    false
            );
        }
    }

}
