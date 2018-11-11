package ua.org.petroff.game.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import java.util.HashMap;

public class Assets {

    private static final String PATH = "";
    private static final String IMAGE_PATH = "images/";
    private static final String MAP_PATH = "maps/";
    private static final String SKIN_PATH = "gdx-skins/default/skin/";

    private final AssetManager manager = new AssetManager();

    private final HashMap<String, AssetDescriptor> store = new HashMap();

    public void loadTexture(String alias, String path) {
        load(alias, Assets.PATH + Assets.IMAGE_PATH + path, Texture.class);
    }

    public FileHandle loadSkin(String path) {
        return Gdx.files.internal(Assets.PATH + Assets.SKIN_PATH + path);
    }

    public void loadMap(String alias, String path) {
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        load(alias, Assets.PATH + Assets.MAP_PATH + path, TiledMap.class);
    }

    private void load(String alias, String path, Class entityDescriptor) {
        AssetDescriptor assetDescriptor = new AssetDescriptor(path, entityDescriptor);
        manager.load(assetDescriptor);
        store.put(alias, assetDescriptor);
    }

    public <T> T get(String alias) {
        if (!manager.update()) {
            return null;
        }
        AssetDescriptor assetDescriptor = store.get(alias);
        return (T) assetDescriptor.type.cast(manager.get(assetDescriptor));
    }

    public boolean isUploaded() {
        return manager.update();
    }

    public AssetManager getManager() {
        return manager;
    }

}
