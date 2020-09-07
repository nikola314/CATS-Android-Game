package com.kn160642.cats.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kn160642.cats.battle.MainThread;
import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.db.Entities.User;
import com.kn160642.cats.db.MyDatabase;
import com.kn160642.cats.helpers.RenderHelper;
import com.kn160642.cats.helpers.TypesHelper;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public static Canvas canvas;
    SurfaceHolder surfaceHolder;

    private Component[] selectedComponents = new Component[3];
    Bitmap[] bitmaps;

    private void init(){
        getHolder().addCallback(this);
        setFocusable(true);
    }


    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = getHolder();
        bitmaps = RenderHelper.getBitmapArray(getResources());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void draw(final User u){
        Log.i("DRAW", "USAO");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("DRAW", "RUN");
                Log.i("DRAW", u.getUsername());
                if(u.getChassisId() > 0) {
                    selectedComponents[TypesHelper.ComponentType.CHASSIS] =
                            MyDatabase.getInstance(getContext()).componentDao().getComponentById(u.getChassisId());
                }
                if(u.getWheelsId() > 0) {
                    selectedComponents[TypesHelper.ComponentType.WHEELS] =
                            MyDatabase.getInstance(getContext()).componentDao().getComponentById(u.getWheelsId());
                }
                if(u.getWeaponId() > 0) {
                    selectedComponents[TypesHelper.ComponentType.WEAPON] =
                            MyDatabase.getInstance(getContext()).componentDao().getComponentById(u.getWeaponId());
                }
                drawVehicle();
            }
        }).start();
    }

    public void changeSelectedComponent(Component c){
        selectedComponents[c.getType()] = c;
        drawVehicle();
    }

    public void drawVehicle(){
        canvas = null;
        try {
            surfaceHolder = getHolder();
            canvas = surfaceHolder.lockCanvas();
            synchronized(surfaceHolder) {

                draw(canvas);
            }
        } catch (Exception e) {  Log.i("DRAW", "EXCEPTION"); }
        finally {
            if (canvas != null)            {
                try {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private int offset=0;

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            drawPedestal(canvas);
            drawChassis(canvas);
            drawWheels(canvas);
            drawWeapons(canvas);

            // TODO: dont create paint in every draw
//            Paint paint = new Paint();
        }

    }

    private void drawPedestal(Canvas canvas) {
        Bitmap bmp = bitmaps[RenderHelper.BitmapIndex.PEDESTAL.getValue()];
        canvas.drawBitmap(bmp,null,RenderHelper.RenderSizes.getPedestalRect(canvas.getWidth(), canvas.getHeight()),null);
    }

    private void drawChassis(Canvas canvas) {
        Component c = selectedComponents[TypesHelper.ComponentType.CHASSIS];
        int bmpIndex = RenderHelper.getBitmapIndexForComponent(c);
        Bitmap bmp = bitmaps[bmpIndex];
        canvas.drawBitmap(bmp,null,RenderHelper.RenderSizes.getVehicleInTheGarageRect(canvas.getWidth(), canvas.getHeight()),null);
    }

    private void drawWheels(Canvas canvas) {
        Component c = selectedComponents[TypesHelper.ComponentType.WHEELS];
        int bmpIndex = RenderHelper.getBitmapIndexForComponent(c);
        Bitmap bmp = bitmaps[bmpIndex];

        Rect vehicle = RenderHelper.RenderSizes.getVehicleInTheGarageRect(canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(bmp,null,RenderHelper.RenderSizes.getWheelRect(1,vehicle),null);
        canvas.drawBitmap(bmp,null,RenderHelper.RenderSizes.getWheelRect(2,vehicle),null);
    }

    private void drawWeapons(Canvas canvas) {
        Component c = selectedComponents[TypesHelper.ComponentType.WEAPON];
        int bmpIndex = RenderHelper.getBitmapIndexForComponent(c);
        Bitmap bmp = bitmaps[bmpIndex];

        Rect vehicle = RenderHelper.RenderSizes.getVehicleInTheGarageRect(canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(bmp,null,RenderHelper.RenderSizes.getWeaponRect(vehicle, c),null);
    }
}
