package com.example.hangoutfinance_app;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PersonalDetails_Activity extends AppCompatActivity {

    private EditText personalInfo_name, personalInfo_surname, personalInfo_age, personalInfo_email;
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        personalInfo_name = (EditText) findViewById(R.id.Personal_name);
        personalInfo_surname = (EditText) findViewById(R.id.personal_surname);
        personalInfo_age = (EditText) findViewById(R.id.personal_age);
        personalInfo_email = (EditText) findViewById(R.id.personal_email);

        mAuth = FirebaseAuth.getInstance();

        ImageView m = (ImageView) findViewById(R.id.members_image9);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m1();
            }
        });
        ImageView t = (ImageView) findViewById(R.id.details_image9);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1();
            }
        });
        ImageView e = (ImageView) findViewById(R.id.expenses_image9);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1();
            }
        });
        ImageView h = (ImageView) findViewById(R.id.home_image9);
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h1();
            }
        });

    }
    public void a3_ButtonClicked(View view) {
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

    public void logout1_buttonClicked(View view){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void confirm1_buttonClicked(View view){
        updateInfo();
    }

    private void updateInfo(){
        String j=personalInfo_age.getText().toString();
        String o=personalInfo_name.getText().toString();
        String h=personalInfo_surname.getText().toString();

        String a=  personalInfo_email.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();

            String key= mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getKey();
            User info = new User(o,j,h,a);
            Map<String,Object> map =info.toMap();

            HashMap<String,Object> updates =new HashMap<>();
            updates.put("MyApp Users/" + key, map);

            mDatabase.updateChildren(updates).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    user.updateEmail(a)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.w("UpdateInfo", "Success", task.getException());
                                    } else {
                                        Log.w("UpdateInfo", "failure");
                                    }
                                }
                            });

                }
            });
        }
        personalInfo_age.setText("");
        personalInfo_email.setText("");
        personalInfo_surname.setText("");
        personalInfo_name.setText("");

    }
}