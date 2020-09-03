package com.kn160642.cats.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kn160642.cats.db.Entities.User;
import com.kn160642.cats.db.dao.UserDao;


@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static MyDatabase instance = null;

    public static MyDatabase getInstance(Context context){
        if(instance == null){
            synchronized (MyDatabase.class){
                instance = Room
                        .databaseBuilder(context.getApplicationContext(), MyDatabase.class, "my-database")
                        .fallbackToDestructiveMigration()
                        .addCallback(rdc)
                        .build();
            }
        }
        return instance;
    }

    static RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
        public void onCreate (SupportSQLiteDatabase db) {
            // TODO: prepopulate database
        }
        public void onOpen (SupportSQLiteDatabase db) {
            // do something every time database is open
        }
    };
}
