package ua.org.petroff.game.engine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ua.org.petroff.game.engine.GameEngine;
import ua.org.petroff.game.engine.Settings;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = Settings.GAME_NAME;
        config.width = Settings.APP_WIDTH;
        config.height = Settings.APP_HEIGHT;
        new LwjglApplication(new GameEngine(), config);
    }
}
