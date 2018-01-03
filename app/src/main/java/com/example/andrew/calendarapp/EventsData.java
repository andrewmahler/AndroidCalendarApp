package com.example.andrew.calendarapp;

import static android.provider.BaseColumns._ID;
import static com.example.andrew.calendarapp.Constants.DATE;
import static com.example.andrew.calendarapp.Constants.TABLE_NAME;
import static com.example.andrew.calendarapp.Constants.TITLE;
import static com.example.andrew.calendarapp.Constants.TIME;
import static com.example.andrew.calendarapp.Constants.DETAILS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class EventsData extends SQLiteOpenHelper{
    private static final String DATABASE_NAME =
            "calendar.db";
    private static final int DATABASE_VERSION = 8;
    // set to a different number to keep testing

    /* Creates a helper object for the Calendar Database */
    public EventsData(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
            _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DATE + " VARCHAR(100),"
            + TIME + " INTEGER,"   // TODO change to varchar
            + TITLE + " TEXT NOT NULL,"
            + DETAILS + " VARCHAR(100));");  //TODO is this syntax correct?
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
