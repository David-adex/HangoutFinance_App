package com.example.hangoutfinance_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Account_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mAuth = FirebaseAuth.getInstance();
        ImageView m = (ImageView) findViewById(R.id.members_image1);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m1();
            }
        });
        ImageView t = (ImageView) findViewById(R.id.details_image1);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1();
            }
        });
        ImageView e = (ImageView) findViewById(R.id.expenses_image1);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1();
            }
        });
        ImageView h = (ImageView) findViewById(R.id.home_image1);
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h1();
            }
        });
    }

    public void a_ButtonClicked(View view) {
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, Account_Activity.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
    }

    public void h1() {
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, Home_Activity.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
    }

    public void m1() {
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, Members_Activity.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
    }

    public void e1() {
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, Expenses_Activity.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
    }

    public void t1() {
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, TripDetails_Activity.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
    }

    public void password_buttonClicked(View view) {
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, ChangePassword.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
    }

    public void userInfo_buttonClicked(View view) {
        String tripName = getIntent().getStringExtra("tripName");
        Intent intent = new Intent(this, PersonalDetails_Activity.class);
        intent.putExtra("tripName", tripName);
        startActivity(intent);
        finish();
    }

    public void logout2_buttonClicked(View view){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
