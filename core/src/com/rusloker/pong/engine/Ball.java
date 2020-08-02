package com.rusloker.pong.engine;

public class Ball extends GameEntity {
    private Vector2D speed;
    public static final float RADIUS = 0.25f;

    public Ball() {
        super();
        speed = Vector2D.zero;
    }

    @Override
    public float getWidth() {
        return RADIUS * 2;
    }

    @Override
    public float getHeight() {
        return RADIUS * 2;
    }

    public void setSpeed(Vector2D speed) {
        this.speed = speed;
    }

    public void setSpeed(float x, float y) {
        this.speed = new Vector2D(x, y);
    }

    public Vector2D getSpeed() {
        return speed;
    }
}
