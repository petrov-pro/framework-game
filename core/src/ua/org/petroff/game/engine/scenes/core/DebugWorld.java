package ua.org.petroff.game.engine.scenes.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import ua.org.petroff.game.engine.Settings;

public class DebugWorld {

    public static Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    public static FPSLogger fps = new FPSLogger();

    public static DebugWorld instance;

    //debug
    public static Vector2 position = new Vector2(0, 0);
    public static Color color = Color.RED;
    public static Color colorCurrent = color;
    public static float width = 0.1f;
    public static float height = 1f;
    private static final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public static void run(World world, Matrix4 combined) {
        if (Settings.IS_DEBUG) {
            fps.log();
            //Gdx.app.log("fps", " " + Gdx.graphics.getFramesPerSecond());
            debugRenderer.setDrawAABBs(true);
            debugRenderer.setDrawVelocities(true);
            debugRenderer.setDrawContacts(true);
            debugRenderer.setDrawInactiveBodies(true);
            debugRenderer.render(world, combined);
            debugView(combined);
            MessageManager.getInstance().setDebugEnabled(true);
        }
    }

    private static void debugView(Matrix4 combined) {
        if (!colorCurrent.equals(Color.CLEAR)) {
            colorCurrent = Color.CLEAR;
        } else {
            colorCurrent = color;
        }

        shapeRenderer.setProjectionMatrix(combined);
        shapeRenderer.setColor(colorCurrent);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(position.x, position.y, width, height);
        shapeRenderer.end();
        if (!position.isZero()) {
            Gdx.app.log("debug", " x: " + position.x + " y: " + position.y);
        }
    }

}
