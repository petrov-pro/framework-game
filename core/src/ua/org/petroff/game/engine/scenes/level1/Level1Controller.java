package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import ua.org.petroff.game.engine.entities.Interfaces.MoveEntityInterface;
import ua.org.petroff.game.engine.entities.player.Player;
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

            case Keys.SPACE:
                entity.jump();
                break;

            case Keys.CONTROL_LEFT:
                entity.hit();
                break;

        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.RIGHT:
                entity.stop(Player.Actions.MOVE);
                break;

            case Keys.LEFT:
                entity.stop(Player.Actions.MOVE);
                break;

            case Keys.SPACE:
                entity.stop(Player.Actions.JUMP);
                break;

            case Keys.CONTROL_LEFT:
                entity.stop(Player.Actions.HIT);
                break;

        }

        return super.keyUp(keycode);
    }

}
