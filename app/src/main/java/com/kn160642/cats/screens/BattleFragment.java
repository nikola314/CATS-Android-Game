package com.kn160642.cats.screens;


import android.graphics.PixelFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.kn160642.cats.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BattleFragment extends Fragment {


    public BattleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_battle, container, false);

        initCanvas(root);

        return root;
    }

    private void initCanvas(View root){
        SurfaceView sfvTrack = (SurfaceView)root.findViewById(R.id.battleView);
        sfvTrack.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);
    }


}
