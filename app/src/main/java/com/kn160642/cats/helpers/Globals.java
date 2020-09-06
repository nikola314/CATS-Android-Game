package com.kn160642.cats.helpers;

public class Globals {
    private static String user;

    public static String getActiveUser(){
        return user;
    }

    public static void setActiveUser(String user){
        Globals.user = user;
    }
}
