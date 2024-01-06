package ua.org.petroff.game.engine.entities.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import java.util.Map;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.Interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.entities.Interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class View implements ViewInterface, GraphicQueueMemberInterface, QueueDrawInterface {

    private Label levelLabel;

    private Stage stage;
    private ExtendViewport viewport;
    private Table table;
    // Now we create our widgets. Our widgets will be labels, essentially text, that allow us to display Game Information
    private Label countdownLabel;
    static Label scoreLabel;
    private Label timeLabel;
    private Label worldLabel;
    private Label scoreNameLabel;
    private Label liveLabel;
    private Label deadLabel;

    private final Assets asset;
    private final GraphicResources graphicResources;
    private final HUD model;
    private Animation healthAnimation;

    public View(Assets asset, GraphicResources graphicResources, HUD model) {
        this.asset = asset;
        this.model = model;
        this.graphicResources = graphicResources;
        init();
    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putSafe(QueueDraw.Z_INDEX_END, this);
    }

    @Override
    public void draw() {
        stage.getViewport().apply();
        stage.act();
        stage.draw();
        countdownLabel.setText(model.worldTimer.toString());
        liveLabel.setText(model.currentLive.toString());
        if (model.currentLive <= 0) {
            deadLabel.setVisible(true);
        }
    }

    private void loadAnimation() {
        TextureAtlas atlas = asset.getAtlas();
        TextureAtlas.AtlasRegion playerTextureHealth = atlas.findRegion("health");
        TextureRegion[] playerRegionsHealth = new TextureRegion[7];

        for (int i = 0; i < 7; i++) {
            playerRegionsHealth[i] = new TextureRegion(playerTextureHealth, 50 * i, 0, 50, 63);
        }
        healthAnimation = new Animation(0.1f, (Object[]) playerRegionsHealth);

    }

    private void init() {
        loadAnimation();

        viewport = new ExtendViewport(Settings.APP_WIDTH, Settings.APP_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, graphicResources.getSpriteBatch()); // We must create order by creating a table in our stage

        Skin skin = new Skin(asset.loadSkin("uiskin.json"));

        table = new Table(skin);
        table.top(); // Will put it at the top of our stage
        table.setFillParent(true);

        countdownLabel = new Label(model.worldTimer.toString(), skin);
        scoreLabel = new Label(model.score.toString(), skin);
        timeLabel = new Label("TIME", skin);
        levelLabel = new Label("WASTE LAND", skin);
        worldLabel = new Label("ROUND 1", skin);

        deadLabel = new Label("YOU ARE DEAD", skin);
        deadLabel.setFontScale(3);
        deadLabel.setColor(Color.RED);
        deadLabel.setVisible(false);

        table.add(scoreNameLabel).expandX().padTop(10); // This expand X makes everything in the row share the row equally
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);

        table.row(); // THIS CREATES A NEW ROW
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        table.row().expandY().bottom();
        HorizontalGroup liveGroup = new HorizontalGroup();

        ImageAnimation image = new ImageAnimation();
        image.setAnimation(healthAnimation);
        liveGroup.left()
                .addActor(image);

        liveLabel = new Label(model.currentLive.toString(), skin);
        liveLabel.setFontScale(2);
        liveGroup.rowBottom()
                .space(5)
                .addActor(liveLabel);

        table.add(liveGroup)
                .left()
                .padLeft(50)
                .padBottom(50);

        table.add(deadLabel)
                .center();

        // add table to our stage
        stage.addActor(table);

        if (Settings.IS_DEBUG) {
            table.setDebug(true);
        }

        graphicResources.setViewportHud(viewport);
    }

}
