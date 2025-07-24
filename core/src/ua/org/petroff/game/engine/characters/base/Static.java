package ua.org.petroff.game.engine.characters.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.interfaces.WorldInterface;

public class Static extends GraphicElement {

    private final TextureRegion frame;

    public Static(TextureRegion frame, StateInterface.State state, WorldInterface.Vector vector) {
        super(state, vector, false);
        this.frame = frame;
    }

    @Override
    public TextureRegion prepareGraphic() {
        return frame;
    }
}
