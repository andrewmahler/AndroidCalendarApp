package com.example.andrew.calendarapp;

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

public class View_Edit_Activity extends ListActivity {

    ArrayList list = new ArrayList();
    ArrayList eventList = new ArrayList();

    ArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.appointment_view_edit);
        Button edit = (Button) findViewById(R.id.select_edit_button);
        final EditText editEntry = (EditText)findViewById(R.id.user_edit_editText);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        // populating the array with the events for that day
        String date = getIntent().getStringExtra("DATE");
        // assertion, will exit if true
        assert(date.compareTo("") != 0);
        Cursor cursor = MainActivity.getEvents();
        final StringBuilder builder = new StringBuilder();

        ArrayList<String> times = new ArrayList<String>();

        while (cursor.moveToNext()) {
            String thisDate = cursor.getString(2);
            String eventTitle = cursor.getString(1);
            String eventTime = cursor.getString(3);
            if (thisDate.compareTo(date) == 0) {
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

                    Intent intent = new Intent(View_Edit_Activity.this, Edit.class);
                    intent.putExtra("INDEX", apptToEdit);
                    intent.putExtra("EVENTS", eventList);
                    intent.putExtra("DATE", getIntent().getStringExtra("DATE"));
                    intent.putExtra("TITLE", eventList.get(apptToEdit-1).toString());
                    editEntry.setText("");
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