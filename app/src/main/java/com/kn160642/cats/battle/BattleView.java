package com.kn160642.cats.battle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class BattleView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private int screenWidth, screenHeight;
    private Vehicle[] vehicles;

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
    }

    public void update() {
        // TODO: implement
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {

            canvas.drawColor(Color.TRANSPARENT);
            // TODO: implement
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            canvas.drawCircle(100,100,50, paint);

        }
    }
}
