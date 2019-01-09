package ua.org.petroff.game.engine;

import ua.org.petroff.game.engine.scenes.core.ManagerScenes;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import ua.org.petroff.game.engine.scenes.level1.Level1Container;
import ua.org.petroff.game.engine.scenes.mainmenu.MainMenuContainer;

public class GameEngine extends Game {

    @Override
    public void create() {
        ManagerScenes managerScreen = new ManagerScenes(this);
        //managerScreen.load(MainMenuContainer.DESCRIPTOR);
        managerScreen.load(Level1Container.DESCRIPTOR);
    }

}
