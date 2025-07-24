package ua.org.petroff.game.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shader {

    private ShaderProgram shader;
    private Assets asset;

    public Shader(Assets asset, String nameVert, String nameFrag) {
        this.asset = asset;
        shader = new ShaderProgram(asset.loadShader(nameVert), asset.loadShader(nameFrag));
        if (!shader.isCompiled()) {
            Gdx.app.error("Shader", "Shader compilation failed: " + shader.getLog());
        }
    }

    public ShaderProgram getShader() {
        return shader;
    }

}
