package com.rusloker.pong.viewmodels;

import androidx.lifecycle.ViewModel;

import com.rusloker.pong.GameMode;
import com.rusloker.pong.GameRepository;

public class GameViewModel extends ViewModel {
    public GameMode getGameMode() {
        return GameRepository.getGameMode();
    }

    public void viewCreation() {
        GameRepository.createGame();
    }

    public void countdownEnded() {
        GameRepository.startGame();
    }

    public void viewDetach() {
        GameRepository.stopGame();
    }
}