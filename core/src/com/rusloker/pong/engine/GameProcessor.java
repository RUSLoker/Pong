package com.rusloker.pong.engine;

import com.rusloker.pong.GameRepository;
import com.rusloker.pong.InputController;
import com.rusloker.pong.Player;
import com.rusloker.pong.Side;

import org.omg.CORBA.MARSHAL;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public final class GameProcessor {
    private final Ball ball;
    private final Plank firstPlank;
    private final Plank secondPlank;
    private int firstScore, secondScore;
    private final Random random;
    private static volatile GameProcessor instance;
    private boolean playing;
    private Timer timer;
    private final List<GameEntity> entities;
    private static final float PLANK_MOVING_SPEED = 8f;
    private static final float BALL_START_SPEED = 9f;
    private static final float ANGLE_RANDOM_CONSTANT = (float) Math.PI / 35f;
    private static final float MIN_ANGLE = (float) Math.PI / 12 ;
    private Player turn;
    private static final float CALCULATION_DELAY = 0.002f;
    private float currentPlankSpeed = PLANK_MOVING_SPEED;

    private GameProcessor() {
        ball = new Ball();
        firstPlank = new Plank();
        secondPlank = new Plank();
        random = new Random();
        playing = false;
        timer = new Timer("gameTimer");
        entities = new ArrayList<>(3);
        if (random.nextFloat() <= 0.5) {
            turn = Player.First;
        } else {
            turn = Player.Second;
        }
    }

    private static GameProcessor getInstance() {
        if (instance == null) {
            synchronized (GameProcessor.class) {
                GameProcessor curInstance = instance;
                if (curInstance == null) {
                    instance = new GameProcessor();
                }
            }
        }
        return instance;
    }

    public static void createGame() {
        final GameProcessor instance = getInstance();
        instance.firstScore = 0;
        instance.secondScore = 0;
        GameRepository.setScore(instance.firstScore, instance.secondScore);
        instance.setBallStartPosition();
        instance.firstPlank.setPosition(4.5f, Plank.HEIGHT + 0.2f);
        instance.secondPlank.setPosition(4.5f, 15.80f - Plank.HEIGHT);
        instance.entities.add(instance.ball);
        instance.entities.add(instance.firstPlank);
        instance.entities.add(instance.secondPlank);
        GameRepository.putEntities(instance.entities);
        if (instance.timer == null) {
            instance.timer = new Timer("gameTimer");
        }
        instance.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                instance.update();
            }
        }, 1, (long) (CALCULATION_DELAY * 1000));
    }

    public static void startGame() {
        GameProcessor instance = getInstance();
        if (!instance.playing) {
            float quarterPi = (float) Math.PI / 4;
            Vector2D speed = new Vector2D(0, BALL_START_SPEED)
                    .rotate(-quarterPi + instance.random.nextFloat() * 2 * quarterPi);
            if (instance.turn == Player.First) {
                speed = speed.reverseY();
            }
            instance.ball.setSpeed(speed);
            instance.playing = true;
        }
    }

    public static void restartGame() {
        GameProcessor instance = getInstance();
        instance.setBallStartPosition();
        instance.firstPlank.setPosition(4.5f, Plank.HEIGHT + 0.2f);
        instance.secondPlank.setPosition(4.5f, 15.80f - Plank.HEIGHT);
        float quarterPi = (float) Math.PI / 4;
        Vector2D speed = new Vector2D(0, BALL_START_SPEED)
                .rotate(-quarterPi + instance.random.nextFloat() * 2 * quarterPi);
        if (instance.turn == Player.First) {
            speed = speed.reverseY();
        }
        instance.ball.setSpeed(speed);
        instance.currentPlankSpeed = PLANK_MOVING_SPEED;
        instance.playing = true;
    }

    public static void stopGame() {
        GameProcessor instance = getInstance();
        instance.playing = false;
        if (instance.timer != null) {
            instance.timer.cancel();
            instance.timer.purge();
            instance.timer = null;
        }
    }

    public static void pauseGame() {
        GameProcessor instance = getInstance();
        instance.playing = false;
    }

    public static void resumeGame() {
        instance.playing = true;
    }

    private void update() {
        if (!playing) {
            GameRepository.putEntities(entities);
            return;
        }

        ball.setPosition(ball.getPosition().add(ball.getSpeed().scale(CALCULATION_DELAY)));

        //Planks controls get
        if (InputController.isPlayerMotionEventActive(Player.First, Side.Left))
            firstPlank.setSpeed(new Vector2D(-currentPlankSpeed, 0));
        else if (InputController.isPlayerMotionEventActive(Player.First, Side.Right))
            firstPlank.setSpeed(new Vector2D(currentPlankSpeed, 0));
        else
            firstPlank.setSpeed(Vector2D.zero);

        if (InputController.isPlayerMotionEventActive(Player.Second, Side.Left))
            secondPlank.setSpeed(new Vector2D(currentPlankSpeed, 0));
        else if (InputController.isPlayerMotionEventActive(Player.Second, Side.Right))
            secondPlank.setSpeed(new Vector2D(-currentPlankSpeed, 0));
        else
            secondPlank.setSpeed(Vector2D.zero);

        //Planks move
        firstPlank.setPosition(firstPlank.getPosition().add(firstPlank.getSpeed().scale(CALCULATION_DELAY)));
        secondPlank.setPosition(secondPlank.getPosition().add(secondPlank.getSpeed().scale(CALCULATION_DELAY)));

        //Checking ball-wall collision
        if (ball.getPosition().x + Ball.RADIUS >= 9 && ball.getSpeed().x > 0
                || ball.getPosition().x - Ball.RADIUS <= 0 && ball.getSpeed().x < 0) {
            ball.setSpeed(ball.getSpeed().reverseX());
        }

        //Checking plank-wall collision
        checkPlankPosition(firstPlank);
        checkPlankPosition(secondPlank);

        //Plank-ball check
        boolean firstPlankCollision = false;
        if (ball.getPosition().y <= 0) {
            turn = Player.First;
            secondScore++;
            GameRepository.setScore(instance.firstScore, instance.secondScore);
            restartGame();
        } else if (ball.getPosition().y >= 16) {
            turn = Player.Second;
            firstScore++;
            GameRepository.setScore(instance.firstScore, instance.secondScore);
            restartGame();
        } else if ((firstPlankCollision = isBallCollidesPlank(ball, firstPlank))
                    && ball.getSpeed().y < 0
                    && ball.getPosition().y >= firstPlank.getPosition().y
                    || isBallCollidesPlank(ball, secondPlank)
                    && ball.getSpeed().y > 0
                    && ball.getPosition().y <= secondPlank.getPosition().y) {
            Vector2D speed = ball.getSpeed();
            speed = speed.reverseY();
            if (firstPlankCollision) {
                speed = speed.rotate((float) Math.PI / 100  * -firstPlank.getSpeed().x);
                if (speed.y < 0)
                    speed = speed.reverseY();
            } else {
                speed = speed.rotate((float) Math.PI / 100  * -secondPlank.getSpeed().x);
                if (speed.y > 0)
                    speed = speed.reverseY();
            }
            speed = speed
                    .rotate(-ANGLE_RANDOM_CONSTANT + random.nextFloat() * 2 * ANGLE_RANDOM_CONSTANT);
            speed = speed.scale(1.07f);
            float angle;
            if ((angle = Vector2D.angleBetween(new Vector2D(1, 0), speed)) < MIN_ANGLE) {
                speed = speed.rotate(MIN_ANGLE - angle);
            } else if ((angle = Vector2D.angleBetween(new Vector2D(-1, 0), speed)) < MIN_ANGLE) {
                speed = speed.rotate(angle - MIN_ANGLE);
            }
            ball.setSpeed(speed);
            currentPlankSpeed *= 1.005f;
        }
        GameRepository.putEntities(entities);
    }

    private void checkPlankPosition(Plank plank) {
        if (plank.getPosition().x + plank.getWidth() / 2 > 9) {
            plank.setPosition(new Vector2D(9 - plank.getWidth() / 2, plank.getPosition().y));
            plank.setSpeed(Vector2D.zero);
        } else if (plank.getPosition().x - plank.getWidth() / 2 < 0) {
            plank.setPosition(new Vector2D(0 + plank.getWidth() / 2, plank.getPosition().y));
            plank.setSpeed(Vector2D.zero);
        }
    }

    private void setBallStartPosition() {
        ball.setPosition(ball.getWidth() * 2 + random.nextFloat()*(9 - ball.getWidth() * 2), 8);
    }

    public static boolean isBallCollidesPlank(Ball ball, Plank plank) {
        if (ball.getPosition().y - Ball.RADIUS >= plank.getPosition().y - plank.getHeight() / 2
                && ball.getPosition().y + Ball.RADIUS <= plank.getPosition().y + plank.getHeight() / 2) {

            boolean firstCondition = ball.getPosition().x + Ball.RADIUS >= plank.getPosition().x - plank.getWidth() / 2
                    && ball.getPosition().x < plank.getPosition().x + plank.getWidth() / 2;
            boolean secondCondition = ball.getPosition().x - Ball.RADIUS <= plank.getPosition().x + plank.getWidth() / 2
                    && ball.getPosition().x > plank.getPosition().x - plank.getWidth() / 2;
            return firstCondition || secondCondition;

        } else if (ball.getPosition().x + Ball.RADIUS <= plank.getPosition().x + plank.getWidth() / 2
                && ball.getPosition().x - Ball.RADIUS >= plank.getPosition().x - plank.getWidth() / 2) {

            boolean firstCondition = ball.getPosition().y + Ball.RADIUS >= plank.getPosition().y - plank.getHeight() / 2
                    && ball.getPosition().y < plank.getPosition().y + plank.getHeight() / 2;
            boolean secondCondition = ball.getPosition().y - Ball.RADIUS <= plank.getPosition().y + plank.getHeight() / 2
                    && ball.getPosition().y > plank.getPosition().y - plank.getHeight() / 2;
            return firstCondition || secondCondition;
        } else {
            Vector2D leftTopAngle = new Vector2D(
                    plank.getPosition().x - plank.getWidth() / 2,
                    plank.getPosition().y + plank.getHeight() / 2).sub(ball.getPosition());
            Vector2D leftBottomAngle = new Vector2D(
                    plank.getPosition().x - plank.getWidth() / 2,
                    plank.getPosition().y - plank.getHeight() / 2).sub(ball.getPosition());
            Vector2D rightTopAngle = new Vector2D(
                    plank.getPosition().x + plank.getWidth() / 2,
                    plank.getPosition().y + plank.getHeight() / 2).sub(ball.getPosition());
            Vector2D rightBottomAngle = new Vector2D(
                    plank.getPosition().x + plank.getWidth() / 2,
                    plank.getPosition().y - plank.getHeight() / 2).sub(ball.getPosition());
            return leftTopAngle.length <= Ball.RADIUS
                    || leftBottomAngle.length <= Ball.RADIUS
                    || rightTopAngle.length <= Ball.RADIUS
                    || rightBottomAngle.length <= Ball.RADIUS;
        }
    }

}
