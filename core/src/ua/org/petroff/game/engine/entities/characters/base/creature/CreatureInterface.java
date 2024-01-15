package ua.org.petroff.game.engine.entities.characters.base.creature;

import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import ua.org.petroff.game.engine.entities.Interfaces.GroundedInterface;

public interface CreatureInterface extends Telegraph, GroundedInterface {

    public void decreaseLive(int amount, Vector2 placeHit);

    public void died();

    public boolean isGrounded();
}
