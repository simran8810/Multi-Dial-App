package com.example.multi_dial_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



public class SqlController {


    private Database dbhelper;
    private Context context;
    private SQLiteDatabase database;

    public SqlController(Context c) {
        context = c;
    }

    public SqlController open() throws SQLException {
        dbhelper = new Database(context);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbhelper.close();
    }


    //SQLITE READ AND WRITE TRANSACTIONS

    //WRITE : basic profile data
    public void insertCallData(String callerName, String campaign, String personName, String personNumber,
                           String location, String shortFeedback, String detailFeedback, String callDateTime) {
        ContentValues cv = new ContentValues();

        cv.put(Database.CALLER_NAME, callerName);
        cv.put(Database.COL_CAMPAIGN, campaign);
        cv.put(Database.COL_NAME, personName);
        cv.put(Database.COL_NUMBER, personNumber);
        cv.put(Database.COL_LOCATION, location);
        cv.put(Database.COL_SHORT_FEEDBACK, shortFeedback);
        cv.put(Database.COL_DETAIL_FEEDBACK, detailFeedback);
        cv.put(Database.COL_CALL_DATETIME, callDateTime);


        Log.e("testing insertdata", callerName+","+campaign+","+personName+","+personNumber+","+location+","+
                shortFeedback+","+detailFeedback+","+callDateTime+",");

        database.insert(Database.CALL_HISTORY_TABLE, null, cv);

    }




    //READ : user basic profile data
    public Cursor getCallHistory(String caller){
        Cursor cursor = database.query(true, Database.CALL_HISTORY_TABLE, new String[]
                        { Database.RECORD_ID, Database.CALLER_NAME, Database.COL_CAMPAIGN, Database.COL_NAME, Database.COL_NUMBER,
                                Database.COL_LOCATION, Database.COL_SHORT_FEEDBACK, Database.COL_DETAIL_FEEDBACK,
                                Database.COL_CALL_DATETIME,} ,
                Database.CALLER_NAME +"='"+caller+"'", null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            return cursor;
        }
        else
            return cursor;

    }

}