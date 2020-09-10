package com.kn160642.cats.battle;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.helpers.RenderHelper;
import com.kn160642.cats.helpers.TypesHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vehicle {
    private List<Component> components;
    Map<Component, Bitmap> compImages;
    private Resources res;

    private boolean isLeft;
    private Rect position;

    public Rect getPosition() {
        return position;
    }

    public void setPosition(Rect position) {
        this.position = position;
    }

    public static boolean intersects(Vehicle v1, Vehicle v2){
        // TODO: implement
        return false;
    }

    private Vehicle(){
        position = null;
        compImages = new HashMap<>();

    }

    public Vehicle(List<Component> comps, boolean isLeft, Resources res){
        this();
        this.isLeft = isLeft;
        components = comps;
        components.sort(new Comparator<Component>() {
            @Override
            public int compare(Component o1, Component o2) {
                return o1.getType() - o2.getType();
            }
        });
        this.res = res;
        mapComponentsWithImages();
    }

    public Vehicle(boolean isLeft){
        this();
        this.isLeft = isLeft;
        components = new ArrayList<>();
    }

    private void mapComponentsWithImages(){
        Bitmap[] images = RenderHelper.getBitmapArray(res);
        for(Component c: components){
            // TODO: if right direction???
            int bmpIndex = RenderHelper.getBitmapIndexForComponent(c);
            if(!isLeft) {
                bmpIndex = bmpIndex+1;
            }
            compImages.put(c,images[bmpIndex]);
        }
    }

    public void draw(Canvas canvas){
        for(Component c: components){
            Rect r = position;
            if(c.getType() == TypesHelper.ComponentType.WHEELS){
                r = RenderHelper.RenderSizes.getWheelRect(1,position, isLeft);
                canvas.drawBitmap(compImages.get(c),null, r,null);
                r = RenderHelper.RenderSizes.getWheelRect(2,position, isLeft);

            }
            else if(c.getType() == TypesHelper.ComponentType.WEAPON){
                r = RenderHelper.RenderSizes.getWeaponRect(position, c, isLeft);
            }
            canvas.drawBitmap(compImages.get(c),null, r,null);
        }
    }
}
