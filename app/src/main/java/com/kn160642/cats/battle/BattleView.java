package com.kn160642.cats.battle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.db.Entities.User;
import com.kn160642.cats.db.MyDatabase;
import com.kn160642.cats.helpers.Globals;
import com.kn160642.cats.helpers.RenderHelper;
import com.kn160642.cats.helpers.TypesHelper;

import java.util.ArrayList;
import java.util.List;


public class BattleView extends SurfaceView implements SurfaceHolder.Callback {
    private final int PLAYER = 0;
    private final int BOT = 1;

    private MainThread thread;
    private int screenWidth, screenHeight;

    private Vehicle[] vehicles;
    private boolean playing;

    private void init(){
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    public BattleView(Context context) {
        super(context);
        init();
    }

    public BattleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BattleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BattleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = null;
        try{
            canvas = holder.lockCanvas();
            screenHeight = canvas.getHeight();
            screenWidth = canvas.getWidth();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            holder.unlockCanvasAndPost(canvas);
        }

        initVehicles();
        if(thread.getState() == Thread.State.TERMINATED){
            thread = new MainThread(getHolder(), this);
        }
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    private void initVehicles(){
        // TODO: implement
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDatabase db = MyDatabase.getInstance(getContext());

                User user = db.userDao().getUserById(Globals.getActiveUserId());
                List<Component> userCmps = db.componentDao().getThreeComponentsDead(user.getChassisId(), user.getWeaponId(), user.getWheelsId());
                List<Component> botCmps = db.componentDao().getThreeComponentsDead(Globals.defaultChassisId, Globals.defaultWeaponId, Globals.defaultWheelsId);
                vehicles = new Vehicle[2];
                vehicles[PLAYER] = new Vehicle(userCmps, true, getResources());
                vehicles[BOT] = new Vehicle(botCmps,false,getResources());

                playing = true;
            }
        }).start();
    }

    public void update() {
        if(!playing){
            return;
        }
        initVehiclePositionsIfNeeded();
        // TODO: implement
    }

    private void initVehiclePositionsIfNeeded() {
        if(vehicles[PLAYER].getPosition() == null){
            vehicles[PLAYER].setPosition(RenderHelper.RenderSizes.getVehicleStartingRect(screenWidth,screenHeight,true));
            vehicles[BOT].setPosition(RenderHelper.RenderSizes.getVehicleStartingRect(screenWidth,screenHeight,false));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {

            canvas.drawColor(Color.TRANSPARENT);
            // TODO: implement
            if(!playing) return;

            for(Vehicle v: vehicles){
                v.draw(canvas);
            }

        }
    }
}
