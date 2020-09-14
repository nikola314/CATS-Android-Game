package com.kn160642.cats.battle;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.helpers.Globals;
import com.kn160642.cats.helpers.RenderHelper;
import com.kn160642.cats.helpers.TypesHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vehicle {
    private List<Component> components;
    private Map<Component, Bitmap> compImages;
    private Resources res;

    private boolean isLeft;
    private Rect position;

    private int startHealth;
    private double health;
    private Paint paint;

    private int width, height;

    private Rect weaponRect;
    private int weaponProduct = 1;
    private int weaponDirection; // 0 - vertical, 1 - horizontal
    private int weaponMaxOffset;
    private int weaponCurrentOffset;

    private float wheelRotationAngle;

    public int getWidth() {
        return width;
    }

    void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    void setHeight(int height) {
        this.height = height;
    }

    Rect getPosition() {
        return position;
    }

    void setPosition(Rect position) {
        this.position = position;
        initializeWeapon();
    }

    private Vehicle(){
        position = null;
        compImages = new HashMap<>();
        paint= new Paint(Color.BLACK);
    }

    Vehicle(List<Component> comps, boolean isLeft, Resources res){
        this();
        this.isLeft = isLeft;
        components = comps;
        startHealth = 0;
        for(Component c: comps){
            startHealth+= c.getHealth();
        }
        health = startHealth;
        this.res = res;
        mapComponentsWithImages();
        wheelRotationAngle = isLeft? 360: 0;
    }

    public Vehicle(boolean isLeft){
        this();
        this.isLeft = isLeft;
        components = new ArrayList<>();
    }

    private void mapComponentsWithImages(){
        Bitmap[] images = RenderHelper.getBitmapArray(res);
        for(Component c: components){
            int bmpIndex = RenderHelper.getBitmapIndexForComponent(c);
            if(!isLeft) {
                bmpIndex = bmpIndex+1;
            }
            compImages.put(c,images[bmpIndex]);
        }
    }

    void draw(Canvas canvas){
        for(Component c: components){
            if(c==null) continue;
            Rect r = position;
            Bitmap bmp = compImages.get(c);
            if(bmp==null || canvas == null || r == null) continue;
            if(c.getType() == TypesHelper.ComponentType.WHEELS){
                r = RenderHelper.RenderSizes.getWheelRect(1,position, isLeft);
                canvas.drawBitmap (bmp, RenderHelper.rotateBitmap(bmp,r,wheelRotationAngle), null);
                r = RenderHelper.RenderSizes.getWheelRect(2,position, isLeft);
                canvas.drawBitmap (bmp, RenderHelper.rotateBitmap(bmp,r,wheelRotationAngle), null);
            }
            else if(c.getType() == TypesHelper.ComponentType.WEAPON){
                r= weaponRect;
                canvas.drawBitmap(bmp,null, r,null);
            }
            else{
                canvas.drawBitmap(bmp,null, r,null);
            }

        }
        drawHealthBar(canvas);
    }

    public Rect getWeaponRect(){
        return weaponRect;
    }

    private void drawHealthBar(Canvas canvas){
        int start = isLeft? canvas.getWidth()/2:0;
        int height = 30;
        int top = 10, left=start + 10, right=start + canvas.getWidth()/2 -10, bottom = top+10+height;
        Rect r = new Rect(left,top,right,bottom);
        paint.setColor(Color.BLACK);
        canvas.drawRect(r,paint);
        r.top+=5; r.bottom-=5; r.left+=5; r.right-=5;
        paint.setColor(Color.GREEN);
        double ratio = health/(double)startHealth;

        if(isLeft){
            r.left = (int) (r.right - r.width() * ratio);
        }
        else{
            r.right = (int) (r.left+r.width()*ratio);
        }

        canvas.drawRect(r, paint);
    }

    public void updateComponents(Vehicle opponent){
        updateWeaponPosition(opponent);
        updateWheelPosition();
    }


    private void updateWheelPosition() {
        double dir = isLeft? -2:2;
        wheelRotationAngle += dir;
        if(wheelRotationAngle<0) wheelRotationAngle=360;
        if(wheelRotationAngle>360) wheelRotationAngle = 0;
    }

    private void initializeWeapon(){
        Component weapon = getComponentOf(TypesHelper.ComponentType.WEAPON);
        if(weapon == null) return;
        weaponDirection = 0; // vertical
        weaponCurrentOffset = 0;
        switch(weapon.getName()){
            case "rocket":
                weaponDirection = 1;
                weaponMaxOffset = Integer.MAX_VALUE;
                break;
            case "chainsaw": case "drill":
                weaponDirection = 1;
                weaponMaxOffset = position.width()/30;
                break;
            default:
                weaponMaxOffset = position.height()/2;
        }
    }

    private void updateWeaponPosition(Vehicle opponent) {
        Component weapon = getComponentOf(TypesHelper.ComponentType.WEAPON);
        if(weapon == null) return;
        weaponCurrentOffset += weaponProduct;
        if(weaponCurrentOffset == weaponMaxOffset || weaponCurrentOffset == 0){
            weaponProduct *= -1;
        }
        Rect r = RenderHelper.RenderSizes.getWeaponRect(position,weapon,isLeft);
        int dir = isLeft? -1:1;
        if(weaponDirection == 0){ // vertical
            r.offset(0,weaponCurrentOffset);
        }
        else{ // horizontal
             r.offset(weaponCurrentOffset*dir *5,0);
        }
        weaponRect = r;
        if(weapon.getName().equals("rocket")){
            if(Rect.intersects(weaponRect, opponent.getPosition())){
                opponent.hit((weapon.getPower()/ Globals.hitFactor)*Globals.weaponDecrement);
                weaponRect = RenderHelper.RenderSizes.getWeaponRect(position,weapon,isLeft);
                weaponCurrentOffset=0;
            }
        }
        if(weapon.getName().equals("forklift")){
            if(Rect.intersects(position, opponent.getPosition())) {
                opponent.lift();
            }
            else{
                if(!Rect.intersects(position,opponent.getPosition())){
                    opponent.gravity(this);
                }
            }
        }
    }

    public void gravity(Vehicle opponent){
        int offset = 10;
        int diff = height- height/RenderHelper.RenderSizes.OFFSET_GROUND_FACTOR - position.bottom;

        if(diff<0){
            offset+= diff;
        }
        if(Rect.intersects(position, opponent.getPosition())){
            offset-=10;
        }
        position.offset(0,offset);
    }

    public void lift(){
        this.position.offset(0, -10);
    }

    public void moveForward(int speed){
        if(isLeft) speed=-speed;
        int diff = 0;
        if(position.left+speed < 0){
            diff = position.left+speed;
        }
        else if(position.right+speed>width){
            diff = position.right+speed -width;
        }
        speed-=diff;

        position.left+=speed;
        position.right+=speed;
    }

    public Component getComponentOf(int type){
        for(Component c: components){
            if(c.getType() == type){
                return c;
            }
        }
        return null;
    }

    public double getHealth() {
        return health;
    }

    public int getStrength(){
        int strenght = 0;
        for(Component c:components){
            strenght+= c.getPower();
        }
        return strenght;
    }

    public void hit(double strenght){
        if(strenght < 0) return;
        health-=strenght;
    }

}
