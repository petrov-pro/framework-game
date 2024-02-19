package ua.org.petroff.game.engine.entities.map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ParallaxLayer {

    private final Texture texture;
    private final float factor;
    private final Camera camera;
    private final TextureRegion region;
    private int xOffset;
    private int yOffset;

    ParallaxLayer(Texture texture, float factor, Camera camera) {
        this.texture = texture;
        this.factor = factor;
        this.camera = camera;
        this.texture.setWrap(
                Texture.TextureWrap.Repeat,
                Texture.TextureWrap.ClampToEdge
        );

        region = new TextureRegion(texture);
    }

    public void render(SpriteBatch batch) {
        xOffset = (int) (camera.position.x * factor);
        yOffset = (int) (camera.position.y * 0.1);
        region.setRegionX(xOffset % texture.getWidth());
        region.setRegionY(yOffset % texture.getHeight());
        region.setRegionWidth(texture.getWidth());
        region.setRegionHeight(texture.getHeight());
        batch.draw(
                region,
                camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2,
                xOffset % texture.getWidth(),
                yOffset % texture.getHeight(),
                camera.viewportWidth,
                camera.viewportHeight,
                1f,
                1f,
                0
        );
    }
}
