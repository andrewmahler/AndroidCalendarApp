package com.example.andrew.calendarapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class DeleteActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.appointment_delete);
        final Calendar calendar = new Calendar();
        String date = getIntent().getStringExtra("DATE");
        calendar.setDate(date);

        Button deleteAll = (Button) findViewById(R.id.delete_all);

        Button pickAndChoose = (Button) findViewById(R.id.choose_delete);

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.deleteAllByDate(calendar.getDate());
                finish();
            }
        });

        pickAndChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteActivity.this, DeleteActivity2.class);
                intent.putExtra("DATE",getIntent().getStringExtra("DATE"));
                startActivity(intent);
                finish();
            }
        });
    }
}