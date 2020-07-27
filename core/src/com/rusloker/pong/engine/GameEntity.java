package com.rusloker.pong.engine;

public abstract class GameEntity {
    private Vector2D position;
    private static Integer previousId = 0;
    private final int id;

    GameEntity() {
        position = Vector2D.zero;
        synchronized (previousId) {
            previousId++;
            id = previousId;
        }
    }

    public void setPosition(float x, float y) {
        position = new Vector2D(x, y);
    }

    public void setPosition(Vector2D p) {
        position = p;
    }

    public Vector2D getPosition() {
        return position;
    }

    public abstract float getWidth();

    public abstract float getHeight();

    @Override
    public int hashCode() {
        return id;
    }
}
