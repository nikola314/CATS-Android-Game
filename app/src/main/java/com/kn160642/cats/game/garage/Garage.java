package com.kn160642.cats.game.garage;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.kn160642.cats.R;
import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.helpers.RenderHelper;

public class Garage {
    private Bitmap[] bitmaps;
    private Component[] components;

    public Garage(Resources res){
        bitmaps = RenderHelper.getBitmapArray(res);
        components = getUserComponents();
    }

    private Component[] getUserComponents(){
        // TODO: implement


        return null;
    }

    public void draw(Canvas canvas) {
        // TODO: implement
    }

}
