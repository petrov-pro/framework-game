package ua.org.petroff.game.engine.scenes.level1.entities.map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import ua.org.petroff.game.engine.entities.ViewInterface;
import ua.org.petroff.game.engine.util.Assets;

public class View implements ViewInterface {

    private final Assets assets;
    private OrthogonalTiledMapRenderer renderer;
    private final int[] backgroundLayers = {0, 2};
    private final int[] foregroundLayers = {1};
    private final float unitScale = 64f;

    public View(Assets assets) {
        this.assets = assets;
    }

    @Override
    public void loadResources() {
        assets.loadMap("map-level1", "level1/level1.tmx");
    }

    @Override
    public void init() {
        renderer = new OrthogonalTiledMapRenderer(((TiledMap) assets.get("map-level1")), 1 / unitScale);
    }

    @Override
    public void draw(Camera camera, SpriteBatch spriteBatch) {
        renderer.setView((OrthographicCamera) camera);
        renderer.render(backgroundLayers);
        renderer.render();
        renderer.render(foregroundLayers);
    }

}
