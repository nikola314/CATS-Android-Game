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
