package com.example.andrew.calendarapp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class DeleteActivity2 extends ListActivity {

    ArrayList list = new ArrayList();
    ArrayList eventList = new ArrayList();
    private AlertDialog mDialog;

    ArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.appointment_delete_scroller);
        Button delete = (Button) findViewById(R.id.delete_button);
        final EditText deleteEntry = (EditText)findViewById(R.id.user_delete);

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
                // TODO could cause issues splitting by "," when event titles have commas in names
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

        OnClickListener deleteListener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                //if they entered a number
                if (deleteEntry.getText().toString().compareTo("") != 0) {
                    final int apptToDelete = Integer.parseInt(deleteEntry.getText().toString());
                    MainActivity.apptToChange = apptToDelete;
                    AlertDialog.Builder confirmation =
                            new AlertDialog.Builder(DeleteActivity2.this);
                    confirmation.setMessage("Would you like to delete event: \"" +
                            eventList.get(apptToDelete - 1) + "\"?");
                    confirmation.setCancelable(false);
                    confirmation.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface,
                                            int i) {
                            if (MainActivity.apptToChange != -1) {
                                MainActivity.deleteSingleAppt(MainActivity.apptToChange,
                                        eventList, getIntent().getStringExtra("DATE"));
                                eventList.remove(apptToDelete - 1);
                                list.remove(apptToDelete - 1);
                                MainActivity.apptToChange = -1;
                                // clearing what user entered in
                                deleteEntry.setText("");
                                adapter.notifyDataSetChanged();
                                finish();
                            }
                        }
                    });
                    confirmation.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface,
                                            int i) {
                            MainActivity.apptToChange = -1;
                        }
                        });
                    mDialog = confirmation.show();
                }

                adapter.notifyDataSetChanged();
            }
        };

        //Setting the event listener
        delete.setOnClickListener(deleteListener);

        setListAdapter(adapter);
    }
}