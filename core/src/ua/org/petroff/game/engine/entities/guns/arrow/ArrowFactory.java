package ua.org.petroff.game.engine.entities.guns.arrow;

import java.util.ArrayList;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.guns.GunListener;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class ArrowFactory implements EntityInterface {

    public static final String DESCRIPTOR = "arrow";
    public static final int MAX = 50;

    private final View view;
    private final GameResources gameResources;
    private final ArrayList<Arrow> arrows = new ArrayList<>();

    public ArrowFactory(Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        view = new View(asset, graphicResources, this);
        this.gameResources = gameResources;
        new Telegraph(this, gameResources);
        gameResources.getWorldContactListener().addListener(new GunListener(gameResources));
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    @Override
    public void update() {
        for (Arrow arrow : arrows) {
            arrow.handleActive();
        }
    }

    public void shoot(float x, float y, float angular, float forceX, float forceY) {
        arrows.add(new Arrow(gameResources, x, y, angular, forceX, forceY));
        if (arrows.size() > MAX) {
            (arrows.get(0)).destroy();
            arrows.remove(0);

        }
    }

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }

}
