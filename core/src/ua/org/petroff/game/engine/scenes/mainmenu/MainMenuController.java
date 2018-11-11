package ua.org.petroff.game.engine.scenes.mainmenu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ua.org.petroff.game.engine.scenes.ManagerScenes;

public class MainMenuController {
    
    ManagerScenes managerScenes;
    
    public MainMenuController(ManagerScenes managerScenes) {
        this.managerScenes = managerScenes;
    }
    
    public ClickListener startButton() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("hiii");
                managerScenes.load(MainMenuHandler.DESCRIPTOR);
            }
        };
    }
    
}
