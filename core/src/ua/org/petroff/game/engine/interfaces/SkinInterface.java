package ua.org.petroff.game.engine.interfaces;

public interface SkinInterface {

    public enum Type {
        DEFAULT, BOW, SWORD
    }

    public SkinInterface.Type getSkinType();

}
