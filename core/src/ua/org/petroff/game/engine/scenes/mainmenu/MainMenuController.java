package ua.org.petroff.game.engine.scenes.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ua.org.petroff.game.engine.scenes.Interface.ControllerInterface;
import ua.org.petroff.game.engine.scenes.core.ManagerScenes;
import ua.org.petroff.game.engine.scenes.level1.Level1Container;

public class MainMenuController implements ControllerInterface {

    private final ManagerScenes managerScenes;
    private final MainMenuScreen mainMenuScreen;

    public MainMenuController(ManagerScenes managerScenes, MainMenuScreen mainMenuScreen) {
        this.managerScenes = managerScenes;
        this.mainMenuScreen = mainMenuScreen;

    }

    private ClickListener startButton() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                managerScenes.load(Level1Container.DESCRIPTOR);
            }
        };
    }

    private ClickListener quitButton() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                managerScenes.quit();
            }
        };
    }

    @Override
    public void bindControl() {
        Gdx.input.setInputProcessor(this.mainMenuScreen.getStage());
        this.mainMenuScreen.getButtonStart().addListener(this.startButton());
        this.mainMenuScreen.getButtonQuit().addListener(this.quitButton());

    }

}
