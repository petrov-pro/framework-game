package ua.org.petroff.game.engine.scenes.core;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GraphicResources {

    private Viewport viewport;
    private Viewport viewportHud;
    private SpriteBatch spriteBatch;
    private RayHandler rayHandler;

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

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    public void setRayHandler(RayHandler rayHandler) {
        this.rayHandler = rayHandler;
    }

}
