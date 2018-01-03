package com.example.andrew.calendarapp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class MoveActivity extends ListActivity {

    ArrayList list = new ArrayList();
    ArrayList eventList = new ArrayList();

    ArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.appointment_move);
        Button move = (Button) findViewById(R.id.select_move_button);
        final EditText moveEntry = (EditText)findViewById(R.id.user_move);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        // populating the array with events for that day
        String date = getIntent().getStringExtra("DATE");
        // assertion, will exit if true
        assert(date.compareTo("") != 0);
        Cursor cursor = MainActivity.getEvents();
        final StringBuilder builder = new StringBuilder();

        while (cursor.moveToNext()) {
            String thisDate = cursor.getString(2);
            String eventTitle = cursor.getString(1);
            if (thisDate.compareTo(date) == 0) {
                builder.append(eventTitle);
                builder.append(",");
            }
        }
        String[] events = null;

        //makes sure events were actually added
        if (builder.toString().compareTo("") != 0) {
            events = builder.toString().split(",");
            for (int i = 0; i < events.length; i++) {
                list.add("" + (i+1) + ".\t" + events[i]);
                eventList.add(events[i]);
            }
        }

        // move listener
        OnClickListener moveListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                //if they entered a number
                if (moveEntry.getText().toString().compareTo("") != 0) {
                    final int apptToChange = Integer.parseInt(moveEntry.getText().toString());
                    MainActivity.apptToChange = apptToChange;

                    // going to new activity to allow to pick
                    Intent intent = new Intent(MoveActivity.this, MoveActivity2.class);
                    intent.putExtra("OLD_DATE", getIntent().getStringExtra("DATE"));
                    intent.putExtra("EVENTS",eventList);
                    //eventList.remove(apptToChange-1);
                    list.remove(apptToChange - 1);
                    adapter.notifyDataSetChanged();
                    moveEntry.setText("");
                    startActivity(intent);
                    finish();
                }
            }
        };

        move.setOnClickListener(moveListener);

        setListAdapter(adapter);
    }
}