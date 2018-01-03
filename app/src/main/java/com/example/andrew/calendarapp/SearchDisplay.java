package com.example.andrew.calendarapp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class SearchDisplay extends ListActivity {

    ArrayList list = new ArrayList();
    ArrayList eventList = new ArrayList();
    private AlertDialog mDialog;

    ArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.appointment_view_edit);
        Button edit = (Button) findViewById(R.id.select_edit_button);
        final EditText editEntry = (EditText)findViewById(R.id.user_edit_editText);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);


        //figure all this shit out

        Cursor cursor = MainActivity.getEvents();
        final StringBuilder builder = new StringBuilder();

        ArrayList<String> eventsPassedIn = getIntent().getStringArrayListExtra("SEARCH_RESULTS");
        ArrayList<String> times = new ArrayList<String>();
        final ArrayList<String> dates = new ArrayList<String>();

        while (cursor.moveToNext()) {
            String eventTitle = cursor.getString(1);
            if (eventsPassedIn.contains(eventTitle)) {
                String thisDate = cursor.getString(2);
                dates.add(thisDate);
                String eventTime = cursor.getString(3);
                times.add(eventTime);
                builder.append(eventTitle);
                builder.append(",");
            }
        }
        String[] events = null;

        //makes sure events were actually added
        if (builder.toString().compareTo("") != 0) {
            events = builder.toString().split(",");
            for (int i = 0; i < events.length; i++) {
                list.add("" + (i+1) + ". " + times.get(i) + ".\t" + events[i]);
                eventList.add(events[i]);
            }
        }

        OnClickListener editButton = new OnClickListener() {
            @Override
            public void onClick(View v) {
                //if they entered a number
                if (editEntry.getText().toString().compareTo("") != 0) {
                    final int apptToEdit = Integer.parseInt(editEntry.getText().toString());
                    MainActivity.apptToChange = apptToEdit;

                    Intent intent = new Intent(SearchDisplay.this, Edit.class);
                    intent.putExtra("INDEX", apptToEdit);
                    intent.putExtra("EVENTS", eventList);
                    intent.putExtra("DATE", dates.get(apptToEdit-1));
                    intent.putExtra("TITLE", eventList.get(apptToEdit-1).toString());
                    startActivity(intent);
                    finish();
                }

                adapter.notifyDataSetChanged();
            }
        };

        edit.setOnClickListener(editButton);

        setListAdapter(adapter);
    }
}