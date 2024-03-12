package ua.org.petroff.game.engine.characters.creature;

import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import ua.org.petroff.game.engine.interfaces.GroundedInterface;

public interface CreatureInterface extends Telegraph, GroundedInterface {

    public void decreaseLife(int amount, Vector2 placeHit);
    
    public void changeLife(int amount);
    
    public void decreaseLife(int amount, Vector2 placeHit, Vector2 directionHit);

    public void died();

    public boolean isGrounded();
}
