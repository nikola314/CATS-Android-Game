package com.kn160642.cats.battle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kn160642.cats.R;
import com.kn160642.cats.db.Entities.Box;
import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.db.Entities.History;
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

    private long gameStart;
    private boolean deadLock;

    private Rect[] walls;
    private Bitmap pillarBitmap;

    private Paint textPaint;

    private void init(){
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        textPaint = new Paint();
        textPaint.setTextSize(200);
        textPaint.setFakeBoldText(true);
        textPaint.setColor(Color.RED);
        textPaint.setTextAlign(Paint.Align.CENTER);
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
        gameStart = System.currentTimeMillis();
        pillarBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pillars);
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

                for(Vehicle v: vehicles){
                    v.setWidth(screenWidth);
                    v.setHeight(screenHeight);
                }

                playing = true;
            }
        }).start();
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
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            for(Vehicle v: vehicles){
                v.draw(canvas);
            }
            if(walls != null){
                for(Rect pillar: walls){
                    canvas.drawBitmap(pillarBitmap, null, pillar,null);
                }
            }


            if(checkForEndOfGame()){
                drawWinner(canvas);
                return;
            }
        }
    }

    private int getResult(){
        if(vehicles[PLAYER].getHealth() > vehicles[BOT].getHealth()){
            return TypesHelper.ResultType.WON;
        }
        else if(vehicles[PLAYER].getHealth() > vehicles[BOT].getHealth()){
            return TypesHelper.ResultType.LOST;
        }
        else{
            return TypesHelper.ResultType.DRAW;
        }
    }

    private void drawWinner(Canvas canvas) {
        String[] winner = new String[]{"You won!", "Opponent won!", "It's a draw!"};
        int result = getResult();
        canvas.drawText(winner[result],screenWidth/2, screenHeight/2,textPaint);
    }

    public void update() {
        if(!playing){
            return;
        }
        boolean end = checkForEndOfGame();
        if(end){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    History stat = new History();
                    stat.setResult(getResult());
                    stat.setUserId(Globals.getActiveUserId());
                    MyDatabase db = MyDatabase.getInstance(getContext());
                    db.historyDao().insertStats(stat);
                    if(stat.getResult() == TypesHelper.ResultType.WON){
                        int gamesWon = db.historyDao().getCountOfGamesWon(Globals.getActiveUserId());
                        if(gamesWon%Globals.winsForBox == 0){
                            Box b = new Box();
                            b.setUserId(Globals.getActiveUserId());
                            b.setTimestamp(System.currentTimeMillis());
                            b.setTimeToOpen(Globals.defaultTimeToOpen);
                            b.setOpened(false);
                            db.boxDao().insertBox(b);
                            Log.i("BOX","inserted");
                        }
                    }
                }
            }).start();

            // todo: put result in database, stats
            playing = false;
            return;
        }
        deadLock = false;
        initVehiclePositionsIfNeeded();
        updateWalls();

        if(!Rect.intersects(vehicles[PLAYER].getPosition(), vehicles[BOT].getPosition())){
            for(Vehicle v: vehicles){
                boolean toMove = true;
                if(walls!= null){
                    for(Rect r: walls){
                        if(Rect.intersects(r,v.getPosition())){
                            toMove = false;
                        }
                    }
                }
                if(toMove){
                    v.moveForward(RenderHelper.RenderSizes.VEHICLE_SPEED);
                }
            }
        }else{
            deadLock = true;
        }


        handleCollisions();
        for(int i=0; i<2;i++){
            vehicles[i].updateComponents(vehicles[1-i]);
        }
    }

    private boolean checkForEndOfGame() {
        for(Vehicle v:vehicles){
            if(v.getHealth() <= 0) return true;
        }
        return false;
    }

    private void updateWalls(){
        if(walls == null){
            if(System.currentTimeMillis() - gameStart > Globals.gameDuration){
                walls = new Rect[2];
                Rect r = RenderHelper.RenderSizes.getWallStartingRect(screenWidth, screenHeight, 1);
                walls[0]= r;
                r = RenderHelper.RenderSizes.getWallStartingRect(screenWidth, screenHeight, 2);
                walls[1]=r;
            }
        }
        else{
            moveWalls();
        }
    }

    private void moveWalls() {
        for(int i=0;i<2;i++){
            boolean done = false;
            for(Vehicle v: vehicles) {
                if(Rect.intersects(walls[i],v.getPosition())) {
                    done = true;
                }
            }
            if(done){
                for(int j=0;j<2;j++){
                    if(Rect.intersects(walls[i],vehicles[j].getPosition())){
                        vehicles[j].hit(Globals.pillarHitStrength);
                    }
                }
                continue;
            }
            else{
                int dir = i==0? 1:-1;
                walls[i].left += RenderHelper.RenderSizes.PILLAR_SPEED*dir;
                walls[i].right += RenderHelper.RenderSizes.PILLAR_SPEED*dir;
            }
        }
    }

    private void handleCollisions() {
        boolean collided = Rect.intersects(vehicles[PLAYER].getPosition(), vehicles[BOT].getPosition());
        for(int i=0;i<2;i++){
            Rect weaponRect = vehicles[i].getWeaponRect();
            if(weaponRect == null) return;
            if(Rect.intersects(weaponRect, vehicles[1-i].getPosition())){
                double power = vehicles[i].getComponentOf(TypesHelper.ComponentType.WEAPON).getPower();
                vehicles[1-i].hit((power/Globals.hitFactor) * Globals.weaponDecrement);
            }
        }

        if(collided){
            int strenght1 = vehicles[PLAYER].getStrength();
            int strenght2 = vehicles[BOT].getStrength();
            int diff = strenght1-strenght2;

            Vehicle toMove = diff>0? vehicles[BOT]:vehicles[PLAYER];
            int movement = diff>0? -diff:diff;
            if(walls != null){
                boolean canMove = true;
                for(Rect wall:walls){
                    if(Rect.intersects(wall, toMove.getPosition())){
                        canMove = false;
                    }
                }
                if(canMove){
                    toMove.moveForward(movement);
                }
            }
            else{
                toMove.moveForward(movement);
            }
            for(int i=0;i<2;i++){
                Vehicle opponent = vehicles[1-i];
            }
            double strength = diff>0?diff:-diff;
            double hit = strength/Globals.hitFactor;
            if(deadLock) hit *= Globals.deadLockDecrement;
            vehicles[PLAYER].hit(hit);
            vehicles[BOT].hit(hit);
        }
    }
}
