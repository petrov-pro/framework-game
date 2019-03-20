package ua.org.petroff.game.engine.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.entities.EntityInterface;
import ua.org.petroff.game.engine.entities.MoveEntityInterface;
import ua.org.petroff.game.engine.entities.ViewInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.MapHelper;

public class Player implements EntityInterface, MoveEntityInterface {

    private final int zIndex = 2;
    public static final String OBJECT_NAME = "start player";
    public static final String DESCRIPTOR = "Player";
    public Actions state;
    private final Assets asset;
    private Body body;
    private Float velocityX = 5f;
    private Float velocityY = 0f;

    public enum Actions {
        MOVELEFT, MOVERIGHT, JUMP, STAY
    };
    public View view;

    public Player(Assets asset) {
        view = new View(asset, this);
        state = Actions.STAY;
        this.asset = asset;
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public void init(GameResources gameResources) {
        MapObject playerObject = ua.org.petroff.game.engine.util.MapHelper.findObject(asset.getMap(),
                OBJECT_NAME);
        createBody(gameResources, playerObject);
        int x = playerObject.getProperties().get("x", Float.class).intValue();
        int y = playerObject.getProperties().get("y", Float.class).intValue();
        Vector2 position = new Vector2(MapHelper.coordinateToWorld(x), MapHelper.coordinateToWorld(y));
        body.setTransform(position, 0);

    }

    private void createBody(GameResources gameResources, MapObject playerObject) {
        float width = MapHelper.coordinateToWorld(playerObject.getProperties().get("width", Float.class).intValue());
        float height = MapHelper.coordinateToWorld(playerObject.getProperties().get("height", Float.class).intValue());
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        body = gameResources.getWorld().createBody(bodyDef);
        PolygonShape poly = new PolygonShape();
        Vector2 center = new Vector2(width / 2, height / 2.2f);
        poly.setAsBox(width / 4.5f, height / 2.5f, center, 0);
        body.createFixture(poly, 1);
        poly.dispose();
    }

    @Override
    public void left() {
        state = Actions.MOVELEFT;
        body.setLinearVelocity(-velocityX, velocityY);
    }

    @Override
    public void right() {
        state = Actions.MOVERIGHT;
        body.setLinearVelocity(velocityX, velocityY);
    }

    @Override
    public void stop() {
        state = Actions.STAY;
    }

    @Override
    public void update() {
        Gdx.app.log("Velocity", "" + body.getLinearVelocity().x);
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

}
