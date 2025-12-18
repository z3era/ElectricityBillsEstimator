package com.example.electricitybillsestimator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "electricity_bill.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE bills (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "month TEXT, " +
                        "unit REAL, " +
                        "total REAL, " +
                        "rebate REAL, " +
                        "final REAL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS bills");
        onCreate(db);
    }
}

