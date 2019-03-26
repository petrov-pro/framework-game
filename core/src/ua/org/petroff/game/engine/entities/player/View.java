package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import java.util.HashMap;
import java.util.Map;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class View implements ViewInterface, GraphicQueueMemberInterface {

    public enum GraphicType {
        MOVELEFT, MOVERIGHT, STAY, JUMPLEFT, JUMPRIGHT, STAYJUMP
    };
    public View.GraphicType graphicFrame;
    public boolean isLoopAnimation = true;
    public float speedAnimation = 1f;
    public GraphicResources graphicResources;

    private final int zIndex = 2;
    private final Assets asset;
    private final HashMap<View.GraphicType, Object> graphics = new HashMap();
    private final Player model;
    private float stateTime = 0;
    private TextureRegion player;
    private QueueDrawInterface drawStayPlayer;
    private QueueDrawInterface drawAnimationPlayer;

    public View(Assets asset, Player model) {
        this.asset = asset;
        this.model = model;
    }

    @Override
    public void loadResources() {
        this.asset.loadTexture("player", Assets.IMAGE_ENTITIES_PATH + "/player/player.png");
    }

    @Override
    public void init(GraphicResources graphicResources) {
        this.graphicResources = graphicResources;
        loadAnimation();

        drawStayPlayer = new QueueDrawInterface() {
            @Override
            public void draw(GraphicResources graphicResources) {
                graphicResources.getSpriteBatch().draw(player, model.getPosition().x, model.getPosition().y, 2, 2);
            }

        };

        drawAnimationPlayer = new QueueDrawInterface() {
            @Override
            public void draw(GraphicResources graphicResources) {
                stateTime += Gdx.graphics.getDeltaTime();
                Object graphic = graphics.get(graphicFrame);
                graphicResources.getSpriteBatch()
                        .draw((TextureRegion) ((Animation) graphic).getKeyFrame(stateTime * speedAnimation, isLoopAnimation),
                                model.getPosition().x, model.getPosition().y, 2, 2);
            }

        };
    }

    private void loadAnimation() {
        Texture playerTexture = asset.get("player");
        TextureRegion[] playerRegionsRight = new TextureRegion[7];
        TextureRegion[] playerRegionsLeft = new TextureRegion[7];
        TextureRegion[] playerRegionsJumpLeft = new TextureRegion[7];
        TextureRegion[] playerRegionsJumpRight = new TextureRegion[7];
        TextureRegion[] playerRegionsJumpStay = new TextureRegion[7];

        player = new TextureRegion(playerTexture, 0, 384, 64, 64);

        for (int i = 0; i < 7; i++) {
            playerRegionsRight[i] = new TextureRegion(playerTexture, 64 * i, 704, 64, 64);
        }
        Animation walkAnimationRight = new Animation(0.1f, (Object[]) playerRegionsRight);

        for (int i = 0; i < 7; i++) {
            playerRegionsLeft[i] = new TextureRegion(playerTexture, 64 * i, 576, 64, 64);
        }
        Animation walkAnimationLeft = new Animation(0.1f, (Object[]) playerRegionsLeft);

        for (int i = 0; i < 7; i++) {
            playerRegionsJumpLeft[i] = new TextureRegion(playerTexture, 64 * i, 64, 64, 64);
        }
        Animation jumpAnimationLeft = new Animation(0.1f, (Object[]) playerRegionsJumpLeft);

        for (int i = 0; i < 7; i++) {
            playerRegionsJumpRight[i] = new TextureRegion(playerTexture, 64 * i, 192, 64, 64);
        }
        Animation jumpAnimationRight = new Animation(0.1f, (Object[]) playerRegionsJumpRight);

        for (int i = 0; i < 7; i++) {
            playerRegionsJumpStay[i] = new TextureRegion(playerTexture, 64 * i, 128, 64, 64);
        }
        Animation jumpAnimationStay = new Animation(0.1f, (Object[]) playerRegionsJumpStay);

        graphics.put(GraphicType.STAY, player);
        graphics.put(GraphicType.MOVERIGHT, walkAnimationRight);
        graphics.put(GraphicType.MOVELEFT, walkAnimationLeft);
        graphics.put(GraphicType.JUMPLEFT, jumpAnimationLeft);
        graphics.put(GraphicType.JUMPRIGHT, jumpAnimationRight);
        graphics.put(GraphicType.STAYJUMP, jumpAnimationStay);
    }

    @Override
    public Map<Integer, QueueDrawInterface> prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        if (graphicFrame == graphicFrame.STAY) {
            ((QueueDraw) queueDraw).putSafe(zIndex, drawStayPlayer);
        } else {
            ((QueueDraw) queueDraw).putSafe(zIndex, drawAnimationPlayer);
        }
        model.calculateCameraPositionForPlayer(graphicResources.getCamera().position);
        return queueDraw;
    }

    public void setDefaultAnimationParams() {
        isLoopAnimation = true;
        speedAnimation = 1f;
        stateTime = 0;
    }

}
