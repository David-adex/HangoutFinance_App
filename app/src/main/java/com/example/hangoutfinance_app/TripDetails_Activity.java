package com.example.hangoutfinance_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TripDetails_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ListView x,y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        mAuth = FirebaseAuth.getInstance();

        x= findViewById(R.id.listView4);
        y= findViewById(R.id.listView5);
        ImageView m = (ImageView) findViewById(R.id.members_image8);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m1();
            }
        });
        ImageView t = (ImageView) findViewById(R.id.details_image8);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1();
            }
        });
        ImageView e = (ImageView) findViewById(R.id.expenses_image8);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1();
            }
        });
        ImageView h = (ImageView) findViewById(R.id.home_image8);
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h1();
            }
        });

        String tripName = getIntent().getStringExtra("tripName");

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("MyApp Users").child(userId).child("Trips Info");

        Query query = databaseRef.orderByChild("TRIP NAME").equalTo(tripName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // If the specific information is found in the database
                if (dataSnapshot.exists()) {
                    List<String> expenseInfo1 = new ArrayList<>();
                    List<String> expenseInfo2 = new ArrayList<>();
                    List<String> expenseInfo3 = new ArrayList<>();
                    List<String> info4 = new ArrayList<>();
                    for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()) {
                        String key = tripSnapshot.getKey();
                        DatabaseReference expenseRef = databaseRef.child(key).child("Expense Info");
                        expenseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot expensesSnapshot : snapshot.getChildren()) {
                                    String ex_name = expensesSnapshot.child("MEMBER NAME").getValue(String.class);
                                    String e_type = expensesSnapshot.child("EXPENSE TYPE").getValue(String.class);
                                    String e_cash = expensesSnapshot.child("AMOUNT").getValue(String.class);
                                    if (ex_name != null && e_type != null && e_cash != null) {
                                        expenseInfo3.add(ex_name);
                                        expenseInfo1.add(e_type);
                                        expenseInfo2.add(e_cash);
                                        info4.add(tripName);
                                    } else {
                                        Log.w("Active_Trips", "failed");
                                    }
                                }

                                double totalAmount = 0;
                                for (int i = 0; i < expenseInfo2.size(); i++) {
                                    String number_cash = expenseInfo2.get(i);
                                    if (number_cash != null) {
                                        double amount = Double.parseDouble(number_cash);
                                        totalAmount += amount;
                                    }
                                }
                                double division = (totalAmount/expenseInfo2.size());
                                String divisionString = String.format("%.2f", division);
                                List<String> divided_amount= new ArrayList<>();
                                divided_amount.add("Divided Amount for each Member: "+divisionString);

                                ArrayAdapter<String> adapt = new ArrayAdapter<>(TripDetails_Activity.this, android.R.layout.simple_list_item_1, divided_amount);
                                y.setAdapter(adapt);

                                if (expenseInfo1.isEmpty() || expenseInfo2.isEmpty() || expenseInfo3.isEmpty()) {
                                    Toast.makeText(TripDetails_Activity.this, "There is no expense data for this trip", Toast.LENGTH_SHORT).show();
                                    Log.d("Home_Activity", "No Expense Information");
                                    return;
                                }
                                //the expense type and amount combined into one list
                                List<String> combinedTripData = new ArrayList<>();
                                for (int i = 0; i < expenseInfo3.size(); i++) {
                                    combinedTripData.add("Trip Name: "+info4.get(i)+"\nMember Name: "+expenseInfo3.get(i).toUpperCase()+"\nExpense Type: "+expenseInfo1.get(i).toUpperCase() + "\nAMOUNT: " + expenseInfo2.get(i) + " pounds\n");
                                }
                                // Display the combined expense data in a list view
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(TripDetails_Activity.this, android.R.layout.simple_list_item_1, combinedTripData);
                                x.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w(TAG, "onCancelled", error.toException());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

    }

    public void finishTrip_ButtonClicked(View view) {
        String tripName = getIntent().getStringExtra("tripName");

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("MyApp Users").child(userId).child("Trips Info");

        Query query = databaseRef.orderByChild("TRIP NAME").equalTo(tripName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get the unique key for the data node
                    for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
                        String key = tripSnapshot.getKey();

                        DatabaseReference newDataRef = databaseRef.child(key);
                        newDataRef.removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled", error.toException());
            }
        });

    }
    public void a4_ButtonClicked(View view) {
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, Account_Activity.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
    }
    public void h1(){
        String tripName_2 = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, Home_Activity.class);
        intent.putExtra("tripName", tripName_2);
        startActivity(intent);
        finish();
    }
    public void m1(){
        String tripName_4 = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, Members_Activity.class);
        intent.putExtra("tripName", tripName_4);
        startActivity(intent);
        finish();
    }
    public void e1(){
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, Expenses_Activity.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
    }

    public void t1(){
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, TripDetails_Activity.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
    }

    public void logout8_buttonClicked(View view){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}