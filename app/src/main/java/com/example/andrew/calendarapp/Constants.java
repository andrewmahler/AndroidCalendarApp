package com.example.andrew.calendarapp;
import android.provider.BaseColumns;



public interface Constants extends BaseColumns{
    public static final String TABLE_NAME = "calendarEvents";
    // Columns in the events database
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String DETAILS = "details";
}
