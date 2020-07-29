package com.rusloker.pong.viewmodels;

import androidx.lifecycle.ViewModel;

import com.rusloker.pong.GameMode;
import com.rusloker.pong.GameRepository;
import com.rusloker.pong.R;

public class GameModeMenuViewModel extends ViewModel {

    public void setGameMode(GameMode gameMode) {
        if (gameMode != null) {
            GameRepository.setGameMode(gameMode);
        }
    }

    public void menuButtonClicked(int id) {
        GameMode gameMode = null;
        switch (id) {
            case R.id.vs_computer:{
                gameMode = GameMode.VsComputer;
                break;
            }
            case R.id.vs_player: {
                gameMode = GameMode.VsPlayer;
                break;
            }
            case R.id.online: {
                gameMode = GameMode.Online;
                break;
            }
        }
        setGameMode(gameMode);
    }
}