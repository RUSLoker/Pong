package com.rusloker.pong.ai;

import com.rusloker.pong.Action;
import com.rusloker.pong.InputController;
import com.rusloker.pong.Player;
import com.rusloker.pong.Side;
import com.rusloker.pong.Trio;
import com.rusloker.pong.engine.Ball;
import com.rusloker.pong.engine.Vector2D;

public class CleverPongBot extends PongBot {
    private final Ball point = new Ball();

    public CleverPongBot() {
        super(250);
    }

    @Override
    void check() {
        final float fieldWidth = 9;
        final float fieldHeight = 15.5f;
        Vector2D ballSpeed = ball.getSpeed();
        if(ballSpeed.y < 0) {
            ballSpeed = ballSpeed.reverseY();
        }
        float yPos = ball.getPosition().y;
        float xPos = ball.getPosition().x;
        float sideDist = Math.signum(ballSpeed.x) > 0 ? fieldWidth - xPos : xPos;
        float yIntersection = yPos + sideDist * ballSpeed.y / Math.abs(ballSpeed.x);
        if (yIntersection < 15.5f) {
            yPos = yIntersection;
            float bounceLen = (fieldWidth - Ball.RADIUS * 2) * ballSpeed.y / Math.abs(ballSpeed.x);
            float bounceCount = (float) Math.floor((fieldHeight - yPos) / bounceLen);
            yPos += bounceLen * bounceCount;
            if (bounceCount % 2 == 0) {
                ballSpeed = ballSpeed.reverseX();
            }
            xPos = Math.signum(ballSpeed.x) < 0 ? fieldWidth - Ball.RADIUS : 0 + Ball.RADIUS;
        }
        float xIntersection = xPos + (fieldHeight - yPos) * ballSpeed.x / ballSpeed.y;

        //debug point drawing
//        point.setPosition(xIntersection, 15.5f);
//        ArrayList<GameEntity> list = new ArrayList<>();
//        list.add(point);
//        GameRepository.putEntities(list);

        if (plank.getPosition().x - plank.getWidth()/4 > xIntersection) {
            InputController.performPlayerMoveEvent(
                    new Trio<>(Player.Second, Side.Left, Action.Stop));
            InputController.performPlayerMoveEvent(
                    new Trio<>(Player.Second, Side.Right, Action.Start));
        } else if (plank.getPosition().x + plank.getWidth()/4 < xIntersection) {
            InputController.performPlayerMoveEvent(
                    new Trio<>(Player.Second, Side.Right, Action.Stop));
            InputController.performPlayerMoveEvent(
                    new Trio<>(Player.Second, Side.Left, Action.Start));
        } else {
            InputController.performPlayerMoveEvent(
                    new Trio<>(Player.Second, Side.Right, Action.Stop));
            InputController.performPlayerMoveEvent(
                    new Trio<>(Player.Second, Side.Left, Action.Stop));
        }
    }
}
