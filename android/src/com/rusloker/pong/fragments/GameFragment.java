package com.rusloker.pong.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.rusloker.pong.Action;
import com.rusloker.pong.GameMode;
import com.rusloker.pong.InputController;
import com.rusloker.pong.Player;
import com.rusloker.pong.R;
import com.rusloker.pong.Side;
import com.rusloker.pong.Trio;
import com.rusloker.pong.databinding.FragmentGameBinding;
import com.rusloker.pong.viewmodels.GameViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class GameFragment extends Fragment implements AndroidFragmentApplication.Callbacks {
    private GdxVisualiserFragment gdxVisualiserFragment;
    private GameViewModel mViewModel;
    private FragmentGameBinding binding;
    Timer countdown;

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mViewModel.viewCreation();
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game, container, false);
        gdxVisualiserFragment = new GdxVisualiserFragment();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.gameView, gdxVisualiserFragment).
                commit();
        View.OnTouchListener listener = new GameButtonsClickListener();
        binding.firstPlayerLeft.setOnTouchListener(listener);
        binding.firstPlayerRight.setOnTouchListener(listener);
        binding.secondPlayerLeft.setOnTouchListener(listener);
        binding.secondPlayerRight.setOnTouchListener(listener);
        mViewModel.getPauseGameEvent().observe(getViewLifecycleOwner(), (value) -> {
            if (value) {
                binding.pause.setText("|>");
            } else {
                binding.pause.setText("||");
            }
        });
        mViewModel.getScoreChangedEvent().observe(getViewLifecycleOwner(), value -> {
            SpannableString str = new SpannableString(String.valueOf(value.first));
            if (str.charAt(0) == '6' || str.charAt(0) == '9') {
                str = new SpannableString(str.toString() + ".");
                str.setSpan(
                        new RelativeSizeSpan(0.3f),
                        str.length() - 1,
                        str.length(),
                        0);
            }
            binding.firstScore.setText(str);
            str = new SpannableString(String.valueOf(value.second));
            if (str.charAt(0) == '6' || str.charAt(0) == '9') {
                str = new SpannableString(str.toString() + ".");
                str.setSpan(
                        new RelativeSizeSpan(0.3f),
                        str.length() - 1,
                        str.length(),
                        0);
            }
            binding.secondScore.setText(str);
        });
        binding.pause.setOnTouchListener((v, event) -> {
            int action = event.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    v.setAlpha(1);
                    break;
                }
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP: {
                    switch (((TextView)v).getText().toString()) {
                        case "||": {
                            mViewModel.pause();
                            break;
                        }
                        default:
                        case "|>": {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mViewModel.resume();
                            break;
                        }
                    }
                    v.setAlpha(0.2f);
                    break;
                }
            }
            return true;
        });
        GameMode gameMode = mViewModel.getGameMode();
        switch (gameMode) {
            case VsPlayer:
                break;
            case VsComputer:
                binding.secondControls.setVisibility(View.GONE);
                break;
        }

        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                countdown = new Timer("countDown");
                countdown.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(() -> binding.countdownText.setText("3"));
                        }
                    }
                }, 0);
                countdown.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(() -> binding.countdownText.setText("2"));
                        }
                    }
                }, 1000);
                countdown.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(() -> binding.countdownText.setText("1"));
                        }
                    }
                }, 2000);
                countdown.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(() -> {
                                binding.countdownText.setText(R.string.start);
                                binding.countdownText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
                            });
                        }
                    }
                }, 3000);

                //Background resize
                int width = binding.getRoot().getWidth();
                int height = binding.getRoot().getHeight();
                int diameter = (int) Math.ceil(1.2 * Math.sqrt(width * width + height * height));
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(diameter, diameter);
                binding.countdownBackground.setLayoutParams(params);

                //Background centring
                ConstraintSet set = new ConstraintSet();
                set.clone((ConstraintLayout) binding.getRoot());
                set.connect(binding.countdownBackground.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                set.connect(binding.countdownBackground.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
                set.connect(binding.countdownBackground.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
                set.connect(binding.countdownBackground.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
                set.applyTo((ConstraintLayout) binding.getRoot());

                //Background animation
                Animation animation = new ScaleAnimation(1f, 0f, 1f, 0f, diameter/2f, diameter/2f);
                animation.setDuration(3600);
                animation.setFillAfter(true);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        binding.countdown.setVisibility(View.GONE);
                        mViewModel.countdownEnded();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
                binding.countdownBackground.setAnimation(animation);

                binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        return binding.getRoot();
    }

    @Override
    public void exit() {

    }

    @Override
    public void onPause() {
        super.onPause();
        ((TextView) binding.pause).setText("|>");
        mViewModel.pause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(countdown != null) {
            countdown.cancel();
        }
        mViewModel.viewDetach();
        InputController.clearInputs();
    }

    private static class GameButtonsClickListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int motionAction = event.getActionMasked();
            Action action = null;
            Player player = null;
            Side side = null;
            switch (motionAction) {
                case MotionEvent.ACTION_DOWN:
                    v.setAlpha(1);
                    action = Action.Start;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    v.setAlpha(0.2f);
                    action = Action.Stop;
                    break;
            }
            switch (v.getId()) {
                case R.id.firstPlayerLeft:
                    player = Player.First;
                    side = Side.Left;
                    break;
                case R.id.firstPlayerRight:
                    player = Player.First;
                    side = Side.Right;
                    break;
                case R.id.secondPlayerLeft:
                    player = Player.Second;
                    side = Side.Left;
                    break;
                case R.id.secondPlayerRight:
                    player = Player.Second;
                    side = Side.Right;
                    break;
            }

            assert player != null;
            assert action != null;
            InputController.performPlayerMoveEvent(new Trio<>(player, side, action));

            v.performClick();
            return true;
        }
    }

    public static class DecelerateInterpolator implements Interpolator {

        @Override
        public float getInterpolation(float input) {
            return (float) (0.1 * Math.E * Math.log(input) + 1.01);
        }
    }
}