package com.example.andrew.calendarapp;

import static android.provider.BaseColumns._ID;
import static com.example.andrew.calendarapp.Constants.TABLE_NAME;
import static com.example.andrew.calendarapp.Constants.TITLE;
import static com.example.andrew.calendarapp.Constants.TIME;
import static com.example.andrew.calendarapp.Constants.DETAILS;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class Calendar {
    private String date = "00/00/0000";

    public void setDate(String newDate) {
        date = newDate;
    }
    public String getDate() {
        return date;
    }

}
