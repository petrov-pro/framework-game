package ua.org.petroff.game.engine.entities.player.graphics;

import ua.org.petroff.game.engine.entities.characters.base.BaseGraphic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ActionInterface;

public class Animation extends BaseGraphic {

    protected com.badlogic.gdx.graphics.g2d.Animation animation;
    private float speed;

    public Animation(com.badlogic.gdx.graphics.g2d.Animation animation, ActionInterface.Type action, WorldInterface.Vector vector, boolean isLoop, float speed) {
        super(action, vector, isLoop);
        this.animation = animation;
        this.speed = speed;
    }

    public Animation(com.badlogic.gdx.graphics.g2d.Animation animation, ActionInterface.Type action, WorldInterface.Vector vector, boolean isLoop) {
        super(action, vector, isLoop);
        this.animation = animation;
        this.speed = 0f;
    }

    public Animation(com.badlogic.gdx.graphics.g2d.Animation animation, ActionInterface.Type action, boolean isLoop) {
        super(action, isLoop);
        this.animation = animation;
        this.speed = 0f;
    }

    public Animation(com.badlogic.gdx.graphics.g2d.Animation animation, ActionInterface.Type action, boolean isLoop, float speed) {
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
