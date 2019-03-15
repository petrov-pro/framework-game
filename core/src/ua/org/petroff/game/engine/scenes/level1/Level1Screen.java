package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import java.util.HashMap;
import java.util.Map;
import ua.org.petroff.game.engine.entities.EntityInterface;
import ua.org.petroff.game.engine.entities.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.entities.QueueDrawInterface;
import ua.org.petroff.game.engine.scenes.Interface.ScreenLoadResourceInterface;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class Level1Screen extends ScreenAdapter implements ScreenLoadResourceInterface {

    private HashMap<String, EntityInterface> entities;
    private Map<Integer, QueueDrawInterface> queueDraw = new QueueDraw<>();
    private GraphicResources graphicResources;

    @Override
    public void load() {

    }

    @Override
    public void init() {
        graphicResources = new GraphicResources();
        for (EntityInterface entity : entities.values()) {
            ((GraphicQueueMemberInterface) entity.getView()).share(graphicResources);
        }
    }

    public void setEntities(HashMap<String, EntityInterface> entities) {
        this.entities = entities;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        clearScreen();
        fillGraphicQueue();
        renderGraphicQueue();
        graphicResources.getCamera().update();
    }

    @Override
    public void resize(int width, int height) {
        graphicResources.getViewport().update(width, height);
    }

    private void clearScreen() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void fillGraphicQueue() {
        queueDraw.clear();
        for (EntityInterface entity : entities.values()) {
            queueDraw = ((GraphicQueueMemberInterface) entity.getView()).prepareDraw(graphicResources, queueDraw);
        }
    }

    private void renderGraphicQueue() {
        for (QueueDrawInterface entite : queueDraw.values()) {
            entite.draw(graphicResources);
        }
    }

}
