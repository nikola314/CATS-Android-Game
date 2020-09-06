package com.kn160642.cats.db.handlers;

import android.content.Context;
import android.util.Log;

import com.kn160642.cats.db.Entities.User;
import com.kn160642.cats.db.MyDatabase;

public class UserHandler {

    public static void addUser(final Context context, final String username){
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User(username);
                long userId = MyDatabase.getInstance(context).userDao().insertUser(user);
                Log.i("USERID",Long.toString(userId));
            }
        }).start();
    }
}
