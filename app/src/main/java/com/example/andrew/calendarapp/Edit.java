package com.example.andrew.calendarapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.andrew.calendarapp.MainActivity.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;


public class Edit extends Activity {

    AlertDialog mDialog;
    private ExecutorService synThread;
    private Handler guiThread;
    private Runnable synTask;
    private static final String BEGINNINGURL = "http://thesaurus.altervista.org/thesaurus/v1?word=";
    private static final String ENDURL = "&language=en_US&key=shRj5lE66dXVidcnEfS3&output=xml";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_create);


        TextView dateNotice = (TextView)findViewById(R.id.date_notice);
        dateNotice.setText("Event on " + getIntent().getStringExtra("DATE"));


        // get buttons
        Button save = (Button)findViewById(R.id.save_appt_button);
        Button cancel_button = (Button)findViewById(R.id.cancel_button);
        final EditText title = (EditText)findViewById(R.id.title_box);
        final TimePicker time = (TimePicker)findViewById(R.id.event_time);
        final EditText synonymWord = (EditText)findViewById(R.id.synonymBox);
        Button synonymButton = (Button)findViewById(R.id.synonymButton);
        // makes 24 hour view so times can more easily be compared
        time.setIs24HourView(true);

        // setting up the deatils textbox, allowing for a scrollbar after 3 lines have been entered
        final EditText details = (EditText)findViewById(R.id.event_details);
        details.setScroller(new Scroller(this));
        details.setMaxLines(3);
        details.setVerticalScrollBarEnabled(true);
        details.setMovementMethod(new ScrollingMovementMethod());

        // putting in info that was already there
        Cursor cursor = MainActivity.getEvents();
        while (cursor.moveToNext()) {
            if ((getIntent().getStringExtra("DATE").compareTo(cursor.getString(2)) == 0) &&
                    (getIntent().getStringExtra("TITLE").compareTo(cursor.getString(1)) == 0)) {
                title.setText(cursor.getString(1));
                String[] timeComponents = cursor.getString(3).split(":");
                if (timeComponents.length == 2) {
                    time.setHour(Integer.parseInt(timeComponents[0]));
                    time.setMinute(Integer.parseInt(timeComponents[1]));

                }
                details.setText(cursor.getString(4));
                //TODO
                /*MainActivity.deleteSingleAppt(getIntent().getIntExtra("INDEX",1), getIntent()
                        .getStringArrayListExtra("EVENTS"), getIntent().getStringExtra("DATE"));*/
                break;
            }
        }


        save.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                MainActivity.deleteSingleAppt(getIntent().getIntExtra("INDEX",1), getIntent()
                        .getStringArrayListExtra("EVENTS"), getIntent().getStringExtra("DATE"));
                //StringBuilder selectedTime = /* "" + time.getHour() + "" + time.getMinute();*/
                StringBuilder selectedTime = new StringBuilder("");
                if (time.getHour() < 10) {
                    selectedTime.append("0" + time.getHour());
                } else {
                    selectedTime.append(time.getHour());
                }
                selectedTime.append(":");
                if (time.getMinute() < 10) {
                    selectedTime.append("0" + time.getMinute());
                } else {
                    selectedTime.append(time.getMinute());
                }
                //int trueTime = Integer.parseInt(selectedTime);
                if (MainActivity.doesntExist(title.getText().toString(), getIntent().getStringExtra("DATE"))) {
                    MainActivity.addEvent(title.getText().toString(), getIntent().getStringExtra("DATE"), selectedTime.toString(),
                            details.getText().toString());
                    Cursor cursor = MainActivity.getEvents();
                    MainActivity.showEvents(cursor);
                    // should exit back to calendar screen
                    finish();

                } else {
                    AlertDialog.Builder titleError =
                            new AlertDialog.Builder(Edit.this);
                    titleError.setMessage("Appointment " + title.getText().toString() +
                            " already exists, please choose a different event title");
                    titleError.setCancelable(false);
                    titleError.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface,
                                                    int i) {
                                    //nothing
                                }
                            });
                    mDialog = titleError.show();
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        // Threading
        synThread = Executors.newSingleThreadExecutor();
        guiThread = new Handler();
        synTask = new Runnable() {
            public void run() {
                try {
                    if (synonymWord.getText().toString().compareTo("") != 0) {
                        StringBuilder webAddress = new StringBuilder(BEGINNINGURL);
                        webAddress.append(synonymWord.getText());
                        webAddress.append(ENDURL);

                        SynonymTaskForEdit task = new SynonymTaskForEdit(Edit.this,
                                webAddress.toString());
                        synThread.submit(task);
                    }
                } catch (RejectedExecutionException e) {
                    // idk?
                }
            }
        };


        synonymButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if there is a word in the textbox
                if (synonymWord.getText().toString().compareTo("") != 0) {
                    synTask.run();
                }
            }
        });

    }

    public void setSynonyms(ArrayList<String> synonymsList) {
        // display the synonyms
        final ArrayList<String> synonyms = synonymsList;
        guiThread.post(new Runnable() {
            public void run() {
                StringBuilder list = new StringBuilder("Synonyms: ");
                for (int i = 0; i < synonyms.size(); i++) {
                    list.append(synonyms.get(i) + ", ");
                }
                AlertDialog.Builder titleError =
                        new AlertDialog.Builder(Edit.this);
                titleError.setMessage(list.toString());
                titleError.setCancelable(false);
                titleError.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface,
                                                int i) {
                                //nothing
                            }
                        });
                mDialog = titleError.show();
            }
        });

    }

}
