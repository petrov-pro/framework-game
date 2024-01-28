package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import ua.org.petroff.game.engine.entities.Interfaces.MoveEntityInterface;
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
        handlerAction(keycode, true);
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        handlerAction(keycode, false);
        return super.keyUp(keycode);
    }

    private void handlerAction(int keycode, boolean active) {
        switch (keycode) {

            case Keys.RIGHT:
                entity.right(active);
                break;

            case Keys.LEFT:
                entity.left(active);
                break;

            case Keys.SPACE:
                entity.jump(active);
                break;

            case Keys.CONTROL_LEFT:
                entity.fire(active);
                break;

            case Keys.ALT_LEFT:
                entity.ability(active);
                break;

        }
    }

}
