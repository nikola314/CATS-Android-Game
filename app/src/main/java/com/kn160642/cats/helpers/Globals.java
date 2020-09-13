package com.kn160642.cats.helpers;

public class Globals {
    private static String user;
    public static String sharedPreferencesName = "AppPrefs";
    public static String musicOn = "musicOn";
    public static String botPlays = "botPlays";

    public static final int maxProgress = 300;

    public static final int defaultWeaponId = 7;
    public static final int defaultChassisId = 1;
    public static final int defaultWheelsId = 3;
    public static final double hitFactor = 20;
    public static final double deadLockDecrement = 0.2;
    public static final long gameDuration = 1000 * 17;
    public static final double pillarHitStrength = 0.3;
    public static final double weaponDecrement = 0.05;


    public static String getActiveUser(){
        return user;
    }

    public static void setActiveUser(String user){
        Globals.user = user;
    }

    public static long getActiveUserId(){
        return Long.parseLong(user.split(":")[0]);
    }
}
