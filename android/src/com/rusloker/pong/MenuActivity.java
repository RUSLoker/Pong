package com.rusloker.pong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViewById(R.id.vs_computer).setOnClickListener(this);
        findViewById(R.id.vs_player).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, GameActivity.class);
        switch (v.getId()) {
            case R.id.vs_computer:
                bundle.putString(GameActivity.GAME_TYPE, GameType.VsComputer.toString());
                break;
            case R.id.vs_player:
                bundle.putString(GameActivity.GAME_TYPE, GameType.VsPlayer.toString());
                break;
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }
}