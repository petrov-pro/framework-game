package ua.org.petroff.game.engine.entities.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import java.util.Map;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class View implements ViewInterface, GraphicQueueMemberInterface, QueueDrawInterface {

    private Stage stage;
    private ExtendViewport viewport;
    private Table table;
    // Now we create our widgets. Our widgets will be labels, essentially text, that allow us to display Game Information
    private Label countdownLabel;
    static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;

    private final Assets asset;
    private final int zIndex = 2;
    private GraphicResources graphicResources;
    private HUD model;

    public View(Assets asset, HUD model) {
        this.asset = asset;
        this.model = model;
    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putSafe(QueueDraw.Z_INDEX_END, this);
    }

    @Override
    public void loadResources() {
    }

    @Override
    public void init(GraphicResources graphicResources) {
        this.graphicResources = graphicResources;

        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, graphicResources.getSpriteBatch()); // We must create order by creating a table in our stage

        Skin skin = new Skin(asset.loadSkin("uiskin.json"));

        table = new Table(skin);
        table.top(); // Will put it at the top of our stage
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", model.worldTimer), skin);
        scoreLabel = new Label(String.format("%06d", model.score), skin);
        timeLabel = new Label("TIME", skin);
        levelLabel = new Label("WASTE LAND", skin);
        worldLabel = new Label("ROUND 1", skin);
        marioLabel = new Label("SCORE:", skin);

        table.add(marioLabel).expandX().padTop(10); // This expand X makes everything in the row share the row equally
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);

        table.row(); // THIS CREATES A NEW ROW
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        // add table to our stage
        stage.addActor(table);

        graphicResources.setViewportHud(viewport);
    }

    @Override
    public void draw() {
        stage.getViewport().apply();
        stage.act();
        stage.draw();
        countdownLabel.setText(String.format("%03d", model.worldTimer));
    }

}
