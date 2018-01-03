package com.example.andrew.calendarapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

public class MainActivity extends Activity {
    private static EventsData calendar;
    private static String[] fields = {Constants._ID, Constants.TITLE,
            Constants.DATE, Constants.TIME, Constants.DETAILS};
    private static String ORDER_BY = Constants.TIME + " ASC";
    private static final int MONDAY = 2;
    SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
    Calendar dateInfo = new Calendar();
    public static int apptToChange = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get Views
        Button create = (Button)findViewById(R.id.create_button);
        Button view_edit = (Button)findViewById(R.id.view_edit_button);
        Button delete = (Button)findViewById(R.id.delete_button);
        Button move = (Button)findViewById(R.id.move_button);
        Button search = (Button)findViewById(R.id.search_button);
        //final TextView dateNotice = (TextView)findViewById(R.id.date_notice);
        final CalendarView dates = (CalendarView)findViewById(R.id.calendar);
        // initializing the date
        dateInfo.setDate(date.format(new Date(dates.getDate())));


        dates.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                StringBuilder builder = new StringBuilder();
                month += 1;
                builder.append(dayOfMonth + "/");

                // standardizes the way that dates are listed. Turns "3" into "03" for consistency
                if(month<10) {
                    builder.append("0" + month + "/");
                } else {
                    builder.append(month + "/");
                }
                builder.append(year + "");
                dateInfo.setDate(builder.toString());
            }
        });
        dates.setFirstDayOfWeek(MONDAY);
        calendar = new EventsData(this);

        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //String selectedDate = date.format(new Date(dates.getDate()));
                String selectedDate = dateInfo.getDate();
                System.out.println(selectedDate);
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                intent.putExtra("DATE", selectedDate);
                startActivity(intent);
                //setContentView(R.layout.appointment_create);


                //setContentView(R.layout.activity_main);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //String selectedDate = date.format(new Date(dates.getDate()));
                String selectedDate = dateInfo.getDate();
                Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
                System.out.println(selectedDate);

                intent.putExtra("DATE", selectedDate);
                startActivity(intent);
            }
        });

        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDate = dateInfo.getDate();
                Intent intent = new Intent(MainActivity.this, MoveActivity.class);
                intent.putExtra("DATE", selectedDate);
                startActivity(intent);
            }
        });

        view_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDate = dateInfo.getDate();
                Intent intent = new Intent(MainActivity.this, View_Edit_Activity.class);
                intent.putExtra("DATE", selectedDate);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivity(intent);
            }
        });

    }

    public static void addEvent(String title, String date, String time, String details) {
        /* Insert new record into the calendar */
        SQLiteDatabase db = calendar.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.TITLE, title);
        values.put(Constants.DATE, date);
        values.put(Constants.TIME, time);
        //System.out.println("TIME TO ADD IS: " + time + "\n\n");
        values.put(Constants.DETAILS, details);
        db.insertOrThrow(Constants.TABLE_NAME, null, values);
    }

    public static Cursor getEvents() {
        SQLiteDatabase db = calendar.getReadableDatabase();

        /* NOTE: query(Table name, columns, selection, selection args,
        * groupby, having, orderby )*/
        Cursor cursor = db.query(Constants.TABLE_NAME, fields, null,
                null, null, null, ORDER_BY);
        return cursor;
    }

    public static void showEvents(Cursor cursor) {


        StringBuilder builder = new StringBuilder("Appointments:\n");
        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            String date = cursor.getString(2);
            String time = cursor.getString(3);
            String outputTime = new StringBuilder("" + time).toString();

            String details = cursor.getString(4);
            builder.append(title + "\n" + "date: " + date + "\n" + "time: "
                + outputTime.substring(0,outputTime.length()/2) + ":" +
                    outputTime.substring(outputTime.length()/2,outputTime.length()) + "\n" +
                    "description: " + details + "\n");
        }
        cursor.close();

        System.out.print(builder.toString());
    }

    public static void deleteAllByDate(String date) {
        SQLiteDatabase db = calendar.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.DATE + " = " + "\"" + date + "\"", null);
        showEvents(getEvents());
    }

    public static void deleteSingleAppt(int index, ArrayList list, String date) {
        int trueIndex = index-1;
        String title = list.get(trueIndex).toString();
        System.out.println("\n\n WHERE " + Constants.DATE + " = "+"\"" + date + "\" AND " +
                Constants.TITLE + " = " + "\"" + title + "\"");
        SQLiteDatabase db = calendar.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.DATE + " = "+"\"" + date + "\" AND " +
                Constants.TITLE + " = " + "\"" + title + "\"", null);
        showEvents(getEvents());

    }

    public static boolean doesntExist(String title, String date) {
        Cursor cursor = getEvents();

        while (cursor.moveToNext()) {
            String currTitle = cursor.getString(1);
            String currDate = cursor.getString(2);
            if ((currDate.compareTo(date) == 0) && (currTitle.compareTo(title) == 0)) {
                return false;
            }
        }
        // this event is okay to make because it doesn't exist
        return true;
    }

    public static void moveEvent(String newDate, String oldDate, ArrayList eventList) {
        //use appttochangeas index, but -1 when looking at list
        Cursor cursor = getEvents();
        String title = eventList.get(apptToChange-1).toString();
        SQLiteDatabase db = calendar.getWritableDatabase();

        while (cursor.moveToNext()) {
            String currTitle = cursor.getString(1);
            String currDate = cursor.getString(2);
            String currTime = cursor.getString(3);
            String currDetails = cursor.getString(4);

            if ((currTitle.compareTo(title) == 0) && (currDate.compareTo(oldDate) == 0)) {
                deleteSingleAppt(apptToChange, eventList, currDate);

                ContentValues values = new ContentValues();
                values.put(Constants.TITLE, title);
                values.put(Constants.DATE, newDate);
                values.put(Constants.TIME, currTime);
                values.put(Constants.DETAILS, currDetails);
                db.insertOrThrow(Constants.TABLE_NAME, null, values);
                showEvents(getEvents());
            }
        }
    }

    public static ArrayList<String> search(String searchText) {
        ArrayList<String> events = new ArrayList<String>();
        Cursor cursor = getEvents();

        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            String details = cursor.getString(4);

            if (title.toLowerCase().contains(searchText.toLowerCase()) ||
                    details.toLowerCase().contains(searchText.toLowerCase())) {
                events.add(title);
            }
        }
        return events;
    }
}
