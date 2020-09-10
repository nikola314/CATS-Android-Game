package com.kn160642.cats.battle;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.kn160642.cats.game.GameView;

public class MainThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private BattleView battleView;
    private boolean running;
    public static Canvas canvas;

    private int targetFPS = 60;
    private double averageFPS;

    public MainThread(SurfaceHolder surfaceHolder, BattleView battleView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.battleView = battleView;
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / targetFPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder) {
                    this.battleView.update();
                    this.battleView.draw(canvas);
                }
            } catch (Exception e) {       }
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

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            } catch (Exception e) {}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == targetFPS)        {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }
}
