package ua.org.petroff.game.engine.scenes.level1.entities.map.surface;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import java.util.ArrayList;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.entities.BodyDescriber;
import ua.org.petroff.game.engine.entities.GroupDescriber;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class Surface {

    public static final String DESCRIPTOR = "surface";
    public static final String NAME = "ground";

    public static final String DAMAGE_TYPE = "damage";
    public static final String DEAD_TYPE = "dead";
    public static final String PLATFORM_TYPE = "platform";

    public static final int DAMAGE = 100;
    public static final String DAMAGE_FIELD = "damage";

    private final float friction = 2f;
    private final Assets asset;
    private final GameResources gameResources;

    public Surface(Assets asset, GameResources gameResources, GraphicResources graphicResources) {
        this.asset = asset;
        this.gameResources = gameResources;
        createSurface();
    }

    private void createSurface() {
        com.badlogic.gdx.maps.Map map = asset.getMap();

        ArrayList<MapObject> groundObjects = ua.org.petroff.game.engine.util.MapResolver.findObject(asset.getMap(),
                GroupDescriber.SURFACE);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        for (MapObject groundObject : groundObjects) {

            Float friction = groundObject.getProperties().get("friction", Float.class);

            String type = groundObject.getProperties().get("type", String.class);

            if (null == friction) {
                friction = this.friction;
            }

            Body body = gameResources.getWorld().createBody(bodyDef);

            float[] vertices = ((PolylineMapObject) groundObject).getPolyline().getTransformedVertices();
            Vector2[] worldVertices = new Vector2[vertices.length / 2];

            for (int i = 0; i < vertices.length / 2; ++i) {
                worldVertices[i] = new Vector2();
                worldVertices[i].x = vertices[i * 2] * Settings.SCALE;
                worldVertices[i].y = vertices[i * 2 + 1] * Settings.SCALE;
            }

            ChainShape chain = new ChainShape();
            chain.createChain(worldVertices);

            FixtureDef groundFixture = new FixtureDef();
            groundFixture.density = 1f;
            groundFixture.shape = chain;
            groundFixture.restitution = 0f;
            groundFixture.friction = friction;

            Fixture fixture = body.createFixture(groundFixture);

            if (type != null) {
                if (type.equals(DEAD_TYPE)) {
                    BodyDescriber bodyDescriber = new BodyDescriber(NAME, DEAD_TYPE, GroupDescriber.SURFACE);
                    fixture.setUserData(bodyDescriber);
                } else if (type.equals(PLATFORM_TYPE)) {
                    BodyDescriber bodyDescriber = new BodyDescriber(NAME, PLATFORM_TYPE, GroupDescriber.SURFACE);
                    fixture.setUserData(bodyDescriber);
                }
            }
            chain.dispose();

        }
        gameResources.getWorldContactListener().addListener(new SurfaceListener(gameResources, this));
    }

}
