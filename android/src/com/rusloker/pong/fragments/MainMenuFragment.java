package com.rusloker.pong.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rusloker.pong.R;
import com.rusloker.pong.databinding.FragmentMainMenuBinding;


public class MainMenuFragment extends Fragment {

    FragmentMainMenuBinding binder;

    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(
                inflater, R.layout.fragment_main_menu, container, false);
        return binder.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDefault(binder.play);
        setDefault(binder.settings);
        setDefault(binder.exit);
        binder.play.setOnTouchListener(this::touchMenuButton);
        binder.settings.setOnTouchListener(this::touchMenuButton);
        binder.exit.setOnTouchListener(this::touchMenuButton);

    }

    private boolean touchMenuButton(View v, MotionEvent event) {
        int action = event.getActionMasked();
        Log.i("TouchEventMainMenu", Integer.toString(action));
        setTextViewState((TextView) v, action);
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

            }

        }
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