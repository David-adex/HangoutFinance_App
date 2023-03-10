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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class modifyExpenses extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText mExpenses_name, mExpenses_amount, mExpenses_type;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_expenses);
        mAuth = FirebaseAuth.getInstance();

        mExpenses_type=(EditText) findViewById(R.id.modify_type);
        mExpenses_amount=(EditText) findViewById(R.id.modify_amount);
        mExpenses_name=(EditText) findViewById(R.id.modify_member);

        ImageView m = (ImageView) findViewById(R.id.members_image7);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m1();
            }
        });
        ImageView t = (ImageView) findViewById(R.id.details_image7);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1();
            }
        });
        ImageView e = (ImageView) findViewById(R.id.expenses_image7);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1();
            }
        });
        ImageView h = (ImageView) findViewById(R.id.home_image7);
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h1();
            }
        });

    }
    public void a9_ButtonClicked(View view) {
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
    public void logout7_buttonClicked(View view){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void ExpensesConfirm_ButtonClicked(View view) {updateExpenses();}

    public void updateExpenses(){

        String e_type= mExpenses_type.getText().toString();
        String e_amount=mExpenses_amount.getText().toString();
        String e_name=mExpenses_name.getText().toString();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference("MyApp Users").child(userId).child("Trips Info");

            Query query = mDatabase.orderByChild("Member name").equalTo(e_name);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Get the unique key for the data node
                        String key = snapshot.getChildren().iterator().next().getKey();

                        DatabaseReference newDataRef=mDatabase.child(key).child("Expense Info");
                        newDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        String k= snapshot.getChildren().iterator().next().getKey();
                                        modify Modify= new modify(e_name,e_type,e_amount);
                                        Map<String,Object> map =Modify.toMap();
                                        HashMap<String,Object> updates =new HashMap<>();
                                        updates.put("/"+k, map);
                                        newDataRef.updateChildren(updates);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.w(TAG, "onCancelled", error.toException());
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "onCancelled", error.toException());
                }
            });
        }
        mExpenses_name.setText("");
        mExpenses_type.setText("");
        mExpenses_amount.setText("");
    }

}