package com.rusloker.pong.ai;

import com.rusloker.pong.Action;
import com.rusloker.pong.GameRepository;
import com.rusloker.pong.InputController;
import com.rusloker.pong.Player;
import com.rusloker.pong.Side;
import com.rusloker.pong.Trio;
import com.rusloker.pong.engine.Ball;
import com.rusloker.pong.engine.GameEntity;
import com.rusloker.pong.engine.Plank;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

public class PongBot {
    private Ball ball;
    private Plank plank;
    private Timer timer;

    public PongBot(){
        timer = new Timer("botTimer");
    }

    private void check() {
        updateEntities();
        if (ball == null || plank == null) {
            return;
        }
        if (ball.getPosition().x > plank.getPosition().x + plank.getWidth() / 2 - plank.getWidth() / 9) {
            InputController.performPlayerMoveEvent(new Trio<>(Player.Second, Side.Right, Action.Stop));
            InputController.performPlayerMoveEvent(new Trio<>(Player.Second, Side.Left, Action.Start));
        } else if (ball.getPosition().x < plank.getPosition().x - plank.getWidth() / 2 + plank.getWidth() / 9) {
            InputController.performPlayerMoveEvent(new Trio<>(Player.Second, Side.Left, Action.Stop));
            InputController.performPlayerMoveEvent(new Trio<>(Player.Second, Side.Right, Action.Start));
        } else if (ball.getPosition().x > plank.getPosition().x
                && InputController.isPlayerMotionEventActive(Player.Second, Side.Right)
                || ball.getPosition().x < plank.getPosition().x
                && InputController.isPlayerMotionEventActive(Player.Second, Side.Left)
        ){
            InputController.performPlayerMoveEvent(new Trio<>(Player.Second, Side.Left, Action.Stop));
            InputController.performPlayerMoveEvent(new Trio<>(Player.Second, Side.Right, Action.Stop));
        }
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

    public void stop(){
        timer.cancel();
        timer.purge();
    }

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                PongBot.this.check();
            }
        }, 10, 10);
    }
}
