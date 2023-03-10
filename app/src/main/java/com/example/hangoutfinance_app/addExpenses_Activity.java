package com.example.hangoutfinance_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class addExpenses_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText Expenses_name, Expenses_type, Expenses_Amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        mAuth = FirebaseAuth.getInstance();

        ImageView m = (ImageView) findViewById(R.id.members_image2);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m1();
            }
        });
        ImageView t = (ImageView) findViewById(R.id.details_image2);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1();
            }
        });
        ImageView e = (ImageView) findViewById(R.id.expenses_image2);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1();
            }
        });
        ImageView h = (ImageView) findViewById(R.id.home_image2);
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h1();
            }
        });

    }
    public void a5_ButtonClicked(View view) {
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, Account_Activity.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
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

    public void logout_buttonClicked(View view){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void submit2_buttonClicked(View view){
        save_details2();
    }

    public void save_details2() {
        Expenses_name=(EditText) findViewById(R.id.add_member);
        Expenses_Amount=(EditText) findViewById(R.id.add_amount);
        Expenses_type=(EditText) findViewById(R.id.add_type);
        String a_name = Expenses_name.getText().toString().trim();
        String type_name = Expenses_type.getText().toString().trim();
        String cash = Expenses_Amount.getText().toString().trim();

        HashMap<String,Object> dataMap= new HashMap<>();
        dataMap.put("MEMBER NAME", a_name);
        dataMap.put("EXPENSE TYPE", type_name);
        dataMap.put("AMOUNT", cash);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("MyApp Users").child(userId).child("Trips Info");

        Query query = databaseRef.orderByChild("Member name").equalTo(a_name);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // If the specific information is found in the database
                if (dataSnapshot.exists()) {
                    // Get the unique key for the data node
                    String key = dataSnapshot.getChildren().iterator().next().getKey();

                    // Store the new information under the same unique key
                    DatabaseReference newDataRef = databaseRef.child(key).child("Expense Info");
                    newDataRef.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("addExpenses_Activity", "AddExpenses:success");
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
            Expenses_Amount.setText("");
            Expenses_type.setText("");
            Expenses_name.setText("");
    }
}