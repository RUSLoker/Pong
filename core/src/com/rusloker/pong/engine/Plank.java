package com.rusloker.pong.engine;

public class Plank extends GameEntity {
    public static final float HEIGHT = 0.25f;
    public static final float WIDTH = 2;
    private Vector2D speed;

    public Plank() {
        super();
        speed = Vector2D.zero;
    }

    @Override
    public float getWidth() {
        return WIDTH;
    }

    @Override
    public float getHeight() {
        return HEIGHT;
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
