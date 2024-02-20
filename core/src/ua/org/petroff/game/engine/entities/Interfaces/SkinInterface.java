package ua.org.petroff.game.engine.entities.Interfaces;

public interface SkinInterface {

    public enum Type {
        DEFAULT, BOW, SWORD
    }

    public SkinInterface.Type getSkinType();

}
