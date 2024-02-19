package ua.org.petroff.game.engine.entities.map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ua.org.petroff.game.engine.util.Assets;

public class BackGround {

    private final ParallaxLayer[] layers;
    private final Camera camera;
    private final Assets assets;

    public BackGround(Camera camera, Assets assets, int countBackground, float speedX) {
        this.camera = camera;
        this.assets = assets;

        layers = new ParallaxLayer[countBackground];
        float speed = 0;
        for (int i = 0; i < countBackground; i++) {
            layers[i] = new ParallaxLayer(assets.getBackground(i), speed = speed + speedX, camera);
        }
    }

    public void drawBackground(SpriteBatch batch) {
        batch.begin();
        for (ParallaxLayer layer : layers) {
            layer.render(batch);
        }
        batch.end();
    }

}
