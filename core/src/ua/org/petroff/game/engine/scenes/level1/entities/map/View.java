package ua.org.petroff.game.engine.scenes.level1.entities.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import ua.org.petroff.game.engine.entities.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.ViewInterface;
import ua.org.petroff.game.engine.util.Assets;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.QueueDrawInterface;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class View implements ViewInterface, GraphicQueueMemberInterface {

    private final int zIndex = 0;
    private final int zIndexEnd = 100;
    private final Assets assets;
    private OrthogonalTiledMapRenderer renderer;
    private final int[] backgroundLayers = {0, 2};
    private final int[] foregroundLayers = {1};
    private final float unitScale = 64f;
    private Viewport viewport;
    private OrthographicCamera camera;
    private QueueDrawInterface drawBackground;
    private QueueDrawInterface drawBackgroundEnd;

    public View(Assets assets) {
        this.assets = assets;
    }

    @Override
    public void loadResources() {
        assets.loadMap("map-level1", "level1/level1.tmx");
    }

    @Override
    public void init() {
        renderer = new OrthogonalTiledMapRenderer(((TiledMap) assets.get("map-level1")), 1 / unitScale, new SpriteBatch());
        camera = new OrthographicCamera();
        camera.position.set(0, 0, 0);
        viewport = new FillViewport(Settings.WIDTH, Settings.HEIGHT, camera);
        renderer.setView(camera);
        drawBackground = new QueueDrawInterface() {
            @Override
            public void draw(GraphicResources graphicResources) {
                renderer.setView((OrthographicCamera) graphicResources.getCamera());
                renderer.render(backgroundLayers);
                renderer.render(foregroundLayers);
                renderer.getBatch().begin();
            }

        };

        drawBackgroundEnd = new QueueDrawInterface() {
            @Override
            public void draw(GraphicResources graphicResources) {
                renderer.getBatch().end();
            }

        };
    }

    @Override
    public java.util.Map<Integer, QueueDrawInterface> prepareDraw(GraphicResources graphicResources, java.util.Map<Integer, QueueDrawInterface> queueDraw) {
        queueDraw.put(zIndex, drawBackground);
        queueDraw.put(zIndexEnd, drawBackgroundEnd);
        return queueDraw;
    }

    @Override
    public void share(GraphicResources graphicResources) {
        graphicResources.setCamera(camera);
        graphicResources.setViewport(viewport);
        graphicResources.setSpriteBatch((SpriteBatch) renderer.getBatch());
    }

}
