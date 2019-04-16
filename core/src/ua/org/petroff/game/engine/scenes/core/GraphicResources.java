package ua.org.petroff.game.engine.scenes.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GraphicResources {

    private Viewport viewport;
    private Viewport viewportHud;
    private SpriteBatch spriteBatch;

    //debug
    public Vector2 position = new Vector2(18, 51);
    public Color color = Color.RED;
    public Color colorCurrent = color;
    public float width = 0.05f;
    public float height = 0.05f;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public Viewport getViewportHud() {
        return viewportHud;
    }

    public void setViewportHud(Viewport viewportHud) {
        this.viewportHud = viewportHud;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public OrthographicCamera getCamera() {
        return (OrthographicCamera) viewport.getCamera();
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    public void debugView() {
        if (!colorCurrent.equals(Color.CLEAR)) {
            colorCurrent = Color.CLEAR;
        } else {
            colorCurrent = color;
        }
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setColor(colorCurrent);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(position.x, position.y, width, height);
        shapeRenderer.end();
        //Gdx.app.log("debug", " x: " + position.x + " y: " + position.y);
    }

}
