package com.rusloker.pong;



import com.badlogic.gdx.Gdx;
import com.rusloker.pong.events.CallableDataEvent;
import com.rusloker.pong.events.DataEvent;
import com.rusloker.pong.events.EventListener;

import java.util.HashMap;
import java.util.Map;

public final class InputController {
    private final CallableDataEvent<Trio<Player, Side, Action>> playerMoveEvent;
    private final Map<Pair<Player, Side>, Boolean> playerMotionEventsActivity;
    private static volatile InputController instance;

    private InputController() {
        this.playerMoveEvent = new CallableDataEvent<>();
        playerMotionEventsActivity = new HashMap<>(4);
        playerMoveEvent.subscribe(new EventListener<Trio<Player, Side, Action>>() {
            @Override
            public void call(Trio<Player, Side, Action> playerSideActionTrio) {
                InputController.this.setPlayerMotionEventsActivity(playerSideActionTrio);
            }
        });
    }

    private static InputController getInstance() {
        if(instance == null) {
            synchronized (InputController.class) {
                InputController curInstance = instance;
                if (curInstance == null) {
                    instance = new InputController();
                }
            }
        }
        return instance;
    }

    public static DataEvent<Trio<Player, Side, Action>> getPlayerMoveEvent() {
        return getInstance().playerMoveEvent;
    }

    public static void performPlayerMoveEvent(Trio<Player, Side, Action> trio){
        Gdx.app.log("playerMotionEvent", trio.first + " " + trio.second + " " + trio.third);
        getInstance().playerMoveEvent.call(trio);
    }

    public static boolean isPlayerMotionEventActive(Player player, Side side) {
        return getInstance().playerMotionEventsActivity.getOrDefault(new Pair<>(player, side), false);
    }

    private void setPlayerMotionEventsActivity(Trio<Player, Side, Action> t) {
        if(t.third == Action.Stop) {
            playerMotionEventsActivity.put(new Pair<>(t.first, t.second), false);
        } else if (t.third == Action.Start) {
            playerMotionEventsActivity.put(new Pair<>(t.first, t.second), true);
        }
    }
}
