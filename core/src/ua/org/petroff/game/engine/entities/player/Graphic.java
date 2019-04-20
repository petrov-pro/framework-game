package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.entities.player.View.GraphicType;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class Graphic implements ViewInterface, GraphicQueueMemberInterface {

    public GraphicResources graphicResources;
    public final HashMap<View.GraphicType, Object> graphics = new HashMap();
    public Sprite sprite;

    private final int zIndex = 2;
    private final Assets asset;
    private final View view;

    public Graphic(Assets asset, View view) {
        this.asset = asset;
        this.view = view;
    }

    @Override
    public void loadResources() {
        this.asset.loadTexture("player", Assets.IMAGE_ENTITIES_PATH + "/player/player.png");
    }

    @Override
    public void init(GraphicResources graphicResources) {
        this.graphicResources = graphicResources;
        view.graphicResources = graphicResources;
        loadAnimation();
    }

    private void loadAnimation() {

        Texture playerTexture = asset.get("player");
        TextureRegion[] playerRegionsRight = new TextureRegion[7];
        TextureRegion[] playerRegionsLeft = new TextureRegion[7];
        TextureRegion[] playerRegionsJumpLeft = new TextureRegion[7];
        TextureRegion[] playerRegionsJumpRight = new TextureRegion[7];
        TextureRegion[] playerRegionsJumpStay = new TextureRegion[7];
        TextureRegion[] playerRegionsDied = new TextureRegion[5];

        TextureRegion player = new TextureRegion(playerTexture, 0, 388, 64, 64);
        sprite = new Sprite(player);
        sprite.setScale(Settings.SCALE);
        sprite.setOriginCenter();

        for (int i = 0; i < 7; i++) {
            playerRegionsRight[i] = new TextureRegion(playerTexture, 64 * i, 708, 64, 64);
        }
        Animation walkAnimationRight = new Animation(0.1f, (Object[]) playerRegionsRight);

        for (int i = 0; i < 7; i++) {
            playerRegionsLeft[i] = new TextureRegion(playerTexture, 64 * i, 580, 64, 64);
        }
        Animation walkAnimationLeft = new Animation(0.1f, (Object[]) playerRegionsLeft);

        for (int i = 0; i < 7; i++) {
            playerRegionsJumpLeft[i] = new TextureRegion(playerTexture, 64 * i, 68, 64, 64);
        }
        Animation jumpAnimationLeft = new Animation(0.1f, (Object[]) playerRegionsJumpLeft);

        for (int i = 0; i < 7; i++) {
            playerRegionsJumpRight[i] = new TextureRegion(playerTexture, 64 * i, 196, 64, 64);
        }
        Animation jumpAnimationRight = new Animation(0.1f, (Object[]) playerRegionsJumpRight);

        for (int i = 0; i < 7; i++) {
            playerRegionsJumpStay[i] = new TextureRegion(playerTexture, 64 * i, 132, 64, 64);
        }
        Animation jumpAnimationStay = new Animation(0.1f, (Object[]) playerRegionsJumpStay);

        for (int i = 0; i < 5; i++) {
            playerRegionsDied[i] = new TextureRegion(playerTexture, 64 * i, 1284, 64, 64);
        }
        Animation diedAnimationStay = new Animation(0.1f, (Object[]) playerRegionsDied);

        graphics.put(GraphicType.STAY, player);
        graphics.put(GraphicType.MOVERIGHT, walkAnimationRight);
        graphics.put(GraphicType.MOVELEFT, walkAnimationLeft);
        graphics.put(GraphicType.JUMPLEFT, jumpAnimationLeft);
        graphics.put(GraphicType.JUMPRIGHT, jumpAnimationRight);
        graphics.put(GraphicType.STAYJUMP, jumpAnimationStay);
        graphics.put(GraphicType.DIED, diedAnimationStay);
    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putSafe(zIndex, view);
    }

}
