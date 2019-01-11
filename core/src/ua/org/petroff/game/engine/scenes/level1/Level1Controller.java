package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import ua.org.petroff.game.engine.scenes.Interface.ControllerInterface;
import ua.org.petroff.game.engine.scenes.core.ManagerScenes;

public class Level1Controller extends InputAdapter implements ControllerInterface {

    private final ManagerScenes managerScenes;
    private final Level1Screen screen;

    public Level1Controller(ManagerScenes managerScenes, Level1Screen level1Screen) {
        this.managerScenes = managerScenes;
        this.screen = level1Screen;
        
    }

    @Override
    public void bindControl() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {

            case Keys.RIGHT:
                break;

            case Keys.LEFT:
                break;

        }

        return super.keyDown(keycode);
    }

}
