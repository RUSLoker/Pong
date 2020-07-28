package com.rusloker.pong.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.rusloker.pong.GameButtonsClickListener;
import com.rusloker.pong.GameType;
import com.rusloker.pong.GdxVisualiserFragment;
import com.rusloker.pong.R;
import com.rusloker.pong.ai.PongBot;
import com.rusloker.pong.engine.GameProcessor;
import com.rusloker.pong.viewmodels.GameViewModel;

public class GameFragment extends Fragment implements AndroidFragmentApplication.Callbacks {
    GdxVisualiserFragment gdxVisualiserFragment;
    public static final String GAME_TYPE = "gameType";
    PongBot pongBot = new PongBot();
    private GameViewModel mViewModel;

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        // TODO: Use the ViewModel
        gdxVisualiserFragment = new GdxVisualiserFragment();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.gameView, gdxVisualiserFragment).
                commit();
        GameProcessor.createGame();
        getView().findViewById(R.id.startButton).setOnClickListener(v -> {
            GameProcessor.startGame();
            v.setVisibility(View.GONE);
        });
        View.OnTouchListener listener = new GameButtonsClickListener();
        getView().findViewById(R.id.firstPlayerLeft).setOnTouchListener(listener);
        getView().findViewById(R.id.firstPlayerRight).setOnTouchListener(listener);
        getView().findViewById(R.id.secondPlayerLeft).setOnTouchListener(listener);
        getView().findViewById(R.id.secondPlayerRight).setOnTouchListener(listener);
        GameType gameType = GameType.VsComputer;
        switch (gameType) {
            case VsPlayer:
                break;
            case VsComputer:
                pongBot.start();
                getView().findViewById(R.id.secondControls).setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void exit() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        pongBot.stop();
        GameProcessor.stopGame();

    }
}