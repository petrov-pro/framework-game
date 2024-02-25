package ua.org.petroff.game.engine.interfaces;

public interface StateInterface {

    public enum State {
        //Actions
        MOVE(1), JUMP(2), DIED(3), FIRE(4), STAY(5), HIT(6), BLOCK(11),
        //Statuses
        GROUND(7), PLAYER_DEAD(8), PLAYER_STATUS(9), CREATURE_COLLISION(10);

        public final int telegramNumber;

        State(int telegramMsg) {
            this.telegramNumber = telegramMsg;
        }

        public static State getStateBy(int value) {
            for (final State state : values()) {
                if (state.telegramNumber == value) {
                    return state;
                }
            }
            throw new Error("Not find state");
        }
    };

    public StateInterface.State getState();

}
