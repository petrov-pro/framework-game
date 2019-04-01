package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.scenes.core.CameraBound;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class View implements ViewInterface, GraphicQueueMemberInterface {

    public enum GraphicType {
        MOVELEFT, MOVERIGHT, STAY, JUMPLEFT, JUMPRIGHT, STAYJUMP, DIED
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
        graphicFrame = GraphicType.STAY;

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
                boolean t = ((Animation) graphic).isAnimationFinished(stateTime);
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
        TextureRegion[] playerRegionsDied = new TextureRegion[5];

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

        for (int i = 0; i < 5; i++) {
            playerRegionsDied[i] = new TextureRegion(playerTexture, 64 * i, 1280, 64, 64);
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
    public Map<Integer, QueueDrawInterface> prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {

        handlerGrpahicFrame();
        if (graphicFrame == GraphicType.STAY) {
            ((QueueDraw) queueDraw).putSafe(zIndex, drawStayPlayer);
        } else {
            ((QueueDraw) queueDraw).putSafe(zIndex, drawAnimationPlayer);
        }
        ((CameraBound) graphicResources.getCamera()).positionSafe(model.getCameraNewPosition());
        return queueDraw;
    }

    private void handlerGrpahicFrame() {

        if (model.isDie()) {
            graphicFrame = GraphicType.DIED;
            isLoopAnimation = false;
            speedAnimation = 1f;
            return;
        }

        if (model.isGround()) {
            isLoopAnimation = true;
            speedAnimation = 1f;
            switch (model.getVector()) {
                case RIGHT:
                    graphicFrame = GraphicType.MOVERIGHT;
                    break;
                case LEFT:
                    graphicFrame = GraphicType.MOVELEFT;
                    break;
                default:
                    graphicFrame = GraphicType.STAY;
                    break;
            }
        } else {
            isLoopAnimation = false;
            speedAnimation = 0.3f;
            switch (model.getVector()) {
                case RIGHT:
                    graphicFrame = GraphicType.JUMPRIGHT;
                    break;
                case LEFT:
                    graphicFrame = GraphicType.JUMPLEFT;
                    break;
                default:
                    graphicFrame = GraphicType.STAYJUMP;
                    break;
            }
        }
    }

    public void changeState() {
        stateTime = 0;
    }

}
