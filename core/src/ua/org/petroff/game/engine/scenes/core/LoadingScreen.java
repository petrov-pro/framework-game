package ua.org.petroff.game.engine.scenes.core;

import ua.org.petroff.game.engine.scenes.Interface.ContainerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ua.org.petroff.game.engine.util.Assets;

public class LoadingScreen extends ScreenAdapter {

    private final TextureAtlas.AtlasRegion backGround;
    private final GlyphLayout layout = new GlyphLayout();
    private String message;
    private ContainerInterface returnScene;
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font = new BitmapFont();
    private final ManagerScenes managerScenes;
    private float progress = 0;

    public LoadingScreen(ManagerScenes managerScenes) {
        this.managerScenes = managerScenes;
        Assets assets = new Assets();
        assets.loadAtlas();
        assets.getManager().finishLoading();
        TextureAtlas atlas = assets.getAtlas();
        backGround = atlas.findRegion("backgroud-loading");
    }

    public void setReturnScene(ContainerInterface returnScene) {
        this.returnScene = returnScene;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void show() {
        super.show();
        message = returnScene.getSceneName();
        returnScene.load();
    }

    @Override
    public void render(float delta) {
        if (returnScene.getAssets().isUploaded()) {
            returnScene.init();
            managerScenes.setScreen(returnScene.getView());
        } else {
            progress = returnScene.getAssets().getManager().getProgress();
        }
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        layout.setText(font, message);
        float widthApp = Gdx.graphics.getWidth();
        float width = widthApp / 2 - layout.width;
        batch.draw(backGround, 0, 0);
        font.draw(batch, message, width, Gdx.graphics.getHeight() / 2);
        batch.end();

    }

}
