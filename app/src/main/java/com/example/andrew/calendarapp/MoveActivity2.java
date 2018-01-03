package com.example.andrew.calendarapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MoveActivity2 extends Activity {
    private static final int MONDAY = 2;
    SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
    Calendar dateInfo = new Calendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_calendar);

        Button cancel = (Button)findViewById(R.id.move_cancel_button);
        Button okay = (Button)findViewById(R.id.move_okay_button);
        final CalendarView dates = (CalendarView)findViewById(R.id.move_calendar);
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MoveActivity.newDate = dateInfo.getDate();
                MainActivity.moveEvent(dateInfo.getDate(), getIntent().getStringExtra("OLD_DATE"),getIntent().getStringArrayListExtra("EVENTS"));
                //MoveActivity.flag = true;
                finish();
            }
        });
    }
}
