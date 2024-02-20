package ua.org.petroff.game.engine.entities.characters.enemies;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import ua.org.petroff.game.engine.entities.Interfaces.StateInterface;
import ua.org.petroff.game.engine.entities.characters.base.creature.StateTelegramInterface;
import ua.org.petroff.game.engine.util.Timer;
import ua.org.petroff.game.engine.entities.weapons.WeaponInterface;

public enum EnemyState implements State<Enemy>, StateTelegramInterface {

    STAY() {

        @Override
        public void enter(Enemy model) {
            model.stay();
        }

        @Override
        public void update(Enemy model) {
            if (model.isShow()) {
                model.changeState(MOVE);
            }
        }

        @Override
        public void exit(Enemy model) {
        }

    },
    MOVE() {

        @Override
        public void enter(Enemy model) {
        }

        @Override
        public void update(Enemy model) {

            if (!model.isShow()) {
                model.changeState(STAY);

                return;
            }

            if (model.isStuck()) {
                model.changeState(JUMP);

                return;
            }

            if (model.withinReachFire()) {
                model.changeState(FIRE);

                return;
            }
            model.move();

        }

        @Override
        public void exit(Enemy model) {
            model.stay();
        }

    },
    FIRE() {

        @Override
        public void enter(Enemy model) {

        }

        @Override
        public void update(Enemy model) {

            if (!model.isShow()) {
                model.changeState(STAY);
                return;
            }

            if (model.fire()) {
                model.changeState(MOVE);
            }
        }

        @Override
        public void exit(Enemy model) {
            model.stay();
        }

    },
    JUMP() {

        @Override
        public void enter(Enemy model) {
            if (!model.isShow()) {
                model.changeState(STAY);

                return;
            }

            model.jump();
        }

        @Override
        public void update(Enemy model) {

            if (model.isGrounded()) {
                model.changeState(MOVE);
            }

        }

        @Override
        public void exit(Enemy model) {
        }

    },
    HIT() {

        @Override
        public void enter(Enemy model) {
            if (model.getCurrentLive() > 0) {
                model.decreaseLive(((WeaponInterface) telegram.extraInfo).getDamage(),
                        ((WeaponInterface) telegram.extraInfo).getPlaceHit(),
                        ((WeaponInterface) telegram.extraInfo).getDirectionHit()
                );
            } else {
                model.changeState(DIED);
            }
        }

        @Override
        public void update(Enemy model) {
            if (model.getCurrentLive() <= 0 && Timer.runReset(model.toString() + "dead", 0.5f)) {
                model.changeState(DIED);

                return;
            }

            if (Timer.runReset(model.toString() + "after_hit", 0.5f)) {
                model.changeState(MOVE);
            }

        }

        @Override
        public void exit(Enemy model) {
        }

    },
    DIED() {

        @Override
        public void enter(Enemy model) {
            model.getStateMachine().setGlobalState(DIED);
        }

        @Override
        public void update(Enemy model) {
            model.died();
        }

        @Override
        public void exit(Enemy model) {
        }

    };

    public Telegram telegram;

    @Override
    public void setTelegram(Telegram telegram) {
        this.telegram = telegram;
    }

    @Override
    public boolean onMessage(Enemy model, Telegram telegram) {

        switch (StateInterface.State.getStateBy(telegram.message)) {

            case HIT:
                HIT.setTelegram(telegram);
                model.changeState(HIT);
                break;

            case DIED:
                model.changeState(DIED);
                break;

            case GROUND:
                model.ground((boolean) telegram.extraInfo);
                break;

            case CREATURE_COLLISION:
                model.ground(true);
                model.changeState(JUMP);
                break;
        }

        return true;
    }

}
