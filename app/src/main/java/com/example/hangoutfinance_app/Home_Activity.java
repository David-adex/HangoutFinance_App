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
import java.util.List;

public class Home_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.listView2);

        ImageView m=(ImageView) findViewById(R.id.members_image);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m1();
            }
        });
        ImageView t=(ImageView) findViewById(R.id.details_image);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1();
            }
        });
        ImageView h=(ImageView) findViewById(R.id.home_image);
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
                    for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()) {
                        String key = tripSnapshot.getKey();
                        DatabaseReference expenseRef = databaseRef.child(key).child("Expense Info");
                        expenseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot expensesSnapshot : snapshot.getChildren()) {
                                    String e_type = expensesSnapshot.child("EXPENSE TYPE").getValue(String.class);
                                    String e_cash = expensesSnapshot.child("AMOUNT").getValue(String.class);
                                    if (e_type != null && e_cash != null) {
                                        expenseInfo1.add(e_type);
                                        expenseInfo2.add(e_cash);
                                    } else {
                                        Log.w("Active_Trips", "failed");
                                    }
                                }
                            if (expenseInfo1.isEmpty() || expenseInfo2.isEmpty()) {
                                Toast.makeText(Home_Activity.this, "There is no expense data for this trip", Toast.LENGTH_SHORT).show();
                                Log.d("Home_Activity", "No Expense Information");
                                return;
                            }
                            //the expense type and amount combined into one list
                                List<String> combinedTripData = new ArrayList<>();
                                for (int i = 0; i < expenseInfo1.size(); i++) {
                                    combinedTripData.add(expenseInfo1.get(i).toUpperCase() + "  -  " + expenseInfo2.get(i) + " pounds");
                                }
                                // Display the combined expense data in a list view
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(Home_Activity.this, android.R.layout.simple_list_item_1, combinedTripData);
                                listView.setAdapter(adapter);
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

    public void h1(){
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, Home_Activity.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
    }
    public void m1(){
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, Members_Activity.class);
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
    public void account_buttonClicked(View view){
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, Account_Activity.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
    }
    public void expenses_buttonClicked(View view){
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, Expenses_Activity.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
    }
    public void logout5_buttonClicked(View view){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}