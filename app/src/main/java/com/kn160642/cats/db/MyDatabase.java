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

import com.kn160642.cats.db.Entities.Box;
import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.db.Entities.History;
import com.kn160642.cats.db.Entities.User;
import com.kn160642.cats.db.Entities.UserComponent;
import com.kn160642.cats.db.dao.BoxDao;
import com.kn160642.cats.db.dao.ComponentDao;
import com.kn160642.cats.db.dao.HistoryDao;
import com.kn160642.cats.db.dao.UserDao;
import com.kn160642.cats.helpers.TypesHelper;

import java.util.List;
import java.util.concurrent.Executors;


@Database(entities = {User.class, Component.class, UserComponent.class, Box.class, History.class}, version = 11, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ComponentDao componentDao();
    public abstract BoxDao boxDao();
    public abstract HistoryDao historyDao();

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
                                testing(context);
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

                Component c = new Component("chassis0",100, 70, 70, TypesHelper.ComponentType.CHASSIS);
                long chassis = db.componentDao().insertComponent(c);
                c = new Component("chassis1",50, 100, 100, TypesHelper.ComponentType.CHASSIS);
                long chassis2 = db.componentDao().insertComponent(c);
                c = new Component("wheel0",30, 60, 50, TypesHelper.ComponentType.WHEELS);
                long wheels = db.componentDao().insertComponent(c);
                c = new Component("wheel1",70, 40, 60, TypesHelper.ComponentType.WHEELS);
                long wheels2 =db.componentDao().insertComponent(c);
                c = new Component("wheel2",50, 90, 50, TypesHelper.ComponentType.WHEELS);
                long wheels3 = db.componentDao().insertComponent(c);
                c = new Component("blade",20, 70, 50, TypesHelper.ComponentType.WEAPON);
                long weapon = db.componentDao().insertComponent(c);
                c = new Component("chainsaw",70, 90, 90, TypesHelper.ComponentType.WEAPON);
                long weapon2 = db.componentDao().insertComponent(c);
                c = new Component("drill",30, 60, 60, TypesHelper.ComponentType.WEAPON);
                long weapon3 = db.componentDao().insertComponent(c);
                c = new Component("forklift",100, 80, 40, TypesHelper.ComponentType.WEAPON);
                long weapon4 =db.componentDao().insertComponent(c);
                c = new Component("rocket",90, 50, 70, TypesHelper.ComponentType.WEAPON);
                long weapon5 = db.componentDao().insertComponent(c);

                User user = new User("nikola");
                user.setChassisId(chassis);
                user.setWheelsId(wheels);
                user.setWeaponId(weapon);
                long userId = db.userDao().insertUser(user);

//                Box b = new Box();
//                b.setTimestamp(System.currentTimeMillis());
//                b.setTimeToOpen(1000*60*5);
//                b.setUserId(userId);
//                b.setOpened(false);
//                db.boxDao().insertBox(b);
//
//                b = new Box();
//                b.setTimestamp(System.currentTimeMillis());
//                b.setTimeToOpen(1000*60*10);
//                b.setUserId(userId);
//                b.setOpened(false);
//                db.boxDao().insertBox(b);

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

                List<User> users = db.userDao().getAllUsersDead();
                for(User u: users){
                    for(int i=0;i<4;i++) {
                        Box b = new Box();
                        b.setTimestamp(System.currentTimeMillis());
                        b.setTimeToOpen(1000*60*(i+5));
                        b.setUserId(u.getUserId());
                        b.setOpened(false);
                        db.boxDao().insertBox(b);
                    }
                }
            }
        });
    }

    private static void testing(final Context context){
//        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                MyDatabase db = getInstance(context);
//
//            }
//        });
    }

}
