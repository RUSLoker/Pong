package com.rusloker.pong.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayMap;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.rusloker.pong.R;
import com.rusloker.pong.databinding.FragmentGameModeMenuBinding;
import com.rusloker.pong.viewmodels.GameModeMenuViewModel;

public class GameModeMenuFragment extends Fragment {

    private GameModeMenuViewModel mViewModel;
    private FragmentGameModeMenuBinding binding;
    private ObservableArrayMap<Integer, Boolean> pointerActivenessStates;

    public GameModeMenuFragment() {}

    public static GameModeMenuFragment newInstance() {
        return new GameModeMenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_mode_menu, container, false);
        mViewModel = new ViewModelProvider(this).get(GameModeMenuViewModel.class);
        pointerActivenessStates = new ObservableArrayMap<>();
        binding.setPointerActivenessStates(pointerActivenessStates);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GameModeMenuViewModel.class);
        binding.online.setOnTouchListener(this::touchMenuButton);
        binding.vsComputer.setOnTouchListener(this::touchMenuButton);
        binding.vsPlayer.setOnTouchListener(this::touchMenuButton);
    }

    private boolean touchMenuButton(View v, MotionEvent event) {
        int action = event.getActionMasked();
        setTextViewState(v, action);
        if (action == MotionEvent.ACTION_UP) {
            mViewModel.menuButtonClicked(v.getId());
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_gameModeMenuFragment_to_gameFragment);
        }
        return true;
    }

    private void setTextViewState(View v, int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pointerActivenessStates.put(v.getId(), true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                pointerActivenessStates.put(v.getId(), false);
                break;
        }
    }
}