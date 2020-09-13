package com.kn160642.cats.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.kn160642.cats.R;
import com.kn160642.cats.db.Entities.Component;

public class RenderHelper {

    public static class RenderSizes {
        public static final double PILLAR_OFFSET_TOP_FACTOR = 4;
        public static final double PILLAR_WIDTH_FACTOR = 8;

        public static final double VEHICLE_INITIAL_HEIGHT_FACTOR = 4;
        public static final double VEHICLE_HEIGHT_FACTOR =1.5 * VEHICLE_INITIAL_HEIGHT_FACTOR;
        public static final double VEHICLE_WIDTH_FACTOR =1.5* 3;

        public static final int VEHICLE_SPEED = 2;
        public static final int PILLAR_SPEED = 5;
        public static final int PILLAR_HIT = 2;

        public static final int OFFSET_GROUND_FACTOR = 16;

        public static final int PEDESTAL_HEIGHT_FACTOR = 5;
        public static final int PEDESTAL_WIDTH_FACTOR = 1;

        public static Rect getWallStartingRect(int width, int height, int ord){
            int wallWidth = (int) (width/PILLAR_WIDTH_FACTOR);
            int wallHeigh = (int) (height - OFFSET_GROUND_FACTOR - height/PILLAR_OFFSET_TOP_FACTOR);
            int top = (int) (height/PILLAR_OFFSET_TOP_FACTOR);
            int bottom = top+wallHeigh;
            int left;
            if(ord == 1){
                left = 0 - wallWidth;
            }
            else{
                left = width;
            }
            int right = left+wallWidth;
            return new Rect(left,top,right,bottom);
        }

        public static Rect getVehicleInTheGarageRect(int width, int height){
            return new Rect(0, height - height/2,width, (int) (height - height/2 +height/VEHICLE_INITIAL_HEIGHT_FACTOR));
        }

        public static Rect getPedestalRect(int width, int height){
            int top = (int) (height - height/2+height/VEHICLE_HEIGHT_FACTOR);
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
            int top = (int) (height - height/VEHICLE_HEIGHT_FACTOR);
            int bottom = height;
            int left = isLeft? (int) (width - width / VEHICLE_WIDTH_FACTOR) :0;
            int right = (int) (left + width/VEHICLE_WIDTH_FACTOR);
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
    public static Matrix rotateBitmap(Bitmap source,Rect r, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.reset ();

        float vw = r.width();
        float vh = r.height();
        float hvw = vw / 2;
        float hvh = vh / 2;
        float bw = (float) source.getWidth ();
        float bh = (float) source.getHeight ();

        // First scale the bitmap to fit into the view.
        // Use either scale factor for width and height,
        // whichever is the smallest.
        float s1x = vw / bw;
        float s1y = vh / bh;
        float s1 = (s1x < s1y) ? s1x : s1y;
        matrix.postScale (s1, s1);

        // Translate the image up and left half the height
        // and width so rotation (below) is around the center.
        matrix.postTranslate(-hvw, -hvh);

        // Rotate the bitmap the specified number of degrees.
        int rotation = (int)angle;
        matrix.postRotate(rotation);

        // If the bitmap is to be scaled, do so.
        // Also figure out the x and y offset values, which start
        // with the values assigned to the view
        // and are adjusted based on the scale.
//        float offsetX = getOffsetX (), offsetY = getOffsetY ();
        float offsetX = r.left, offsetY = r.top;
        float pScale = 1.0f;
        if (pScale != 1.0f) {

            matrix.postScale (pScale, pScale);

            float sx = (0.0f + pScale) * vw / 2;
            float sy = (0.0f + pScale) * vh / 2;

            offsetX += sx;
            offsetY+= sy;

        } else {
            offsetX += hvw;
            offsetY += hvh;
        }

        // The last translation moves the bitmap to where it has to be to have its top left point be
        // where it should be following the rotation and scaling.
        matrix.postTranslate (offsetX, offsetY);

        // Finally, draw the bitmap using the matrix as a guide.
        return matrix;

    }

}
