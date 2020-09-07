package com.kn160642.cats.db;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.db.Entities.User;
import com.kn160642.cats.db.Entities.UserComponent;
import com.kn160642.cats.db.dao.ComponentDao;
import com.kn160642.cats.db.dao.UserDao;
import com.kn160642.cats.helpers.TypesHelper;

import java.util.List;
import java.util.concurrent.Executors;


@Database(entities = {User.class, Component.class, UserComponent.class}, version = 8, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ComponentDao componentDao();

    private static MyDatabase instance = null;

    public static MyDatabase getInstance(final Context context){
        if(instance == null){
            synchronized (MyDatabase.class){
                instance = Room
                        .databaseBuilder(context.getApplicationContext(), MyDatabase.class, "my-database")
                        .fallbackToDestructiveMigration()
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                prepopulateDatabase(context);
                                Log.i("DATABASE", "onCreate");
                            }

                            @Override
                            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                super.onOpen(db);
                                Log.i("DATABASE", "onOpen");
                            }
                        })
                        .build();
            }
        }
        return instance;
    }

    private static void prepopulateDatabase(final Context context){
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                MyDatabase db = getInstance(context);

                Component c = new Component("chassis0",80, 70, 70, TypesHelper.ComponentType.CHASSIS);
                long chassis = db.componentDao().insertComponent(c);
                c = new Component("chassis1",50, 100, 100, TypesHelper.ComponentType.CHASSIS);
                long chassis2 = db.componentDao().insertComponent(c);
                c = new Component("wheel0",20, 70, 70, TypesHelper.ComponentType.WHEELS);
                long wheels = db.componentDao().insertComponent(c);
                c = new Component("wheel1",20, 70, 70, TypesHelper.ComponentType.WHEELS);
                long wheels2 =db.componentDao().insertComponent(c);
                c = new Component("wheel2",20, 70, 70, TypesHelper.ComponentType.WHEELS);
                long wheels3 = db.componentDao().insertComponent(c);
                c = new Component("blade",20, 70, 70, TypesHelper.ComponentType.WEAPON);
                long weapon = db.componentDao().insertComponent(c);
                c = new Component("chainsaw",20, 70, 70, TypesHelper.ComponentType.WEAPON);
                long weapon2 = db.componentDao().insertComponent(c);
                c = new Component("drill",20, 70, 70, TypesHelper.ComponentType.WEAPON);
                long weapon3 = db.componentDao().insertComponent(c);
                c = new Component("forklift",20, 70, 70, TypesHelper.ComponentType.WEAPON);
                long weapon4 =db.componentDao().insertComponent(c);
                c = new Component("rocket",20, 70, 70, TypesHelper.ComponentType.WEAPON);
                long weapon5 = db.componentDao().insertComponent(c);

                User user = new User("nikola");
                user.setChassisId(chassis);
                user.setWheelsId(wheels);
                user.setWeaponId(weapon);
                long userId = db.userDao().insertUser(user);

                db.componentDao().insertUserComponent(new UserComponent(userId, chassis));
                db.componentDao().insertUserComponent(new UserComponent(userId, chassis2));
                db.componentDao().insertUserComponent(new UserComponent(userId, wheels));
                db.componentDao().insertUserComponent(new UserComponent(userId, wheels2));
                db.componentDao().insertUserComponent(new UserComponent(userId, wheels3));
                db.componentDao().insertUserComponent(new UserComponent(userId, weapon));
                db.componentDao().insertUserComponent(new UserComponent(userId, weapon2));
                db.componentDao().insertUserComponent(new UserComponent(userId, weapon3));
                db.componentDao().insertUserComponent(new UserComponent(userId, weapon4));
                db.componentDao().insertUserComponent(new UserComponent(userId, weapon5));

                user = new User("marko");
                user.setChassisId(chassis);
                user.setWheelsId(wheels);
                user.setWeaponId(weapon);
                userId =  db.userDao().insertUser(user);

                db.componentDao().insertUserComponent(new UserComponent(userId, chassis));
                db.componentDao().insertUserComponent(new UserComponent(userId, wheels));
                db.componentDao().insertUserComponent(new UserComponent(userId, weapon));

                user = new User("pera");
                user.setChassisId(chassis);
                user.setWheelsId(wheels);
                user.setWeaponId(weapon);
                userId = db.userDao().insertUser(user);

                db.componentDao().insertUserComponent(new UserComponent(userId, chassis));
                db.componentDao().insertUserComponent(new UserComponent(userId, wheels));
                db.componentDao().insertUserComponent(new UserComponent(userId, weapon));
            }
        });
    }

}
