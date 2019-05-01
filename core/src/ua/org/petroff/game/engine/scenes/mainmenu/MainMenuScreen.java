package ua.org.petroff.game.engine.scenes.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.scenes.Interface.ScreenLoadResourceInterface;
import ua.org.petroff.game.engine.util.Assets;

public class MainMenuScreen extends ScreenAdapter implements ScreenLoadResourceInterface {

    private final static int WIDTH_BUTTON = 100;
    private final static int HEIGHT_BUTTON = 50;
    private final static int PADDING_BOTTOM_BUTTON = 5;

    private Viewport viewport;
    private final Assets assets;
    private Stage stage;
    private Table table;
    private TextButton buttonStart;
    private TextButton buttonQuit;
    private Image background;

    public MainMenuScreen(Assets assets) {
        this.assets = assets;
    }

    @Override
    public void load() {
        viewport = new FillViewport(Settings.APP_WIDTH, Settings.APP_HEIGHT);
        assets.loadAtlas();
        stage = new Stage(viewport);

        Skin skin = new Skin(assets.loadSkin("uiskin.json"));
        table = new Table();
        buttonStart = new TextButton("Start", skin);
        buttonQuit = new TextButton("Quit", skin);

        background = new Image();
        table.setFillParent(true);
        table.setDebug(true);
    }

    @Override
    public void init() {
    }

    public Stage getStage() {
        return stage;
    }

    public TextButton getButtonStart() {
        return buttonStart;
    }

    public TextButton getButtonQuit() {
        return buttonQuit;
    }

    private void composited() {
        TextureAtlas atlas = assets.getAtlas();
        TextureAtlas.AtlasRegion textureBackground = atlas.findRegion("mainmenu-background");

        background.setDrawable(new TextureRegionDrawable(new TextureRegion(textureBackground)));
        background.setSize(textureBackground.getTexture().getWidth(), textureBackground.getTexture().getHeight());
        stage.addActor(background);
        stage.addActor(table);
        table.row().width(MainMenuScreen.WIDTH_BUTTON).height(MainMenuScreen.HEIGHT_BUTTON)
                .padBottom(MainMenuScreen.PADDING_BOTTOM_BUTTON);
        table.add(buttonStart);

        table.row().width(MainMenuScreen.WIDTH_BUTTON).height(MainMenuScreen.HEIGHT_BUTTON)
                .padBottom(MainMenuScreen.PADDING_BOTTOM_BUTTON);
        table.add(buttonQuit);
    }

    @Override
    public void show() {
        super.show();
        composited();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

}
