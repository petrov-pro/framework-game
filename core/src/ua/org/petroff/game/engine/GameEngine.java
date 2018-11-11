package ua.org.petroff.game.engine;

import ua.org.petroff.game.engine.scenes.ManagerScenes;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import ua.org.petroff.game.engine.scenes.mainmenu.MainMenuHandler;

public class GameEngine extends Game {

    @Override
    public void create() {
        ManagerScenes managerScreen = new ManagerScenes(this);
        managerScreen.load(MainMenuHandler.DESCRIPTOR);
    }

}
