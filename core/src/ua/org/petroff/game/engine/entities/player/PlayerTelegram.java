package ua.org.petroff.game.engine.entities.player;

public class PlayerTelegram {

    private final Integer currentLive;

    public PlayerTelegram(Integer currentLive) {
        this.currentLive = currentLive;
    }

    public Integer getCurrentLive() {
        return currentLive;
    }
}
