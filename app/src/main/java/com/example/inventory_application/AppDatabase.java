package com.example.inventory_application;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Item.class}, version = 2, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ItemDao itemDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase get(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "inventory_db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }

        return INSTANCE;
    }
}
