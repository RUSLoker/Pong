package com.rusloker.pong.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rusloker.pong.R;
import com.rusloker.pong.viewmodels.MainMenuViewModel;

public class MainMenuFragment extends Fragment {

    private MainMenuViewModel mViewModel;

    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_menu_fragment, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainMenuViewModel.class);
        // TODO: Use the ViewModel
        getView().findViewById(R.id.vs_computer).setOnTouchListener((v, event) ->
                touchMenuButton(v, event, R.id.action_mainMenuFragment_to_gameFragment));
        getView().findViewById(R.id.vs_player).setOnTouchListener((v, event) ->
                touchMenuButton(v, event, R.id.action_mainMenuFragment_to_gameFragment));
        getView().findViewById(R.id.online).setOnTouchListener((v, event) ->
                touchMenuButton(v, event, R.id.action_mainMenuFragment_to_gameFragment));

    }

    private boolean touchMenuButton(View v, MotionEvent event, int actionId) {
        int action = event.getActionMasked();
        Log.i("TouchEventMainMenu", Integer.toString(action));
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                ((TextView)v.findViewById(R.id.prefix)).setGravity(Gravity.END);
                return true;
            case MotionEvent.ACTION_UP:
                NavHostFragment.findNavController(this)
                        .navigate(actionId);
                v.performClick();
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
                ((TextView)v.findViewById(R.id.prefix)).setGravity(Gravity.START);
                return true;
        }
        return false;
    }

}