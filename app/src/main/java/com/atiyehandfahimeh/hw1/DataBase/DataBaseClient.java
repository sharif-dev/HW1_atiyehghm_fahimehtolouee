package com.atiyehandfahimeh.hw1.DataBase;

import android.content.Context;

import androidx.room.Room;

public class DataBaseClient {

    private Context mCtx;
    private static DataBaseClient mInstance;

    //our app database object
    private AppDataBase appDatabase;

    private DataBaseClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //all_data is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDataBase.class, "all_data").build();
    }

    public static synchronized DataBaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DataBaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDataBase getAppDatabase() {
        return appDatabase;
    }

}
