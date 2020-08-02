package com.rusloker.pong.ai;

import com.rusloker.pong.GameRepository;
import com.rusloker.pong.engine.Ball;
import com.rusloker.pong.engine.GameEntity;
import com.rusloker.pong.engine.Plank;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

public abstract class PongBot {

    Ball ball;
    Plank plank;
    Timer timer;
    long calculationDelayMillis;

    PongBot(long calculationDelayMillis) {
        this.calculationDelayMillis = calculationDelayMillis;
        timer = new Timer("botTimer");
    }

    PongBot(){
        this(10);
    }

    public void stop(){
        timer.cancel();
        timer.purge();
    }

    public void start() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                PongBot.this.updateEntities();
                PongBot.this.check();
            }
        }, 10, calculationDelayMillis);
    }

    private void updateEntities() {
        Collection<GameEntity> entities = GameRepository.getEntities();
        Plank plank1 = null, plank2 = null;
        for (GameEntity i : entities) {
            if (i instanceof Ball){
                ball = (Ball) i;
            } else if (plank1 == null && i instanceof Plank) {
                plank1 = (Plank) i;
            } else if (i instanceof Plank) {
                plank2 = (Plank) i;
                break;
            }
        }
        if (plank1 != null && plank2 != null)
            plank = plank1.getPosition().y > plank2.getPosition().y ? plank1 : plank2;
    }

    abstract void check();
}
