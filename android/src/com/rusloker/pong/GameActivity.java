package com.rusloker.pong;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.rusloker.pong.ai.PongBot;
import com.rusloker.pong.engine.GameProcessor;

public class GameActivity extends AppCompatActivity implements AndroidFragmentApplication.Callbacks {
    GdxVisualiserFragment gdxVisualiserFragment;
    public static final String GAME_TYPE = "gameType";
    PongBot pongBot = new PongBot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gdxVisualiserFragment = new GdxVisualiserFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.gameView, gdxVisualiserFragment).
                commit();
        hideSystemUI();
        GameProcessor.createGame();
        findViewById(R.id.startButton).setOnClickListener(v -> {
            GameProcessor.startGame();
            v.setVisibility(View.GONE);
        });
        View.OnTouchListener listener = new GameActivityButtonsClickListener();
        findViewById(R.id.firstPlayerLeft).setOnTouchListener(listener);
        findViewById(R.id.firstPlayerRight).setOnTouchListener(listener);
        findViewById(R.id.secondPlayerLeft).setOnTouchListener(listener);
        findViewById(R.id.secondPlayerRight).setOnTouchListener(listener);
        Bundle bundle = getIntent().getExtras();
        GameType gameType = GameType.VsComputer;
        if (bundle != null) {
            gameType = GameType.valueOf(bundle.getString(GAME_TYPE));
        }
        switch (gameType) {
            case VsPlayer:
                break;
            case VsComputer:
                pongBot.start();
                findViewById(R.id.secondControls).setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void exit() {

    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pongBot.stop();
        GameProcessor.stopGame();
    }
}