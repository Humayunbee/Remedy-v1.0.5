package com.medicine.remedy.config;

import android.content.Context;

import androidx.room.Room;

import static com.medicine.remedy.utils.AppConstants.dbName;

/**
 * Date 1/19/2021.
 * Created by Md Atik Faysal(Android Developer)
 */
public class DatabaseClient
{
    private static DatabaseClient mInstance;

    //our app database object
    private final AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        //creating the app database with Room database builder
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, dbName)
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
