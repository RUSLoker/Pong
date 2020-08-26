package com.rusloker.pong.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rusloker.pong.GameMode;
import com.rusloker.pong.GameRepository;
import com.rusloker.pong.Pair;

public class GameViewModel extends ViewModel {
    MutableLiveData<Boolean> pauseEvent = new MutableLiveData<>();
    MutableLiveData<Pair<Integer, Integer>> scoreChangedEvent = new MutableLiveData<>();

    public GameViewModel() {
        GameRepository.getPauseToggleEvent().subscribe((data) -> pauseEvent.postValue(data));
        GameRepository.getScoreChangedEvent().subscribe(data -> scoreChangedEvent.postValue(data));
    }

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

    public void pause() {
        GameRepository.pauseGame();
    }

    public void resume() {
        GameRepository.resumeGame();
    }

    public LiveData<Boolean> getPauseGameEvent() {
        return pauseEvent;
    }

    public LiveData<Pair<Integer, Integer>> getScoreChangedEvent() {
        return scoreChangedEvent;
    }
}