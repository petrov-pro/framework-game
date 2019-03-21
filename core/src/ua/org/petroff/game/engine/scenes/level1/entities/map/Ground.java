package ua.org.petroff.game.engine.scenes.level1.entities.map;

import com.badlogic.gdx.math.Vector2;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.util.Assets;

public class Ground {

    private final Assets asset;
    private final GameResources gameResources;

    public Ground(Assets asset, GameResources gameResources) {
        this.asset = asset;
        this.gameResources = gameResources;
    }

    public void create() {

    }

}
