package ua.org.petroff.game.engine.entities.characters.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;

public class Static extends BaseGraphic {

    private final TextureRegion frame;

    public Static(TextureRegion frame, StateInterface.State action, WorldInterface.Vector vector) {
        super(action, vector, false);
        this.frame = frame;
    }

    @Override
    public TextureRegion prepareGraphic() {
        return frame;
    }
}
