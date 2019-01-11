package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.HashMap;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.EntityInterface;
import ua.org.petroff.game.engine.scenes.Interface.ScreenLoadResourceInterface;

public class Level1Screen extends ScreenAdapter implements ScreenLoadResourceInterface {

    private Viewport viewport;
    private OrthographicCamera camera;
    private HashMap<String, EntityInterface> entities;
    private SpriteBatch spriteBatch;

    @Override
    public void load() {
        camera = new OrthographicCamera();
        camera.position.set(5, 0, 0);
        viewport = new FillViewport(Settings.WIDTH, Settings.HEIGHT, camera);
        spriteBatch = new SpriteBatch();
    }

    public void setEntities(HashMap<String, EntityInterface> entities) {
        this.entities = entities;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        clearScreen();
        camera.update();
        spriteBatch.begin();
        for (EntityInterface entity : entities.values()) {
            entity.getView().draw(camera, spriteBatch);
        }
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void clearScreen() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
