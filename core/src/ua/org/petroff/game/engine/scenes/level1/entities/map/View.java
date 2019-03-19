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
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.entities.QueueDrawInterface;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class View implements ViewInterface, GraphicQueueMemberInterface {

    private final Assets assets;
    private OrthogonalTiledMapRenderer renderer;
    private final int[] backgroundLayers = {0, 2};
    private final int[] foregroundLayers = {1};
    private Viewport viewport;
    private OrthographicCamera camera;
    private QueueDrawInterface drawBackground;
    private QueueDrawInterface drawBackgroundEnd;
    private Map map;

    public View(Assets assets, Map map) {
        this.assets = assets;
        this.map = map;
    }

    @Override
    public void loadResources() {
        assets.loadMap("map-level1", "level1/level1.tmx");
    }

    @Override
    public void init(GraphicResources graphicResources) {
        renderer = new OrthogonalTiledMapRenderer(((TiledMap) assets.get("map-level1")), new SpriteBatch());
        camera = new OrthographicCamera();
        camera.position.set(map.getCameraPosition(), 0);
        viewport = new FillViewport(Settings.WIDTH, Settings.HEIGHT, camera);
        renderer.setView(camera);
        share(graphicResources);

        drawBackground = new QueueDrawInterface() {
            @Override
            public void draw(GraphicResources graphicResources) {
                renderer.setView((OrthographicCamera) graphicResources.getCamera());
                renderer.render(backgroundLayers);
                renderer.render(foregroundLayers);
                renderer.getBatch().begin();
                graphicResources.getCamera().update();
            }

        };

        drawBackgroundEnd = new QueueDrawInterface() {
            @Override
            public void draw(GraphicResources graphicResources) {
                renderer.getBatch().end();
                map.gameResources.getDebugRenderer().render(
                        map.gameResources.getWorld(), graphicResources.getCamera().combined);
            }

        };
    }

    @Override
    public java.util.Map<Integer, QueueDrawInterface> prepareDraw(java.util.Map<Integer, QueueDrawInterface> queueDraw) {
        queueDraw.put(QueueDraw.Z_INDEX_START, drawBackground);
        queueDraw.put(QueueDraw.Z_INDEX_END, drawBackgroundEnd);
        return queueDraw;
    }

    public void share(GraphicResources graphicResources) {
        graphicResources.setViewport(viewport);
        graphicResources.setSpriteBatch((SpriteBatch) renderer.getBatch());
    }

}
