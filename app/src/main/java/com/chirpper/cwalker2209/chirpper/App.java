package com.chirpper.cwalker2209.chirpper;

/**
 * Created by CommandModule on 2017-11-26.
 */

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.chirpper.cwalker2209.chirpper.database.AppDatabase;
import com.chirpper.cwalker2209.chirpper.database.User;

public class App extends Application {


    public static App INSTANCE;
    private static final String DATABASE_NAME = "MyDatabase";
    private static final String PREFERENCES = "RoomDemo.preferences";
    private static final String KEY_FORCE_UPDATE = "force_update";

    private AppDatabase database;
    private long userId;

    public static App get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // create database
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                //.addMigrations(AppDatabase.MIGRATION_1_2)
                .build();

        INSTANCE = this;
    }

    public AppDatabase getDB() {
        return database;
    }

    public long getUserId() { return userId;}
    public void setUserId(long userId) {this.userId = userId;}

    public boolean isForceUpdate() {
        return getSP().getBoolean(KEY_FORCE_UPDATE, true);
    }

    public void setForceUpdate(boolean force) {
        SharedPreferences.Editor edit = getSP().edit();
        edit.putBoolean(KEY_FORCE_UPDATE, force);
        edit.apply();
    }

    private SharedPreferences getSP() {
        return getSharedPreferences(PREFERENCES, MODE_PRIVATE);
    }
}
