package com.kn160642.cats.screens;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.kn160642.cats.R;
import com.kn160642.cats.battle.BattleView;
import com.kn160642.cats.helpers.Globals;


/**
 * A simple {@link Fragment} subclass.
 */
public class BattleFragment extends Fragment {

    public BattleFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_battle, container, false);

        initCanvas(root);
        setBotPlays(root);

        return root;
    }

    private void setBotPlays(View root) {
        final SharedPreferences sharedpreferences = getActivity().getSharedPreferences(Globals.sharedPreferencesName,
                Context.MODE_PRIVATE);
        final boolean botPlays = sharedpreferences.getBoolean(Globals.botPlays, true);
        BattleView battle = root.findViewById(R.id.battleView);
        battle.setBotPlays(botPlays);
    }

    private void initCanvas(View root){
        SurfaceView sfvTrack = root.findViewById(R.id.battleView);
        sfvTrack.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);
    }
}
