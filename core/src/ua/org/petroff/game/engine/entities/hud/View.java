package ua.org.petroff.game.engine.entities.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ua.org.petroff.game.engine.Settings;
import ua.org.petroff.game.engine.interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.interfaces.ViewInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.weapons.WeaponInterface;

public class View implements ViewInterface, GraphicQueueMemberInterface, QueueDrawInterface {

    private Stage stage;
    private ExtendViewport viewport;
    private Table table;
    private Label lifeLabel;
    private Label deadLabel;
    private Label ammoLabel;

    private final Assets asset;
    private final GraphicResources graphicResources;
    private final HUD model;
    private Animation healthAnimation;
    private Map<WeaponInterface.Type, Image> weaponImages = new HashMap<>();
    private ArrayList<Image> slots = new ArrayList<>();
    private Image shieldImage;
    private Image ammoImage;

    public View(Assets asset, GraphicResources graphicResources, HUD model) {
        this.asset = asset;
        this.model = model;
        this.graphicResources = graphicResources;
        init();
    }

    @Override
    public void prepareDraw(Map<Integer, QueueDrawInterface> queueDraw) {
        ((QueueDraw) queueDraw).putSafe(QueueDraw.Z_INDEX_END, this);
    }

    @Override
    public void draw() {
        stage.getViewport().apply();
        stage.act();
        stage.draw();
        lifeLabel.setText(model.currentLife.toString());
        if (model.currentLife <= 0) {
            deadLabel.setVisible(true);
        }
    }

    public void drawSlots(ArrayList<WeaponInterface.Type> weaponsPlayer, WeaponInterface.Type weaponCurrent) {
        for (int i = 0; i < weaponsPlayer.size(); i++) {
            Image weaponImage = weaponImages.get(weaponsPlayer.get(i));
            slots.get(i).setDrawable(
                    weaponImage.getDrawable()
            );
            slots.get(i).setScale(1f);
            if (weaponsPlayer.get(i) == weaponCurrent) {
                slots.get(i).setScale(1.3f);
            }
        }

    }

    public void drawAmmo(Integer ammo) {
        if (ammo > 0) {
            ammoLabel.setText(ammo.toString());
            ammoLabel.setVisible(true);
            ammoImage.setVisible(true);
            return;
        }

        ammoLabel.setVisible(false);
        ammoImage.setVisible(false);
    }

    public void showShield(boolean show) {
        shieldImage.setVisible(show);
    }

    private void loadAnimation() {
        TextureAtlas atlas = asset.getAtlas();
        TextureAtlas.AtlasRegion playerTextureHealth = atlas.findRegion("health_hud");
        TextureRegion[] playerRegionsHealth = new TextureRegion[7];

        for (int i = 0; i < 7; i++) {
            playerRegionsHealth[i] = new TextureRegion(playerTextureHealth, 50 * i, 0, 50, 63);
        }
        healthAnimation = new Animation(0.1f, (Object[]) playerRegionsHealth);
    }

    private void loadWeapon() {
        TextureAtlas atlas = asset.getAtlas();
        for (WeaponInterface.Type weapon : WeaponInterface.Type.values()) {
            TextureAtlas.AtlasRegion weaponRegion = atlas.findRegion(weapon.toString());
            TextureRegion weaponTexture = new TextureRegion(weaponRegion, 0, 0, 19, 49);
            Image weaponImage = new Image(weaponTexture);
            weaponImages.put(weapon, weaponImage);
        }
        TextureAtlas.AtlasRegion shieldRegion = atlas.findRegion("shield_hud");
        TextureRegion shieldTexture = new TextureRegion(shieldRegion, 0, 0, 50, 51);
        shieldImage = new Image(shieldTexture);
        shieldImage.setVisible(false);

        TextureAtlas.AtlasRegion ammoRegion = atlas.findRegion("quiver_hud");
        TextureRegion ammoTexture = new TextureRegion(ammoRegion, 0, 0, 16, 50);
        ammoImage = new Image(ammoTexture);
        ammoImage.setVisible(false);

        TextureAtlas.AtlasRegion emptyRegion = atlas.findRegion("empty");
        TextureRegion emptyTexture = new TextureRegion(emptyRegion, 0, 0, 19, 49);
        for (int i = 0; i < HUD.COUNTSLOT; i++) {
            slots.add(i, new Image(emptyTexture));
        }
    }

    private void init() {
        loadAnimation();
        loadWeapon();

        viewport = new ExtendViewport(Settings.APP_WIDTH, Settings.APP_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, graphicResources.getSpriteBatch()); // We must create order by creating a table in our stage

        Skin skin = new Skin(asset.loadSkin("uiskin.json"));

        table = new Table(skin);
        table.top(); // Will put it at the top of our stage
        table.setFillParent(true);

        HorizontalGroup lifeGroup = new HorizontalGroup();

        ImageAnimation image = new ImageAnimation();
        image.setAnimation(healthAnimation);
        lifeGroup.left()
                .addActor(image);

        lifeLabel = new Label(model.currentLife.toString(), skin);
        lifeLabel.setFontScale(2);
        lifeGroup.rowBottom()
                .space(5)
                .addActor(lifeLabel);

        table.add(lifeGroup)
                .left()
                .expandX().padTop(10);

        ammoLabel = new Label("", skin);
        ammoLabel.setFontScale(2);
        table.add(ammoImage).padTop(10);
        table.add(ammoLabel).expandX().padLeft(-100).padTop(10);

        table.add(shieldImage).expandX().padTop(10);

        for (Image slot : slots) {
            table.add(slot).expandX().padTop(10);
        }

        table.row().colspan(5); // THIS CREATES A NEW ROW

        deadLabel = new Label("YOU ARE DEAD", skin);
        deadLabel.setFontScale(3);
        deadLabel.setColor(Color.RED);
        deadLabel.setVisible(false);
        table.add(deadLabel).center().padTop(500);

        // add table to our stage
        stage.addActor(table);

        if (Settings.IS_DEBUG) {
            table.setDebug(true);
        }

        graphicResources.setViewportHud(viewport);
    }

}
