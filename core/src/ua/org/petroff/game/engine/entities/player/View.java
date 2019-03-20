package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;
import ua.org.petroff.game.engine.entities.ViewInterface;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.entities.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.entities.QueueDrawInterface;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class View implements ViewInterface, GraphicQueueMemberInterface {

    private final int zIndex = 2;
    private final Assets asset;
    private final HashMap<Player.Actions, Object> graphics = new HashMap();
    private final Player model;
    private float stateTime = 0;
    private Object graphic;
    private TextureRegion player;
    private QueueDrawInterface drawStayPlayer;
    private QueueDrawInterface drawMovePlayer;

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
        Texture playerTexture = asset.get("player");
        TextureRegion[] playerRegions = new TextureRegion[7];
        TextureRegion[] playerRegions2 = new TextureRegion[7];
        for (int i = 0; i < 7; i++) {
            playerRegions[i] = new TextureRegion(playerTexture, 64 * i, 704, 64, 64);
        }
        player = new TextureRegion(playerTexture, 0, 384, 64, 64);
        Animation walkAnimationRight = new Animation(0.1f, (Object[]) playerRegions);

        for (int i = 0; i < 7; i++) {
            playerRegions2[i] = new TextureRegion(playerTexture, 64 * i, 576, 64, 64);
        }
        Animation walkAnimationLeft = new Animation(0.1f, (Object[]) playerRegions2);
        graphics.put(Player.Actions.STAY, player);
        graphics.put(Player.Actions.MOVERIGHT, walkAnimationRight);
        graphics.put(Player.Actions.MOVELEFT, walkAnimationLeft);

        drawStayPlayer = new QueueDrawInterface() {
            @Override
            public void draw(GraphicResources graphicResources) {
                graphicResources.getSpriteBatch().draw(player, model.getPosition().x, model.getPosition().y, 2, 2);
            }

        };

        drawMovePlayer = new QueueDrawInterface() {
            @Override
            public void draw(GraphicResources graphicResources) {
                stateTime += Gdx.graphics.getDeltaTime();
                graphic = graphics.get(model.state);
                graphicResources.getSpriteBatch().draw((TextureRegion) ((Animation) graphic).getKeyFrame(stateTime, true),
                        model.getPosition().x, model.getPosition().y, 2, 2);
            }

        };
    }

    @Override
    public Map<Integer, QueueDrawInterface> prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        if (model.state == Player.Actions.STAY) {
            ((QueueDraw) queueDraw).putSafe(zIndex, drawStayPlayer);
        } else if (model.state == Player.Actions.MOVELEFT || model.state == Player.Actions.MOVERIGHT) {
            ((QueueDraw) queueDraw).putSafe(zIndex, drawMovePlayer);
        }
        return queueDraw;
    }

}
