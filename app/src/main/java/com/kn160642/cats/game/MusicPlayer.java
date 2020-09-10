package com.kn160642.cats.game;

import android.content.Context;
import android.media.MediaPlayer;

import com.kn160642.cats.R;

public class MusicPlayer {

    MediaPlayer mediaPlayer;
    Context context;

    public MusicPlayer(Context context){
        this.context = context;
    }

    public void pause(){
        if(mediaPlayer!= null)
        mediaPlayer.pause();
    }

    public void resume(){
        if(mediaPlayer!= null)
            mediaPlayer.start();
    }

    public void start(){
        mediaPlayer = MediaPlayer.create(context, R.raw.track0);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void stop(){
        if(mediaPlayer != null)
        mediaPlayer.stop();
    }
}