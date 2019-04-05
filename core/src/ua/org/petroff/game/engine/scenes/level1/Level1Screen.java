package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class Level1Screen extends ScreenAdapter {

    private final GraphicResources graphicResources;
    private final Array<EntityInterface> models;
    private final Array<QueueDrawInterface> drawings;

    public Level1Screen(Array<EntityInterface> models, Array<QueueDrawInterface> drawings, GraphicResources graphicResources) {
        this.graphicResources = graphicResources;
        this.models = models;
        this.drawings = drawings;
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
        graphicResources.getViewportHud().update(width, height);

    }

    private void clearScreen() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void handlerEntities() {
        for (EntityInterface entity : models) {
            entity.update();
        }
    }

    private void renderGraphicQueue() {
        for (QueueDrawInterface entity : drawings) {
            entity.draw();
        }
    }

}
