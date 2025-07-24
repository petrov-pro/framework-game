package ua.org.petroff.game.engine.entities.equipments;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ua.org.petroff.game.engine.interfaces.EntityInterface;
import ua.org.petroff.game.engine.interfaces.SupplierViewInterface;
import ua.org.petroff.game.engine.interfaces.ViewInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.util.MapResolver;

abstract public class Equipment implements EntityInterface, SupplierViewInterface {

    protected Fixture bodyEquipment;
    protected Body body;
    protected Assets asset;
    protected GameResources gameResources;
    protected GraphicResources graphicResources;
    protected ViewInterface view;

    public Equipment(int x, int y, int bodyWidth, int bodyHeight, Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        this.asset = asset;
        this.gameResources = gameResources;
        this.graphicResources = graphicResources;
        createBody(bodyWidth, bodyHeight, gameResources);
        setStartCreaturePostion(x, y);
    }

    @Override
    public void update() {
    }

    @Override
    public ViewInterface getView() {
        return view;
    }

    public Body getBody() {
        return body;
    }

    protected void createBody(int bodyWidth, int bodyHeight, GameResources gameResources) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = false;
        body = gameResources.getWorld().createBody(bodyDef);
        body.setUserData(this);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(MapResolver.coordinateToWorld(bodyWidth), MapResolver.coordinateToWorld(bodyHeight));

        
        bodyEquipment = body.createFixture(poly, 1);
        bodyEquipment.setUserData(this);

        poly.dispose();
    }

    protected void setStartCreaturePostion(int x, int y) {
        Vector2 position = new Vector2(MapResolver.coordinateToWorld(x), MapResolver.coordinateToWorld(y));
        body.setTransform(position, 0);
    }

}
