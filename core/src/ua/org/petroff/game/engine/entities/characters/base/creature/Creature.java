package ua.org.petroff.game.engine.entities.characters.base.creature;

import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.EntityNotifierInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.characters.enemies.skeleton.Skeleton;
import ua.org.petroff.game.engine.entities.characters.enemies.skeleton.CreatureState;
import ua.org.petroff.game.engine.entities.ia.Box2dLocation;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.MapResolver;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;

abstract public class Creature implements EntityInterface, EntityNotifierInterface, StateInterface, CreatureInterface {

    protected StateMachine<Skeleton, CreatureState> stateMachine;

    protected int zIndex;

    protected Body body;
    protected int currentLive = 100;

    protected Assets asset;
    protected GameResources gameResources;
    protected GraphicResources graphicResources;
    protected View view;

    protected boolean isGround = true;

    protected WorldInterface.Vector vector = WorldInterface.Vector.STAY;

    protected float bodyWidth = 1f;
    protected float bodyHeight = 1.45f;
    protected Vector2 centerFoot;
    protected Vector2 placeHit;

    protected Box2dLocation target;

    public Creature(String objectName, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        this.asset = asset;
        this.gameResources = gameResources;
        this.graphicResources = graphicResources;

        createBody(gameResources);
        setStartCreaturePostion(objectName);
    }

    public StateMachine<Skeleton, CreatureState> getStateMachine() {
        return stateMachine;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return stateMachine.handleMessage(msg);
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    @Override
    public boolean isGrounded() {
        return isGround;
    }

    @Override
    public void decreaseLive(int amount, Vector2 position) {
        currentLive = currentLive - amount;
        placeHit = position;
    }

    public Vector2 getPlaceHit() {
        return placeHit;
    }

    //+
    public WorldInterface.Vector getVector() {
        return vector;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public int getCurrentLive() {
        return currentLive;
    }

    @Override
    public void grounded() {
        isGround = true;
        view.resetState(StateInterface.State.JUMP);
    }

    @Override
    public void died() {
    }

    public Body getBody() {
        return body;
    }

    //+
    public boolean isShow() {
        return graphicResources.getCamera().frustum.pointInFrustum(body.getPosition().x, body.getPosition().y, 0);
    }

    @Override
    public StateInterface.State getState() {
        State action = State.valueOf(stateMachine.getCurrentState().toString());
        return action;

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

        centerFoot = bodyPlayer.getBody().getWorldCenter();
        poly.setAsBox((bodyWidth / 2f) - 0.05f, 0.05f, centerFoot.cpy().sub(0, bodyHeight / 2), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.density = 1;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);
        poly.dispose();
    }

    protected void setStartCreaturePostion(String objectName) {
        MapObject playerObject = ua.org.petroff.game.engine.util.MapResolver.findObject(asset.getMap(),
                objectName);
        int x = playerObject.getProperties().get("x", Float.class).intValue();
        int y = playerObject.getProperties().get("y", Float.class).intValue();
        Vector2 position = new Vector2(MapResolver.coordinateToWorld(x), MapResolver.coordinateToWorld(y));
        body.setTransform(position, 0);
    }

}
