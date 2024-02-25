package ua.org.petroff.game.engine.entities.cloud;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import ua.org.petroff.game.engine.interfaces.EntityInterface;
import ua.org.petroff.game.engine.interfaces.SupplierViewInterface;
import ua.org.petroff.game.engine.interfaces.ViewInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.RandomGenerate;

public class CloudFactory implements EntityInterface, SupplierViewInterface {

    public static final String DESCRIPTOR = "cloud";
    public static final int DEFAULT = 30;

    private final View view;
    private final GameResources gameResources;
    private final ArrayList<Cloud> clouds = new ArrayList<>();
    private float forceX = 0.01f;

    public CloudFactory(Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        view = new View(asset, graphicResources, this);
        this.gameResources = gameResources;
        create(DEFAULT);
    }

    public CloudFactory(Assets asset, GameResources gameResources, GraphicResources graphicResources, int numberClouds, int forceX) {
        this.forceX = forceX;
        view = new View(asset, graphicResources, this);
        this.gameResources = gameResources;
        create(numberClouds);
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    @Override
    public void update() {
        for (Cloud cloud : clouds) {
            if (cloud.getBody().getPosition().x > gameResources.getWorldWidth()) {
                cloud.getBody().setTransform(-2, cloud.getBody().getPosition().y, 0f);
            }
        }
    }

    public void create(int numberCloud) {
        for (int i = 1; i <= numberCloud; i++) {
            int x = RandomGenerate.generate(0, gameResources.getWorldWidth());
            int y = RandomGenerate.generate((int) (gameResources.getWorldHeight() / 1.3f), gameResources.getWorldHeight());
            clouds.add(new Cloud(gameResources, x, y, forceX));
        }
    }

    public ArrayList<Cloud> getClouds() {
        return clouds;
    }

}
