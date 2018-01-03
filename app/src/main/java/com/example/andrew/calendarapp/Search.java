package com.example.andrew.calendarapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class Search  extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_fields);

        Button search = (Button)findViewById(R.id.search_enter_button);
        final EditText searchText = (EditText)findViewById(R.id.search_text);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchText.getText().toString().compareTo("") != 0) {
                    ArrayList<String> events = MainActivity.search(searchText.getText().toString());
                    if (events.size() != 0) {
                        Intent intent = new Intent(Search.this, SearchDisplay.class);
                        intent.putExtra("SEARCH_RESULTS", events);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
}
