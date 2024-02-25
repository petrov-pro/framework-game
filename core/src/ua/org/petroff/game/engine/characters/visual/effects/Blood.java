package ua.org.petroff.game.engine.characters.visual.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;

public class Blood implements ViewHitInterface{

    private final Assets asset;
    private ParticleEffect effectBlood;
    private final GraphicResources graphicResources;

    public Blood(Assets asset, GraphicResources graphicResources) {
        this.asset = asset;
        this.graphicResources = graphicResources;
        loadParticle();
    }

    private void loadParticle() {
        effectBlood = new ParticleEffect();
        effectBlood.load(Gdx.files.internal(Assets.PATH + "particles/blood/blood.p"), asset.getAtlas());
        effectBlood.scaleEffect(Settings.SCALE);
    }

    public void drawHit(boolean start, Vector2 placeHit) {
        if (start && effectBlood.isComplete()) {
            effectBlood.setPosition(placeHit.x, placeHit.y);
            effectBlood.start();
        }

        if (!effectBlood.isComplete()) {
            effectBlood.draw(graphicResources.getSpriteBatch(), Gdx.graphics.getDeltaTime());
        }
    }

}
