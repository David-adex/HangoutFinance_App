package com.example.hangoutfinance_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class activeTrips_Activity extends AppCompatActivity {
    private TextView back;
    private ListView listview;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_trips);
        mAuth = FirebaseAuth.getInstance();

        listview = findViewById(R.id.listView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the trip name associated with the clicked item
                String tripName = (String) adapterView.getItemAtPosition(i);

                // Create an intent to move to the next activity
                Intent intent = new Intent(activeTrips_Activity.this, Home_Activity.class);

                // Pass the trip name as an extra to the intent
                intent.putExtra("tripName", tripName);

                // Start the next activity
                startActivity(intent);
                finish();
            }
        });

        back = (TextView) findViewById(R.id.Back1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activeTrips_Activity.this, EntryPage_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void refresh_ButtonClicked(View view){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("MyApp Users").child(userId).child("Trips Info");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Set<String> TripNames = new HashSet<>();
                            for (DataSnapshot TripsSnapshot : snapshot.getChildren()) {
                                String TripName = TripsSnapshot.child("TRIP NAME").getValue(String.class);
                                if (TripName != null) {
                                    TripNames.add(TripName);
                                }
                                 else {
                                    Log.w("Active_Trips", "failed");
                                }
                            }
                            //Collections.sort(TripNames);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(activeTrips_Activity.this, android.R.layout.simple_list_item_1, new ArrayList<>(TripNames));
                            listview.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "Error retrieving Trip names: " + error.getMessage());
                        }
                    });
    }
}