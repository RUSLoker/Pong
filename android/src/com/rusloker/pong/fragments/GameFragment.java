package com.rusloker.pong.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class GameFragment extends Fragment implements AndroidFragmentApplication.Callbacks {
    private GdxVisualiserFragment gdxVisualiserFragment;
    private GameViewModel mViewModel;
    private FragmentGameBinding binding;
    Thread countdown;

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
                countdown = new Thread(() -> {
                    FragmentActivity activity = getActivity();
                    if(activity == null) {
                        return;
                    }
                    activity.runOnUiThread(() -> binding.countdownText.setText("3"));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    activity.runOnUiThread(() -> binding.countdownText.setText("2"));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    activity.runOnUiThread(() -> binding.countdownText.setText("1"));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    activity.runOnUiThread(() -> {
                        binding.countdownText.setText(R.string.start);
                        binding.countdownText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mViewModel.countdownEnded();
                });

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
                animation.setDuration(3500);
                animation.setFillAfter(true);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        countdown.start();
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        binding.countdown.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        Log.i("animation", "repeat");
                    }
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
    public void onDetach() {
        super.onDetach();
        if(countdown != null) {
            countdown.interrupt();
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
            return (float) (0.1 * Math.E * Math.log(input) + 1);
        }
    }
}