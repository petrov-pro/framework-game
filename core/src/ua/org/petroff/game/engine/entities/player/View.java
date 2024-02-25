package ua.org.petroff.game.engine.entities.player;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Filter;
import java.util.Map;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.scenes.core.CameraBound;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.interfaces.StateInterface;
import ua.org.petroff.game.engine.characters.enemies.EnemyGraphic;
import ua.org.petroff.game.engine.characters.visual.effects.Blood;

public class View extends ua.org.petroff.game.engine.characters.creature.View implements ViewInterface, QueueDrawInterface, GraphicQueueMemberInterface {

    public static final int ZINDEX = 50;

    private static final int RAYS_PER = 128;
    private static final float LIGHT_DISTANCE = 16f;

    private Player.PlayerSize currentPlayerSize;
    private PointLight light;
    private Filter filterLight;
    private ParticleEffect effect;

    private boolean canDrawGroundEffect = true;

    public View(Assets asset, GraphicResources graphicResources, Player model) {
        super(
                asset,
                graphicResources,
                model,
                new Graphic(asset, graphicResources),
                new Blood(asset, graphicResources)
        );
        initLight();
        loadParticle();
    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putSafe(ZINDEX, this);
    }

    @Override
    public void draw() {
        changeSize();
        super.draw();
        groundedEffect();
        ((CameraBound) graphicResources.getCamera()).positionSafe(((Player) model).getCameraNewPosition());
    }

    private void changeSize() {
        if (currentPlayerSize != ((Player) model).getPlayerSize()) {
            if (((Player) model).getPlayerSize().equals(Player.PlayerSize.NORMAL)) {
                graphic.sprite.setScale(Settings.SCALE);
            } else {
                graphic.sprite.setScale(Settings.SCALE + 0.011f);
            }

        }
        currentPlayerSize = ((Player) model).getPlayerSize();

    }

    private void groundedEffect() {
        if (!currentPlayerSize.equals(Player.PlayerSize.GROWN)) {
            return;
        }

        if (model.getState() == StateInterface.State.JUMP && effect.isComplete()) {
            canDrawGroundEffect = true;

            return;
        }

        if (model.isGrounded() && canDrawGroundEffect && effect.isComplete()) {
            effect.start();
            float heidhtHalf = (graphic.sprite.getHeight() / 2) * Settings.SCALE;
            effect.setPosition(model.getPosition().x, model.getPosition().y - heidhtHalf + 0.2f);
            canDrawGroundEffect = false;
        }

        if (!effect.isComplete()) {
            effect.draw(graphicResources.getSpriteBatch(), Gdx.graphics.getDeltaTime());
        }

    }

    private void initLight() {
        light = new PointLight(
                graphicResources.getRayHandler(), RAYS_PER, null, LIGHT_DISTANCE, 0f, 0f);
        light.attachToBody(model.getBody(), 0f, 0f);
        filterLight = new Filter();
        filterLight.categoryBits = (short) 1;
        filterLight.groupIndex = (short) 1;
        filterLight.maskBits = (short) 1;
        light.setContactFilter(filterLight);
        light.setColor(
                MathUtils.random(),
                MathUtils.random(),
                MathUtils.random(),
                1f);
        light.setActive(false);
    }

    private void loadParticle() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal(Assets.PATH + "particles/dust/dust.p"), asset.getAtlas());
    }

}
