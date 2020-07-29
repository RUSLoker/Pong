package com.rusloker.pong.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rusloker.pong.R;
import com.rusloker.pong.databinding.FragmentGameModeMenuBinding;
import com.rusloker.pong.viewmodels.GameModeMenuViewModel;

public class GameModeMenuFragment extends Fragment {

    private GameModeMenuViewModel mViewModel;
    FragmentGameModeMenuBinding binder;

    public GameModeMenuFragment() {}

    public static GameModeMenuFragment newInstance() {
        return new GameModeMenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_mode_menu, container, false);
        mViewModel = new ViewModelProvider(this).get(GameModeMenuViewModel.class);
        return binder.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GameModeMenuViewModel.class);
        setDefault(binder.online);
        setDefault(binder.vsComputer);
        setDefault(binder.vsPlayer);
        binder.online.setOnTouchListener(this::touchMenuButton);
        binder.vsComputer.setOnTouchListener(this::touchMenuButton);
        binder.vsPlayer.setOnTouchListener(this::touchMenuButton);
    }

    private boolean touchMenuButton(View v, MotionEvent event) {
        int action = event.getActionMasked();
        Log.i("TouchEventMainMenu", Integer.toString(action));
        setTextViewState((TextView) v, action);
        if (action != MotionEvent.ACTION_UP) {
            return true;
        }
        mViewModel.menuButtonClicked(v.getId());
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_gameModeMenuFragment_to_gameFragment);
        return true;
    }

    private void setTextViewState(TextView v, int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setSelected(v);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setUnselected((TextView) v);
                break;
        }
    }

    private void setUnselected(TextView textView) {
        CharSequence text = textView.getText();
        textView.setText(String.format("%s %s", getResources().getString(R.string.selector), text.subSequence(2, text.length())));
    }

    private void setSelected(TextView textView) {
        CharSequence text = textView.getText();
        textView.setText(String.format(" %s%s", getResources().getString(R.string.selector), text.subSequence(2, text.length())));
    }

    private void setDefault(TextView textView) {
        CharSequence text = textView.getText();
        textView.setText(String.format("%s %s", getResources().getString(R.string.selector), text));
    }
}