package ua.org.petroff.game.engine;

import ua.org.petroff.game.engine.scenes.core.ManagerScenes;
import com.badlogic.gdx.Game;
import ua.org.petroff.game.engine.scenes.level1.Level1Container;

public class GameEngine extends Game {

    @Override
    public void create() {
        ManagerScenes managerScreen = new ManagerScenes(this);
        //managerScreen.load(MainMenuContainer.DESCRIPTOR);
        managerScreen.load(Level1Container.DESCRIPTOR);
    }

}
