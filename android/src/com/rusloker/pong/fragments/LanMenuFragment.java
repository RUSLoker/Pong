package com.rusloker.pong.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayMap;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.rusloker.pong.R;
import com.rusloker.pong.databinding.FragmentGameModeMenuBinding;
import com.rusloker.pong.databinding.FragmentLanMenuBinding;
import com.rusloker.pong.viewmodels.GameModeMenuViewModel;

public class LanMenuFragment extends Fragment {

    private FragmentLanMenuBinding binding;
    private ObservableArrayMap<Integer, Boolean> pointerActivenessStates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_lan_menu, container, false);
        pointerActivenessStates = new ObservableArrayMap<>();
        binding.setPointerActivenessStates(pointerActivenessStates);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.createGame.setOnTouchListener(this::touchMenuButton);
        binding.findGame.setOnTouchListener(this::touchMenuButton);
    }

    private boolean touchMenuButton(View v, MotionEvent event) {
        int action = event.getActionMasked();
        setTextViewState(v, action);
        if (action == MotionEvent.ACTION_UP) {

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