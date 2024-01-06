package ua.org.petroff.game.engine.entities.characters.base.creature;

import com.badlogic.gdx.ai.fsm.StateMachine;
import ua.org.petroff.game.engine.entities.Interfaces.ActionInterface;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.entities.BodyDescriber;
import ua.org.petroff.game.engine.entities.GroupDescriber;
import ua.org.petroff.game.engine.entities.Interfaces.EntityInterface;
import ua.org.petroff.game.engine.entities.Interfaces.EntityNotifierInterface;
import ua.org.petroff.game.engine.entities.Interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.Interfaces.WorldInterface;
import ua.org.petroff.game.engine.entities.characters.enemies.skeleton.Skeleton;
import static ua.org.petroff.game.engine.entities.characters.enemies.skeleton.Skeleton.DESCRIPTOR;
import static ua.org.petroff.game.engine.entities.characters.enemies.skeleton.Skeleton.OBJECT_NAME;
import ua.org.petroff.game.engine.entities.characters.enemies.skeleton.CreatureState;
import ua.org.petroff.game.engine.entities.ia.Box2dLocation;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.MapResolver;

abstract public class Creature implements EntityInterface, EntityNotifierInterface, ActionInterface {

    protected StateMachine<Skeleton, CreatureState> stateMachine;

    protected int zIndex;

    protected Body body;
    protected int currentLive = 100;

    protected Assets asset;
    protected GameResources gameResources;
    protected GraphicResources graphicResources;
    protected View view;

    protected boolean isMove = false;
    protected boolean isJump = false;
    protected boolean isGround = true;
    protected boolean isDie = false;
    protected boolean isHit = false;

    protected WorldInterface.Vector vector = WorldInterface.Vector.STAY;

    protected float bodyWidth = 1f;
    protected float bodyHeight = 1.45f;
    protected Vector2 centerFoot;

    protected Box2dLocation target;

    public Creature(Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        this.asset = asset;
        this.gameResources = gameResources;
        this.graphicResources = graphicResources;

        createBody(DESCRIPTOR, gameResources);
        setStartCreaturePostion();
    }

    public StateMachine<Skeleton, CreatureState> getStateMachine() {
        return stateMachine;
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    @Override
    public boolean isGround() {
        return isGround;
    }

    @Override
    public boolean isDie() {
        return isDie;
    }

    @Override
    public boolean isHit() {
        return isHit;
    }

    @Override
    public boolean isMove() {
        return isMove;
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

    public void grounded() {
        isGround = true;
        view.resetState(ActionInterface.Type.JUMP);
    }

    @Override
    public void died() {
        isDie = true;
    }

    public Body getBody() {
        return body;
    }

    //+
    public boolean isShow() {
        return graphicResources.getCamera().frustum.pointInFrustum(body.getPosition().x, body.getPosition().y, 0);
    }

    //+
    public ActionInterface.Type getAction() {
        Type action = Type.valueOf(stateMachine.getCurrentState().toString());
        return action;

    }

    protected void createBody(String descriptor, GameResources gameResources) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        body = gameResources.getWorld().createBody(bodyDef);

        Filter filterLight = new Filter();
        filterLight.categoryBits = (short) 1;
        filterLight.groupIndex = (short) 0;
        filterLight.maskBits = (short) 0;

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(bodyWidth / 2, bodyHeight / 2);
        Fixture bodyPlayer = body.createFixture(poly, 1);
        bodyPlayer.setUserData(new BodyDescriber(descriptor, BodyDescriber.BODY, GroupDescriber.ALIVE));

        centerFoot = bodyPlayer.getBody().getWorldCenter();
        poly.setAsBox((bodyWidth / 2f) - 0.05f, 0.05f, centerFoot.cpy().sub(0, bodyHeight / 2), 0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.density = 1;
        fixtureDef.isSensor = true;
        Fixture footSensorFixture = body.createFixture(fixtureDef);
        footSensorFixture.setFilterData(filterLight);
        footSensorFixture.setUserData(new BodyDescriber(descriptor, BodyDescriber.BODY_FOOT, GroupDescriber.ALIVE));
        poly.dispose();
    }

    protected void setStartCreaturePostion() {
        MapObject playerObject = ua.org.petroff.game.engine.util.MapResolver.findObject(asset.getMap(),
                OBJECT_NAME);
        int x = playerObject.getProperties().get("x", Float.class).intValue();
        int y = playerObject.getProperties().get("y", Float.class).intValue();
        Vector2 position = new Vector2(MapResolver.coordinateToWorld(x), MapResolver.coordinateToWorld(y));
        body.setTransform(position, 0);
    }

}
