package ua.org.petroff.game.engine.scenes.level1;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import java.util.Map;
import ua.org.petroff.game.engine.interfaces.EntityInterface;
import ua.org.petroff.game.engine.interfaces.GraphicQueueMemberInterface;
import ua.org.petroff.game.engine.interfaces.QueueDrawInterface;
import ua.org.petroff.game.engine.interfaces.SupplierViewInterface;
import ua.org.petroff.game.engine.entities.QueueDraw;
import ua.org.petroff.game.engine.characters.enemies.EnemyFactory;
import ua.org.petroff.game.engine.entities.cloud.CloudFactory;
import ua.org.petroff.game.engine.entities.equipments.EquipmentFactory;
import ua.org.petroff.game.engine.entities.weapons.ranged.RangedWeaponFactory;
import ua.org.petroff.game.engine.entities.hud.HUD;
import ua.org.petroff.game.engine.entities.player.Player;
import ua.org.petroff.game.engine.scenes.Interface.ContainerInterface;
import ua.org.petroff.game.engine.scenes.core.GameResources;
import ua.org.petroff.game.engine.scenes.core.GraphicResources;
import ua.org.petroff.game.engine.scenes.core.ManagerScenes;
import ua.org.petroff.game.engine.entities.map.GameWorld;
import ua.org.petroff.game.engine.entities.weapons.melee.MeleeWeaponFactory;
import ua.org.petroff.game.engine.util.Assets;
import ua.org.petroff.game.engine.interfaces.ActionEntityInterface;

public class Level1Container implements ContainerInterface {
    
    public static final String DESCRIPTOR = "level1";
    
    private Level1Screen screen;
    private final ManagerScenes manageScene;
    
    private final Assets asset = new Assets();
    private final GraphicResources graphicResources = new GraphicResources();
    private final GameResources gameResources;
    
    private final Array<QueueDrawInterface> drawings = new Array();
    private final Array<EntityInterface> entities = new Array();
    
    public Level1Container(ManagerScenes manageScene) {
        this.gameResources = new GameResources();
        this.manageScene = manageScene;
    }
    
    @Override
    public Assets getAssets() {
        return this.asset;
    }
    
    @Override
    public String getSceneName() {
        return "UnderWorld";
    }
    
    @Override
    public Screen getView() {
        return (Screen) this.screen;
    }
    
    @Override
    public void start() {
        loadEntities();
        prepareDraw();
        screen = new Level1Screen(entities, drawings, graphicResources);
        (new Level1Controller(manageScene, screen, (ActionEntityInterface) gameResources.findModel(Player.class))).bindControl();
    }
    
    @Override
    public void loadShareResources() {
        asset.loadMap("map-level1", "level1/level1.tmx");
        asset.loadAtlas();
        asset.loadBackground(0, "level1/background/layer07_Sky.png");
        asset.loadBackground(1, "level1/background/layer04_Clouds.png");
        asset.loadBackground(2, "level1/background/layer06_Rocks.png");
        asset.loadBackground(3, "level1/background/layer05_Hills.png");
        
    }
    
    private void loadEntities() {
        //order important
        gameResources.setModels(entities);
        entities.add(new GameWorld(asset, gameResources, graphicResources));
        entities.add(new HUD(asset, gameResources, graphicResources));
        entities.add(new RangedWeaponFactory(asset, gameResources, graphicResources));
        entities.add(new MeleeWeaponFactory(asset, gameResources));
        entities.add(new CloudFactory(asset, gameResources, graphicResources));
        entities.add(Player.getInstance(asset, gameResources, graphicResources));
        EnemyFactory.addEnemy(entities, asset, gameResources, graphicResources);
        EquipmentFactory.addEquipment(entities, asset, gameResources, graphicResources);
    }
    
    public void prepareDraw() {
        Map<Integer, QueueDrawInterface> queueDraw = new QueueDraw<>();
        for (EntityInterface entity : entities) {
            if (entity instanceof SupplierViewInterface) {
                ((GraphicQueueMemberInterface) ((SupplierViewInterface) entity).getView()).prepareDraw(queueDraw);
            }
            
        }
        copy(queueDraw);
    }
    
    private void copy(Map<Integer, QueueDrawInterface> queueDraw) {
        for (QueueDrawInterface entity : queueDraw.values()) {
            drawings.add(entity);
        }
    }
    
}
