package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import ua.org.petroff.game.engine.entities.MoveEntityInterface;
import ua.org.petroff.game.engine.scenes.Interface.ControllerInterface;
import ua.org.petroff.game.engine.scenes.core.ManagerScenes;

public class Level1Controller extends InputAdapter implements ControllerInterface {

    private final ManagerScenes managerScenes;
    private final Level1Screen screen;
    private final MoveEntityInterface entity;

    public Level1Controller(ManagerScenes managerScenes, Level1Screen level1Screen, MoveEntityInterface entity) {
        this.managerScenes = managerScenes;
        this.screen = level1Screen;
        this.entity = entity;
    }

    @Override
    public void bindControl() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {

            case Keys.RIGHT:
                entity.right();
                break;

            case Keys.LEFT:
                entity.left();
                break;

        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        entity.stop();
        return super.keyUp(keycode);
    }
    
    

}
