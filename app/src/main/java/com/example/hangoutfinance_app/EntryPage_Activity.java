package com.example.hangoutfinance_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EntryPage_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_page);
    }

    public void newTrip_buttonClicked(View view){
        Intent intent = new Intent(this, NewTrips_Activity.class);
        startActivity(intent);
        finish();
    }
    public void activeTrip_buttonClicked(View view){
        Intent intent = new Intent(this, activeTrips_Activity.class);
        startActivity(intent);
        finish();
    }
}