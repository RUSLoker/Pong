package com.rusloker.pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rusloker.pong.ai.CleverPongBot;
import com.rusloker.pong.ai.PongBot;
import com.rusloker.pong.engine.Ball;
import com.rusloker.pong.engine.GameEntity;
import com.rusloker.pong.engine.GameProcessor;
import com.rusloker.pong.engine.Plank;
import com.rusloker.pong.engine.Vector2D;
import com.rusloker.pong.events.CallableDataEvent;
import com.rusloker.pong.events.DataEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class GameRepository {
    private Map<GameEntity, Sprite> drawables;
    private int SCREEN_HEIGHT;
    private int SCREEN_WIDTH;
    private Map<Class<? extends GameEntity>, TextureRegion> textures;
    private static volatile GameRepository instance;
    private Collection<GameEntity> entities;
    private GameMode gameMode;
    private PongBot PongBot;
    private CallableDataEvent<Boolean> pauseToggleEvent;
    private CallableDataEvent<Pair<Integer, Integer>> scoreChangedEvent;

    private GameRepository(){
        textures = new HashMap<>();
        drawables = new HashMap<>();
        entities = Collections.emptyList();
        gameMode = GameMode.VsComputer;
        PongBot = new CleverPongBot();
        pauseToggleEvent = new CallableDataEvent<>();
        scoreChangedEvent = new CallableDataEvent<>();
    }

    private static GameRepository getInstance() {
        if(instance == null) {
            synchronized (GameRepository.class) {
                GameRepository curInstance = instance;
                if (curInstance == null) {
                    instance = new GameRepository();
                }
            }
        }
        return instance;
    }

    public static void putEntities(Collection<GameEntity> entities) {
        GameRepository instance = getInstance();
        assert entities != null;
        instance.entities = entities;
        for (GameEntity i : entities) {
            Vector2D cords = transformCords(i.getPosition().x, i.getPosition().y);
            if (instance.drawables.get(i) != null) {
                instance.drawables.get(i).setOriginBasedPosition(cords.x, cords.y);
            } else if (instance.textures.get(i.getClass()) != null) {
                Sprite sprite = new Sprite(instance.textures.get(i.getClass()));
                Vector2D transSize = transformCords(i.getWidth(), i.getHeight());
                sprite.setSize(transSize.x, transSize.y);
                sprite.setOriginCenter();
                sprite.setOriginBasedPosition(cords.x, cords.y);
                instance.drawables.put(i, sprite);
            }
        }
    }

    public static Collection<GameEntity> getEntities() {
        GameRepository instance = getInstance();
        return instance.entities;
    }

    public static Collection<Sprite> getDrawables() {
        GameRepository instance = getInstance();
        return instance.drawables.values();
    }

    public static Vector2D transformCords(float x, float y) {
        GameRepository instance = getInstance();
        x = x / 9 * instance.SCREEN_WIDTH;
        y = y / 16 * instance.SCREEN_HEIGHT;
        return new Vector2D(x, y);
    }

    public static void initializeGraphics() {
        GameRepository instance = getInstance();
        Texture sprites = new Texture("sprites.png");
        TextureRegion plank = new TextureRegion(sprites, 10, 73, 333, 32);
        TextureRegion ball = new TextureRegion(sprites, 10, 4, 64, 64);
        instance.textures.put(Ball.class, ball);
        instance.textures.put(Plank.class, plank);
        instance.SCREEN_HEIGHT = Gdx.graphics.getHeight();
        instance.SCREEN_WIDTH = Gdx.graphics.getWidth();
    }

    public static GameMode getGameMode(){
        GameRepository instance = getInstance();
        return instance.gameMode;
    }

    public static void setGameMode(GameMode gameMode){
        GameRepository instance = getInstance();
        instance.gameMode = gameMode;
    }

    public static void startGame() {
        GameRepository instance = getInstance();
        switch (instance.gameMode) {
            case VsComputer: {
                instance.PongBot = new CleverPongBot();
                instance.PongBot.start();
                break;
            }
        }
        GameProcessor.startGame();
    }

    public static void stopGame() {
        GameRepository instance = getInstance();
        if(instance.PongBot != null) {
            instance.PongBot.stop();
            instance.PongBot = null;
        }
        GameProcessor.stopGame();
    }

    public static void createGame() {
        GameProcessor.createGame();
    }

    public static void pauseGame() {
        GameRepository instance = getInstance();
        instance.pauseToggleEvent.call(true);
        GameProcessor.pauseGame();
    }

    public static void resumeGame() {
        GameRepository instance = getInstance();
        instance.pauseToggleEvent.call(false);
        GameProcessor.resumeGame();
    }

    public static DataEvent<Boolean> getPauseToggleEvent() {
        return getInstance().pauseToggleEvent;
    }

    public static DataEvent<Pair<Integer, Integer>> getScoreChangedEvent() {
        return getInstance().scoreChangedEvent;
    }

    public static void setScore(int firstScore, int secondScore) {
        GameRepository instance = getInstance();
        instance.scoreChangedEvent.call(new Pair<>(firstScore, secondScore));
    }
}
