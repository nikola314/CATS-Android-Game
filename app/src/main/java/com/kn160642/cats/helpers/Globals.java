package com.kn160642.cats.helpers;

public class Globals {
    private static String username;

    public static String getActiveUser(){
        return username;
    }

    public static void setActiveUser(String username){
        Globals.username = username;
    }
}
