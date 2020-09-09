package com.kn160642.cats.game;

import android.content.Context;

import com.kn160642.cats.db.Entities.Box;
import com.kn160642.cats.db.MyDatabase;
import com.kn160642.cats.helpers.Globals;

import java.util.ArrayList;

public class BoxUpdateThread extends Thread{
    public volatile boolean running = true;
    private BoxAdapter boxAdapter;
    private Context context;

    public BoxUpdateThread(Context context, BoxAdapter boxAdapter){
        this.boxAdapter = boxAdapter;
    }

    @Override
    public void run() {
        while(running){
            try {
                sleep(500);
                boxAdapter.swapItems( new ArrayList<Box>(MyDatabase.getInstance(context).boxDao().getUnopenedBoxesForUserDeadData(Globals.getActiveUserId())));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
