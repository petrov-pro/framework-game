package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import java.util.HashMap;
import ua.org.petroff.game.engine.entities.ViewInterface;
import ua.org.petroff.game.engine.util.Assets;

public class View implements ViewInterface {

    private final Assets asset;
    private final HashMap<Player.Actions, Object> graphics = new HashMap();
    private final Player model;
    private float stateTime = 0;
    private TextureRegion currentFrame;
    private Object graphic;

    public View(Assets asset, Player model) {
        this.asset = asset;
        this.model = model;
    }

    @Override
    public void loadResources() {
        this.asset.loadTexture("player", Assets.IMAGE_ENTITIES_PATH + "/player/player.png");
    }

    @Override
    public void init() {
        Texture playerMT = asset.get("player");
        TextureRegion[] playerRegions = new TextureRegion[7];
        TextureRegion[] playerRegions2 = new TextureRegion[7];
        for (int i = 0; i < 7; i++) {
            playerRegions[i] = new TextureRegion(playerMT, 64 * i, 704, 64, 64);
        }
        TextureRegion player = new TextureRegion(playerMT, 0, 384, 64, 64);
        Animation walkAnimationRight = new Animation(0.1f, (Object) playerRegions);

        for (int i = 0; i < 7; i++) {
            playerRegions2[i] = new TextureRegion(playerMT, 64 * i, 576, 64, 64);
        }
        Animation walkAnimationLeft = new Animation(0.1f, (Object) playerRegions2);
        graphics.put(Player.Actions.STAY, player);
        graphics.put(Player.Actions.MOVERIGHT, walkAnimationRight);
        graphics.put(Player.Actions.MOVELEFT, walkAnimationLeft);
    }

    @Override
    public void draw(Camera camera, SpriteBatch spriteBatch) {
        camera.position.set(0, 0, 0);
        stateTime += Gdx.graphics.getDeltaTime();

        graphic = graphics.get(model.state);
        if (graphic instanceof TextureRegion) {
            currentFrame = (TextureRegion) graphic;
        } else {
            currentFrame = (TextureRegion) ((Animation) graphic).getKeyFrame(stateTime, true);
        }
        spriteBatch.draw(currentFrame, model.getPosition().x, model.getPosition().y, 6, 6);
    }

}
