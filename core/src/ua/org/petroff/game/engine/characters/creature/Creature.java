package ua.org.petroff.game.engine.characters.creature;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.interfaces.EntityInterface;
import ua.org.petroff.game.engine.interfaces.SkinInterface;
import ua.org.petroff.game.engine.interfaces.ViewInterface;
import ua.org.petroff.game.engine.interfaces.WorldInterface;
import ua.org.petroff.game.engine.ia.Box2dLocation;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.MapResolver;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.interfaces.SupplierViewInterface;

abstract public class Creature implements EntityInterface, StateInterface, CreatureInterface, SkinInterface, SupplierViewInterface {

    protected Body body;
    protected int live = 100;

    protected Assets asset;
    protected GameResources gameResources;
    protected GraphicResources graphicResources;
    protected View view;

    protected boolean onGround = true;

    protected WorldInterface.Vector vector = WorldInterface.Vector.STAY;
    protected SkinInterface.Type skin = SkinInterface.Type.DEFAULT;

    protected float bodyWidth = 1f;
    protected float bodyHeight = 1.45f;
    protected Vector2 placeHit;

    protected Box2dLocation target;

    public Creature(int x, int y, String objectName, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        this.asset = asset;
        this.gameResources = gameResources;
        this.graphicResources = graphicResources;

        createBody(gameResources);
        setStartCreaturePostion(x, y);
        gameResources.getWorldContactListener().addListener(new CreatureListener(gameResources, this));
        gameResources.getWorldContactListener().addListener(new GroundListener(gameResources, this));
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    @Override
    public boolean isGrounded() {
        return onGround;
    }

    @Override
    public void decreaseLive(int amount, Vector2 placeHit, Vector2 directionHit) {
        float force = amount * 10;
        body.applyForceToCenter(
                new Vector2(
                        (directionHit.x > 0) ? -force : force,
                        0f),
                true
        );
        decreaseLive(amount, placeHit);
    }

    @Override
    public void decreaseLive(int amount, Vector2 placeHit) {
        live = live - amount;
        this.placeHit = placeHit == null ? body.getPosition() : placeHit;
    }

    public Vector2 getPlaceHit() {
        return placeHit;
    }

    @Override
    public Type getSkinType() {
        return skin;
    }

    //+
    public WorldInterface.Vector getVector() {
        return vector;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public int getCurrentLive() {
        return live;
    }

    @Override
    public void ground(boolean isGround) {
        onGround = isGround;
        if (isGround) {
            view.resetState(StateInterface.State.JUMP);
        }

    }

    @Override
    public void died() {
        vector = WorldInterface.Vector.STAY;
        live = 0;

        ((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(bodyWidth / 2, bodyHeight / 5);
        body.resetMassData();
        Vector2 newPosition = body.getTransform().getPosition();
        body.setTransform(newPosition, 0);
    }

    public Body getBody() {
        return body;
    }

    //+
    public boolean isShow() {
        return graphicResources.getCamera().frustum.pointInFrustum(body.getPosition().x, body.getPosition().y, 0);
    }

    protected void createBody(GameResources gameResources) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        body = gameResources.getWorld().createBody(bodyDef);
        body.setUserData(this);

        Filter filterLight = new Filter();
        filterLight.categoryBits = (short) 1;
        filterLight.groupIndex = (short) 0;
        filterLight.maskBits = (short) 0;

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(bodyWidth / 2, bodyHeight / 2);

        Fixture bodyPlayer = body.createFixture(poly, 1);

        Vector2 centerFoot = bodyPlayer.getBody().getLocalCenter();
        poly.setAsBox((bodyWidth / 2f) - 0.08f, 0.1f, centerFoot.cpy().sub(0, bodyHeight / 1.8f), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.isSensor = true;
        Fixture bodyFootPlayer = body.createFixture(fixtureDef);
        bodyFootPlayer.setUserData(this);
        poly.dispose();
    }

    protected void setStartCreaturePostion(int x, int y) {
        Vector2 position = new Vector2(MapResolver.coordinateToWorld(x), MapResolver.coordinateToWorld(y));
        body.setTransform(position, 0);
    }

}
