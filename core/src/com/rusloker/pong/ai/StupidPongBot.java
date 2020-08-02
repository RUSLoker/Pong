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

public class StupidPongBot extends PongBot {


    void check() {
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
}
