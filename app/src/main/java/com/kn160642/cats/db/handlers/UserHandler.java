package com.kn160642.cats.db.handlers;

import android.content.Context;
import android.util.Log;

import com.kn160642.cats.db.Entities.User;
import com.kn160642.cats.db.Entities.UserComponent;
import com.kn160642.cats.db.MyDatabase;
import com.kn160642.cats.helpers.Globals;

public class UserHandler {

    public static void addUser(final Context context, final String username){
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User(username);
                user.setChassisId(Globals.defaultChassisId);
                user.setWheelsId(Globals.defaultWheelsId);
                user.setWeaponId(Globals.defaultWeaponId);

                MyDatabase db = MyDatabase.getInstance(context);
                long userId = db.userDao().insertUser(user);

                db.componentDao().insertUserComponent(new UserComponent(userId, Globals.defaultChassisId));
                db.componentDao().insertUserComponent(new UserComponent(userId, Globals.defaultWheelsId));
                db.componentDao().insertUserComponent(new UserComponent(userId, Globals.defaultWeaponId));

            }
        }).start();
    }
}
