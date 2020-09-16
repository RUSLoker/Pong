package com.rusloker.pong.viewmodels;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.fragment.NavHostFragment;

import com.rusloker.pong.GameMode;
import com.rusloker.pong.GameRepository;
import com.rusloker.pong.R;

public class GameModeMenuViewModel extends ViewModel {

    public void setGameMode(GameMode gameMode) {
        if (gameMode != null) {
            GameRepository.setGameMode(gameMode);
        }
    }

    public void menuButtonClicked(int id, Fragment fragment) {
        switch (id) {
            case R.id.vs_computer:{
                setGameMode(GameMode.VsComputer);
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_gameModeMenuFragment_to_gameFragment);
                break;
            }
            case R.id.vs_player: {
                setGameMode(GameMode.VsPlayer);
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_gameModeMenuFragment_to_gameFragment);
                break;
            }
            case R.id.online: {
                setGameMode(GameMode.Online);
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_gameModeMenuFragment_to_gameFragment);
                break;
            }
            case R.id.lan: {
                setGameMode(GameMode.Lan);
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_gameModeMenuFragment_to_lanMenuFragment);
                break;
            }
        }
    }
}