package com.rusloker.pong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class GdxVisualiserFragment extends AndroidFragmentApplication {
    GdxVisualiser game;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        game = new GdxVisualiser();
        return initializeForView(game);
    }
}