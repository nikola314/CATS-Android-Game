package com.kn160642.cats.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.kn160642.cats.R;
import com.kn160642.cats.db.Entities.Component;

public class RenderHelper {

    public static class RenderSizes {

        public static final int VEHICLE_HEIGHT_FACTOR = 4;
        public static final int VEHICLE_WIDTH_FACTOR = 3;

        public static final int OFFSET_GROUND_FACTOR = 16;

        public static final int PEDESTAL_HEIGHT_FACTOR = 5;
        public static final int PEDESTAL_WIDTH_FACTOR = 1;

        public static Rect getVehicleInTheGarageRect(int width, int height){
            return new Rect(0, height - height/2,width, height - height/2 +height/VEHICLE_HEIGHT_FACTOR);
        }

        public static Rect getPedestalRect(int width, int height){
            int top =height - height/2+height/VEHICLE_HEIGHT_FACTOR;
            return new Rect(0, top ,width/PEDESTAL_WIDTH_FACTOR, top + height/PEDESTAL_HEIGHT_FACTOR);
        }

        public static Rect getWheelRect(int ord, Rect vehicle, boolean isLeft){
            int height = 2*vehicle.height()/3;
            int top = vehicle.top + (vehicle.bottom-vehicle.top)/2;
            int bottom = top+height;
            int width = vehicle.width()/4;
            int left;
            if(isLeft){
                left = ord==1? vehicle.left+20: vehicle.right-width;

            } else{
                left = ord == 1? vehicle.right-20-width: vehicle.left+20;
            }
            int right = left+width;

            top= top-vehicle.height()/10;
            return new Rect(left,top,right,bottom);
        }

        public static Rect getWeaponRect(Rect vehicle, Component c, boolean isLeft){
            int top, left, right, bottom;
            top = vehicle.top-vehicle.height()/5;
            bottom = vehicle.top+vehicle.height()/3;
            left = vehicle.left;
            if(!isLeft){
                left = vehicle.right - vehicle.width()/4;
            }
            right = left+vehicle.width()/4;
            switch(c.getName()) {
                case "rocket":
                case "drill":
                case"chainsaw":
                    break;
                case "blade":
                    right=left+vehicle.width()/6;
                    break;
                case "forklift":
                    top = top-vehicle.height()/2;
                    right = left+vehicle.width()/8;
                    break;
            }

            return new Rect(left,top,right,bottom);
        }

        public static Rect getWheelRect(int ord, Rect vehicle){
            return getWheelRect(ord,vehicle,true);
        }

        public static Rect getWeaponRect(Rect vehicle, Component c){
            return getWeaponRect(vehicle, c, true);
        }

        public static Rect getVehicleStartingRect(int width, int height, boolean isLeft){
            int top = height - height/VEHICLE_HEIGHT_FACTOR;
            int bottom = height;
            int left = isLeft?  width-width/VEHICLE_WIDTH_FACTOR:0;
            int right = left + width/VEHICLE_WIDTH_FACTOR;
            top-= height/OFFSET_GROUND_FACTOR;
            bottom-= height/OFFSET_GROUND_FACTOR;
            return new Rect(left,top,right,bottom);
        }
    }

    public enum BitmapIndex {
        PEDESTAL(0),
        CHASSIS0(1),
        CHASSIS0RIGHT(2),
        CHASSIS1(3),
        CHASSIS1RIGHT(4),
        DRILL(5),
        DRILLRIGHT(6),
        BLADE(7),
        BLADERIGHT(8),
        CHAINSAW(9),
        CHAINSAWRIGHT(10),
        FORKLIFT(11),
        FORKLIFTRIGHT(12),
        ROCKET(13),
        ROCKETRIGHT(14),
        WHEEL0(15),
        WHEEL1(16),
        WHEEL2(17);

        private int value;
        private BitmapIndex(int value) { this.value = value; }

        public int getValue(){ return value;}
    };

    public static Bitmap[] getBitmapArray(Resources res){
        Bitmap[] bitmaps = new Bitmap[]{
                BitmapFactory.decodeResource(res, R.drawable.pedestal),
                BitmapFactory.decodeResource(res, R.drawable.chassis0),
                BitmapFactory.decodeResource(res, R.drawable.chassis0right),
                BitmapFactory.decodeResource(res, R.drawable.chassis1),
                BitmapFactory.decodeResource(res, R.drawable.chassis1right),
                BitmapFactory.decodeResource(res, R.drawable.drill),
                BitmapFactory.decodeResource(res, R.drawable.drillright),
                BitmapFactory.decodeResource(res, R.drawable.blade),
                BitmapFactory.decodeResource(res, R.drawable.bladeright),
                BitmapFactory.decodeResource(res, R.drawable.chainsaw),
                BitmapFactory.decodeResource(res, R.drawable.chainsawright),
                BitmapFactory.decodeResource(res, R.drawable.forklift),
                BitmapFactory.decodeResource(res, R.drawable.forkliftright),
                BitmapFactory.decodeResource(res, R.drawable.rocket),
                BitmapFactory.decodeResource(res, R.drawable.rocketright),
                BitmapFactory.decodeResource(res, R.drawable.wheel0),
                BitmapFactory.decodeResource(res, R.drawable.wheel1),
                BitmapFactory.decodeResource(res, R.drawable.wheel2)
        };
        return bitmaps;
    }

    public static int getImageResourceIdForComponent(Component component) {
        switch (component.getName()){
            case "chassis0": return R.drawable.chassis0;
            case "chassis1": return R.drawable.chassis1;
            case "wheel0": return R.drawable.wheel0;
            case "wheel1": return R.drawable.wheel1;
            case "wheel2": return R.drawable.wheel2;
            case "blade": return R.drawable.blade;
            case "chainsaw": return R.drawable.chainsaw;
            case "drill": return R.drawable.drill;
            case "forklift": return R.drawable.forklift;
            case "rocket": return R.drawable.rocket;
        }
        return -1;
    }

    public static int getBitmapIndexForComponent(Component component){
        switch (component.getName()){
            case "chassis0": return 1;
            case "chassis1": return 3;
            case "wheel0": return 15;
            case "wheel1": return 16;
            case "wheel2": return 17;
            case "blade": return 7;
            case "chainsaw": return 9;
            case "drill": return 5;
            case "forklift": return 11;
            case "rocket": return 13;
        }
        return -1;
    }
}
