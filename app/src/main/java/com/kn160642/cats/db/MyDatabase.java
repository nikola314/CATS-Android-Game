package com.kn160642.cats.db;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
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

import java.util.concurrent.Executors;


@Database(entities = {User.class, Component.class, UserComponent.class}, version = 7, exportSchema = false)
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
                                prepopulateDatabase(context);
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

                Log.i("USER", "2");
                Component c = new Component("chassis0",80, 70, 70, TypesHelper.ComponentType.CHASSIS);
                long chassis = db.componentDao().insertComponent(c);
                c = new Component("chassis1",50, 100, 100, TypesHelper.ComponentType.CHASSIS);
                db.componentDao().insertComponent(c);
                c = new Component("wheel0",20, 70, 70, TypesHelper.ComponentType.WHEELS);
                long wheels = db.componentDao().insertComponent(c);
                c = new Component("wheel1",20, 70, 70, TypesHelper.ComponentType.WHEELS);
                db.componentDao().insertComponent(c);
                c = new Component("wheel2",20, 70, 70, TypesHelper.ComponentType.WHEELS);
                db.componentDao().insertComponent(c);
                c = new Component("blade",20, 70, 70, TypesHelper.ComponentType.WEAPON);
                long weapon = db.componentDao().insertComponent(c);
                c = new Component("chainsaw",20, 70, 70, TypesHelper.ComponentType.WEAPON);
                db.componentDao().insertComponent(c);
                c = new Component("drill",20, 70, 70, TypesHelper.ComponentType.WEAPON);
                db.componentDao().insertComponent(c);
                c = new Component("forklift",20, 70, 70, TypesHelper.ComponentType.WEAPON);
                db.componentDao().insertComponent(c);
                c = new Component("rocket",20, 70, 70, TypesHelper.ComponentType.WEAPON);
                db.componentDao().insertComponent(c);

                User user = new User("nikola");
                user.setChassisId(chassis);
                user.setWheelsId(wheels);
                user.setWeaponId(weapon);
                long userId = db.userDao().insertUser(user);

                db.componentDao().insertUserComponent(new UserComponent(userId, chassis));
                db.componentDao().insertUserComponent(new UserComponent(userId, wheels));
                db.componentDao().insertUserComponent(new UserComponent(userId, weapon));

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
