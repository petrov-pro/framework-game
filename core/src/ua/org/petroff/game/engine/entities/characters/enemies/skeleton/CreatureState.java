package ua.org.petroff.game.engine.entities.characters.enemies.skeleton;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.characters.base.creature.StateTelegramInterface;
import ua.org.petroff.game.engine.entities.guns.GunInterface;
import ua.org.petroff.game.engine.util.Timer;

public enum CreatureState implements State<Skeleton>, StateTelegramInterface {

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

        }

        @Override
        public void update(Skeleton model) {

            if (!model.isShow()) {
                model.getStateMachine().changeState(STAY);
                return;
            }

            if (model.fire()) {
                model.getStateMachine().changeState(MOVE);
            }
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
    HIT() {

        @Override
        public void enter(Skeleton model) {
            if (model.getCurrentLive() > 0) {
                model.decreaseLive(((GunInterface) telegram.extraInfo).getDamage(), ((GunInterface) telegram.extraInfo).getPlaceHit());
            } else {
                model.getStateMachine().changeState(DIED);
            }
        }

        @Override
        public void update(Skeleton model) {
            if (model.getCurrentLive() <= 0 && Timer.run(0.5f)) {
                model.getStateMachine().changeState(DIED);

                return;
            }

            if (Timer.run(0.5f)) {
                model.getStateMachine().changeState(MOVE);
            }

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
            model.died();
        }

        @Override
        public void exit(Skeleton model) {
        }

    };

    public Telegram telegram;

    @Override
    public void setTelegram(Telegram telegram) {
        this.telegram = telegram;
    }

    @Override
    public boolean onMessage(Skeleton model, Telegram telegram) {

        switch (StateInterface.State.getStateBy(telegram.message)) {

            case HIT:
                HIT.setTelegram(telegram);
                model.getStateMachine().changeState(HIT);
                break;

            case DIED:
                model.getStateMachine().changeState(DIED);
                break;

            case GROUND:
                model.grounded();
                break;
        }

        return true;
    }

}
