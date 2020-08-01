package com.rusloker.pong.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayMap;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.rusloker.pong.R;
import com.rusloker.pong.databinding.FragmentMainMenuBinding;


public class MainMenuFragment extends Fragment {

    private FragmentMainMenuBinding binding;
    private ObservableArrayMap<Integer, Boolean> pointerActivenessStates;

    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_main_menu, container, false);
        pointerActivenessStates = new ObservableArrayMap<>();
        binding.setPointerActivenessStates(pointerActivenessStates);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.play.setOnTouchListener(this::touchMenuButton);
        binding.settings.setOnTouchListener(this::touchMenuButton);
        binding.exit.setOnTouchListener(this::touchMenuButton);

    }

    private boolean touchMenuButton(View v, MotionEvent event) {
        int action = event.getActionMasked();
        Log.i("TouchEventMainMenu", Integer.toString(action));
        setTextViewState(v, action);
        if(action != MotionEvent.ACTION_UP) {
            return true;
        }
        switch (v.getId()) {
            case R.id.play: {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_mainMenuFragment_to_gameModeMenuFragment);
                break;
            }
            case R.id.settings: {

                break;
            }

            case R.id.exit: {
                getActivity().finishAndRemoveTask();
            }

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