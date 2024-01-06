package ua.org.petroff.game.engine.entities.characters.enemies.skeleton;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum CreatureState implements State<Skeleton> {

    STAY() {

        @Override
        public void enter(Skeleton model) {
            model.stay();
        }

        @Override
        public void update(Skeleton model) {
            if (model.isShow()) {
                model.getStateMachine().changeState(MOVE);
            }
        }

        @Override
        public void exit(Skeleton model) {
        }

    },
    MOVE() {

        @Override
        public void enter(Skeleton model) {
        }

        @Override
        public void update(Skeleton model) {

            if (!model.isShow()) {
                model.getStateMachine().changeState(STAY);

                return;
            }

            if (model.canFire()) {
                model.getStateMachine().changeState(FIRE);

                return;
            }

            model.move();

        }

        @Override
        public void exit(Skeleton model) {
            model.stay();
        }

    }, 
    FIRE() {

        @Override
        public void enter(Skeleton model) {
            model.fire();
        }

        @Override
        public void update(Skeleton model) {

            if (!model.isShow()) {
                model.getStateMachine().changeState(STAY);
                return;
            }

            model.getStateMachine().changeState(MOVE);

        }

        @Override
        public void exit(Skeleton model) {
            model.stay();
        }

    }, 
    JUMP() {

        @Override
        public void enter(Skeleton model) {
        }

        @Override
        public void update(Skeleton model) {

        }

        @Override
        public void exit(Skeleton model) {
        }

    }, 
    DIED() {

        @Override
        public void enter(Skeleton model) {
        }

        @Override
        public void update(Skeleton model) {
        }

        @Override
        public void exit(Skeleton model) {
        }

    };

    @Override
    public boolean onMessage(Skeleton model, Telegram telegram) {
        // We don't use messaging in this example
        return false;
    }

}
