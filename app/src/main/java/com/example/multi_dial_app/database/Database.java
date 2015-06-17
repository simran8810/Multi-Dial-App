package com.example.multi_dial_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "telecaller";
    public static final int DATABASE_VERSION = 1;

    public static final String CALL_HISTORY_TABLE = "call_history";

    //CALL HISTORY INFO TABLE
    public static final String RECORD_ID = "_id";

    public static final String CALLER_NAME = "telecaller";
    public static final String COL_CAMPAIGN = "campaign";
    public static final String COL_NAME = "name";
    public static final String COL_NUMBER = "number";
    public static final String COL_LOCATION = "location";
    public static final String COL_SHORT_FEEDBACK = "short_feedback";
    public static final String COL_DETAIL_FEEDBACK = "detail_feedback";
    public static final String COL_CALL_DATETIME = "call_datetime";




    private static final String CREATE_CALL_HISTORY_DATABASE = "CREATE TABLE "
            + CALL_HISTORY_TABLE + " ("
            + RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CALLER_NAME + " TEXT, "
            + COL_CAMPAIGN + " TEXT, "
            + COL_NAME + " TEXT,"
            + COL_NUMBER + " TEXT, "
            + COL_LOCATION + " TEXT,"
            + COL_SHORT_FEEDBACK + " TEXT,"
            + COL_DETAIL_FEEDBACK + " TEXT,"
            + COL_CALL_DATETIME + " TEXT);";




    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //onCreate method
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_CALL_HISTORY_DATABASE);
    }

    //onUpgrade method
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_CALL_HISTORY_DATABASE);
        onCreate(db);
    }
}