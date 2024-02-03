package ua.org.petroff.game.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import java.util.HashMap;

public class Assets {

    public static final String PATH = "data/";
    public static final String IMAGE_PATH = "images/";
    public static final String IMAGE_ENTITIES_PATH = "images/entities/";
    public static final String MAP_PATH = "map/";
    public static final String SKIN_PATH = "gdx-skins-master/default/skin/";

    private final AssetManager manager = new AssetManager();

    private final HashMap<String, AssetDescriptor> store = new HashMap();

    private String aliasMap;
    private String atlasPath = "atlas/general.atlas";

    public void loadTexture(String alias, String path) {
        load(alias, Assets.PATH + path, Texture.class);
    }

    public Texture getTexture(String alias) {
        return (Texture) get(alias);
    }

    public FileHandle loadSkin(String path) {
        return Gdx.files.internal(Assets.SKIN_PATH + path);
    }

    public void loadAtlas(String path) {
        if (store.containsKey(path)) {
            return;
        }
        AssetDescriptor assetDescriptor = new AssetDescriptor(path, TextureAtlas.class);
        manager.load(assetDescriptor);
        store.put(path, assetDescriptor);
        atlasPath = path;
    }

    public void loadAtlas() {
        loadAtlas(atlasPath);
    }

    public TextureAtlas getAtlas() {
        return (TextureAtlas) get(atlasPath);
    }

    public Map getMap() {
        return (Map) get(aliasMap);
    }

    public void loadMap(String alias, String path) {
        aliasMap = alias;
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        load(alias, Assets.MAP_PATH + path, TiledMap.class);
    }

    private void load(String alias, String path, Class entityDescriptor) {
        AssetDescriptor assetDescriptor = new AssetDescriptor(path, entityDescriptor);
        manager.load(assetDescriptor);
        store.put(alias, assetDescriptor);
    }

    private <T> T get(String alias) {
        AssetDescriptor assetDescriptor = store.get(alias);
        return (T) manager.get(assetDescriptor);
    }

    public boolean isUploaded() {
        return manager.update();
    }

    public AssetManager getManager() {
        return manager;
    }

}
