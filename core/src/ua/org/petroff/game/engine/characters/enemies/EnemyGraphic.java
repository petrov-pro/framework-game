package ua.org.petroff.game.engine.characters.enemies;

import ua.org.petroff.game.engine.interfaces.GraphicLoaderInterface;
import ua.org.petroff.game.engine.interfaces.SkinInterface;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;

public class EnemyGraphic extends ua.org.petroff.game.engine.characters.base.Graphic {

    public EnemyGraphic(
            Assets asset,
            GraphicResources graphicResources,
            GraphicLoaderInterface graphicLoader,
            SkinInterface.Type typeSkin,
            String regionName,
            float velocityFireAnimation
    ) {
        super(asset, graphicResources);
        graphics.put(
                typeSkin,
                graphicLoader.loadAnimation(sprite, asset, regionName, velocityFireAnimation)
        );
    }

}
