package com.rusloker.pong;

import android.view.MotionEvent;
import android.view.View;

public class GameButtonsClickListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int motionAction = event.getActionMasked();
        Action action = null;
        Player player = null;
        Side side = null;
        switch (motionAction) {
            case MotionEvent.ACTION_DOWN:
                action = Action.Start;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                action = Action.Stop;
                break;
        }
        switch (v.getId()) {
            case R.id.firstPlayerLeft:
                player = Player.First;
                side = Side.Left;
                break;
            case R.id.firstPlayerRight:
                player = Player.First;
                side = Side.Right;
                break;
            case R.id.secondPlayerLeft:
                player = Player.Second;
                side = Side.Left;
                break;
            case R.id.secondPlayerRight:
                player = Player.Second;
                side = Side.Right;
                break;
        }

        assert player != null;
        assert action != null;
        InputController.performPlayerMoveEvent(new Trio<>(player, side, action));

        //TODO: delete following block
        /*if (player == Player.First && side == Side.Left) {
            InputController.performPlayerMoveEvent(new Trio<>(Player.Second, Side.Right, action));
        } else if (player == Player.First && side == Side.Right) {
            InputController.performPlayerMoveEvent(new Trio<>(Player.Second, Side.Left, action));
        }*/

        v.performClick();
        return false;
    }
}
