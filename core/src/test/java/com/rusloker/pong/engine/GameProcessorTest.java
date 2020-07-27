package com.rusloker.pong.engine;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class GameProcessorTest {

    @Test
    public void testIsBallCollidesPlank() {
        Ball ball = new Ball();
        ball.setPosition(0, 0);
        Plank plank = new Plank();
        plank.setPosition(0, 0);
        assertTrue(GameProcessor.isBallCollidesPlank(ball, plank));
        ball.setPosition(0, plank.getHeight() / 2);
        plank.setPosition(0, 0);
        assertTrue(GameProcessor.isBallCollidesPlank(ball, plank));
        ball.setPosition(0, plank.getHeight() / 2 + Ball.RADIUS);
        plank.setPosition(0, 0);
        assertTrue(GameProcessor.isBallCollidesPlank(ball, plank));
        ball.setPosition(0, -plank.getHeight() / 2 - Ball.RADIUS);
        plank.setPosition(0, 0);
        assertTrue(GameProcessor.isBallCollidesPlank(ball, plank));
        ball.setPosition(0, -plank.getHeight() / 2 - Ball.RADIUS - 0.00001f);
        plank.setPosition(0, 0);
        assertFalse(GameProcessor.isBallCollidesPlank(ball, plank));
    }
}