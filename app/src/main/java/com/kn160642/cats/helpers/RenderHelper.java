package com.kn160642.cats.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.kn160642.cats.R;

public class RenderHelper {
    public static class RenderSizes {

        public static final int VEHICLE_HEIGHT_FACTOR = 4;
        public static final int VEHICLE_WIDTH_FACTOR = 5;

        public static final int PEDESTAL_HEIGHT_FACTOR = 5;
        public static final int PEDESTAL_WIDTH_FACTOR = 3;

        public static Rect getVehicleInTheGarageRect(int width, int height){
            return new Rect(width/3, height - height/2,width/3 +width/PEDESTAL_WIDTH_FACTOR, height - height/2 +height/VEHICLE_HEIGHT_FACTOR);
        }

        public static Rect getPedestalRect(int width, int height){
            int top =height - height/2+height/VEHICLE_HEIGHT_FACTOR;
            return new Rect(width/3, top ,width/3 +width/PEDESTAL_WIDTH_FACTOR, top + height/PEDESTAL_HEIGHT_FACTOR);
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
}
