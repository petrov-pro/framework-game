package ua.org.petroff.game.engine.entities.player.graphics;

import ua.org.petroff.game.engine.characters.base.GraphicElement;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.interfaces.StateInterface;

public class Animation extends GraphicElement {

    protected com.badlogic.gdx.graphics.g2d.Animation animation;
    private float speed;

    public Animation(com.badlogic.gdx.graphics.g2d.Animation animation, StateInterface.State action, WorldInterface.Vector vector, boolean isLoop, float speed) {
        super(action, vector, isLoop);
        this.animation = animation;
        this.speed = speed;
    }

    public Animation(com.badlogic.gdx.graphics.g2d.Animation animation, StateInterface.State action, WorldInterface.Vector vector, boolean isLoop) {
        super(action, vector, isLoop);
        this.animation = animation;
        this.speed = 0f;
    }

    public Animation(com.badlogic.gdx.graphics.g2d.Animation animation, StateInterface.State action, boolean isLoop) {
        super(action, isLoop);
        this.animation = animation;
        this.speed = 0f;
    }

    public Animation(com.badlogic.gdx.graphics.g2d.Animation animation, StateInterface.State action, boolean isLoop, float speed) {
        super(action, isLoop);
        this.speed = speed;
        this.animation = animation;
    }

    @Override
    public TextureRegion prepareGraphic() {
        stateTime += Gdx.graphics.getDeltaTime() + speed;
        if (!isLoop) {
            isFinish = animation.isAnimationFinished(stateTime);
        }
        
        return (TextureRegion) animation.getKeyFrame(stateTime, isLoop);
    }

}
