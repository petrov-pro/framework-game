package ua.org.petroff.game.engine.entities.weapons.ranged;

import com.badlogic.gdx.ai.msg.Telegraph;
import java.util.ArrayList;
import ua.org.petroff.game.engine.interfaces.EntityInterface;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.interfaces.SupplierViewInterface;
import ua.org.petroff.game.engine.interfaces.ViewInterface;
import ua.org.petroff.game.engine.weapons.WeaponInterface;
import ua.org.petroff.game.engine.weapons.WeaponListener;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class RangedWeaponFactory implements EntityInterface, SupplierViewInterface, Telegraph {

    public static final String DESCRIPTOR = "arrow";
    public static final int MAX = 500;

    private final View view;
    private final GameResources gameResources;
    private final ArrayList<Arrow> arrows = new ArrayList<>();

    public RangedWeaponFactory(Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        view = new View(asset, graphicResources, this);
        this.gameResources = gameResources;
        gameResources.getWorldContactListener().addUniqListener(new WeaponListener(gameResources));
        gameResources.getMessageManger().addListener(this, StateInterface.State.FIRE.telegramNumber);
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

    private void shoot(Telegram telegram) {
        arrows.add(new Arrow(gameResources, telegram.getX(), telegram.getY(), telegram.getAngular(), telegram.getForceX(), telegram.getForceY()));
        if (arrows.size() > MAX) {
            arrows.remove(0).destroy();
        }
    }

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }

    @Override
    public boolean handleMessage(com.badlogic.gdx.ai.msg.Telegram tlgrm) {
        if (StateInterface.State.getStateBy(tlgrm.message) == StateInterface.State.FIRE
                && tlgrm.extraInfo instanceof Telegram
                && WeaponInterface.rangedWeapon.contains(((Telegram) tlgrm.extraInfo).getType())) {
            shoot((Telegram) tlgrm.extraInfo);
        }

        return true;
    }

}
