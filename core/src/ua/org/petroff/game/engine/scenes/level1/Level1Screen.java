package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import java.util.HashMap;
import java.util.Map;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class Level1Screen extends ScreenAdapter {

    private final HashMap<String, EntityInterface> entities;
    private Map<Integer, QueueDrawInterface> queueDraw = new QueueDraw<>();
    private final GraphicResources graphicResources;

    public Level1Screen(HashMap<String, EntityInterface> entities, GraphicResources graphicResources) {
        this.entities = entities;
        this.graphicResources = graphicResources;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        clearScreen();
        handlerEntities();
        renderGraphicQueue();
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

    private void handlerEntities() {
        queueDraw.clear();
        for (EntityInterface entity : entities.values()) {
            entity.update();
            try {
                queueDraw = ((GraphicQueueMemberInterface) entity.getView()).prepareDraw(queueDraw);
            } catch (UnsupportedOperationException UNOE) {
            }

        }
    }

    private void renderGraphicQueue() {
        for (QueueDrawInterface entite : queueDraw.values()) {
            entite.draw(graphicResources);
        }
    }

}
