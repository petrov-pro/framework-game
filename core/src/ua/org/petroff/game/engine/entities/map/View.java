package ua.org.petroff.game.engine.entities.map;

import box2dLight.DirectionalLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import ua.org.petroff.game.engine.interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.interfaces.ViewInterface;
import ua.org.petroff.game.engine.util.Assets;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.scenes.core.CameraBound;
import ua.org.petroff.game.engine.scenes.core.DebugWorld;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class View implements ViewInterface, GraphicQueueMemberInterface {

    static final int RAYS_PER = 128;

    private final Assets assets;
    private OrthogonalTiledMapRenderer renderer;
    private final int[] mapLayers = {0};
    private final int[] foregroundLayers = {1};
    private Viewport viewport;
    private final GameWorld map;
    private final GraphicResources graphicResources;
    private DirectionalLight sun;
    private float sunDirection = 250f;
    private BackGround background;

    public View(Assets assets, GraphicResources graphicResources, GameWorld map) {
        this.assets = assets;
        this.map = map;
        this.graphicResources = graphicResources;
        init();
    }

    private void init() {
        renderer = new OrthogonalTiledMapRenderer(((TiledMap) assets.getMap()), Settings.SCALE, new SpriteBatch());
        CameraBound camera = new CameraBound();
        camera.position.set(map.getCameraPosition(), 0);
        camera.setWorldBounds(map.gameResources.getWorldWidth(), map.gameResources.getWorldHeight());
        viewport = new FillViewport(Settings.WIDTH, Settings.HEIGHT, camera);
        share(graphicResources);
        initLight();
        initSun();

        background = new BackGround(camera, assets, 4, 10f);

        renderer.setView(camera);
        renderer.getBatch().setProjectionMatrix(camera.combined);
    }

    private void initLight() {
        RayHandler rayHandler = new RayHandler(map.gameResources.getWorld());
        rayHandler.setAmbientLight(0f, 0f, 0f, 0.5f);
        rayHandler.setBlurNum(3);
        rayHandler.setShadows(Settings.IS_SHADOW);
        rayHandler.setCulling(true);
        graphicResources.setRayHandler(rayHandler);
    }

    private void initSun() {
        sun = new DirectionalLight(graphicResources.getRayHandler(), 4 * RAYS_PER, null, sunDirection);
    }

    @Override
    public void prepareDraw(java.util.Map<Integer, QueueDrawInterface> queueDraw) {

        queueDraw.put(QueueDraw.Z_INDEX_START, new QueueDrawInterface() {

            boolean directionForward = true;

            @Override
            public void draw() {

                graphicResources.getCamera().update();
                renderer.setView((OrthographicCamera) graphicResources.getCamera());

                background.drawBackground((SpriteBatch) renderer.getBatch());
                renderer.render(mapLayers);

                graphicResources.getRayHandler().setCombinedMatrix(graphicResources.getCamera());
                graphicResources.getRayHandler().updateAndRender();
                if (sunDirection < 200f) {
                    sunDirection += Gdx.graphics.getDeltaTime();
                } else if (sunDirection > 340f) {
                    sunDirection -= Gdx.graphics.getDeltaTime();
                }
                sunDirection = MathUtils.clamp(sunDirection, 200f, 340f);
                sun.setDirection(sunDirection);

                renderer.getBatch().begin();
            }

        });
        queueDraw.put(QueueDraw.Z_INDEX_END - 1, new QueueDrawInterface() {
            @Override
            public void draw() {
                renderer.getBatch().end();
                renderer.render(foregroundLayers);
                if (Settings.IS_DEBUG) {
                    DebugWorld.run(map.gameResources.getWorld(), graphicResources.getViewport().getCamera().combined);
                }
            }

        });
    }

    private void share(GraphicResources graphicResources) {
        graphicResources.setViewport(viewport);
        graphicResources.setSpriteBatch((SpriteBatch) renderer.getBatch());
    }

}
