package ua.org.petroff.game.engine.entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ViewInterface {

    public void loadResources();

    public void init();
    
    public void draw(Camera camera, SpriteBatch spriteBatch);
}
