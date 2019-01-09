package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.scenes.Interface.ScreenLoadResourceInterface;
import ua.org.petroff.game.engine.util.Assets;

public class Level1Screen extends ScreenAdapter implements ScreenLoadResourceInterface {

    private Viewport viewport;
    private final Assets assets;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private final int[] backgroundLayers = {0, 2};
    private final int[] foregroundLayers = {1};

    public Level1Screen(Assets assets) {
        this.assets = assets;
    }

    @Override
    public void load() {
        initCamera();
        loadGraphic();
    }

    @Override
    public void show() {
        super.show();
        initMap();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        clearScreen();
        renderer.render(backgroundLayers);
        renderer.render();
        renderer.render(foregroundLayers);
        viewport.update(Settings.APP_WIDTH, Settings.APP_HEIGHT);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void initCamera() {
        camera = new OrthographicCamera();
        viewport = new FillViewport(Settings.APP_WIDTH, Settings.APP_HEIGHT, camera);
    }

    private void initMap() {
        renderer = new OrthogonalTiledMapRenderer(((TiledMap) assets.get("map-level1")));
    }

    private void loadGraphic() {
        assets.loadMap("map-level1", "level1/level1.tmx");
    }

    private void clearScreen() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
