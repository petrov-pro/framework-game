package ua.org.petroff.game.engine.scenes.level1.entities.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.util.Assets;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.scenes.core.CameraBound;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class View implements ViewInterface, GraphicQueueMemberInterface {

    private final Assets assets;
    private OrthogonalTiledMapRenderer renderer;
    private final int[] backgroundLayers = {0, 2};
    private final int[] foregroundLayers = {1};
    private Viewport viewport;
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
        renderer = new OrthogonalTiledMapRenderer(((TiledMap) assets.get("map-level1")), 1 / 32f, new SpriteBatch());
        CameraBound camera = new CameraBound();
        camera.position.set(map.getCameraPosition(), 0);
        camera.setWorldBounds(map.gameResources.getWorldWidth(), map.gameResources.getWorldHeight());
        viewport = new FillViewport(Settings.WIDTH, Settings.HEIGHT, camera);
        renderer.setView(camera);
        share(graphicResources);
        

        drawBackground = new QueueDrawInterface() {
            @Override
            public void draw(GraphicResources graphicResources) {
                renderer.setView((OrthographicCamera) graphicResources.getCamera());
                graphicResources.getCamera().update();
                renderer.getBatch().setProjectionMatrix(graphicResources.getCamera().combined);
                renderer.render(backgroundLayers);
                renderer.render(foregroundLayers);
                renderer.getBatch().begin();
            }

        };

        drawBackgroundEnd = new QueueDrawInterface() {
            @Override
            public void draw(GraphicResources graphicResources) {
                renderer.getBatch().end();
                map.gameResources.debugPhysic(graphicResources.getCamera().combined);
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
