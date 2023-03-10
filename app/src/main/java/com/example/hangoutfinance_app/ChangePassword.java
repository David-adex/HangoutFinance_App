package com.example.hangoutfinance_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//import java.util.Objects;

public class ChangePassword extends AppCompatActivity{
    private EditText main_password, password, reenter_password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();

        main_password=(EditText) findViewById(R.id.current_password);
        password = (EditText) findViewById(R.id.change_password);
        reenter_password=(EditText) findViewById(R.id.confirm_password);
        ImageView m=(ImageView) findViewById(R.id.members_image3);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m1();
            }
        });
        ImageView t=(ImageView) findViewById(R.id.details_image3);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1();
            }
        });
        ImageView e=(ImageView) findViewById(R.id.expenses_image3);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1();
            }
        });
        ImageView h=(ImageView) findViewById(R.id.home_image3);
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h1();
            }
        });
        ImageView a = (ImageView) findViewById(R.id.account_image3);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a1();
            }
        });

    }
    public void a1(){
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

    public void logout3_buttonClicked(View view){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void confirm_buttonClicked(View view){
        password_change();
    }

    private void password_change(){
        String x = main_password.getText().toString();
        String y=password.getText().toString();
        String z= reenter_password.getText().toString();

        if (!x.isEmpty() && !y.isEmpty() && !z.isEmpty()){
            if(y.equals(z)){

                FirebaseUser user = mAuth.getCurrentUser();
                String user_email =user.getEmail();

                if(user != null && user_email != null){
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user_email, x);

// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ChangePassword.this, "Successfully", Toast.LENGTH_LONG).show();
                                        user.updatePassword(y)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.w("ChangePassword", "Success", task.getException());
                                                            //Log.d(TAG, "Password updated.");
                                                        }
                                                        else{
                                                            Log.w("ChangePassword", "failure");
                                                            Toast.makeText(ChangePassword.this, "Password Change failed.", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
                main_password.setText("");
                password.setText("");
                reenter_password.setText("");
            }
            else{
                Toast.makeText(ChangePassword.this, "New Passwords don't match", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(ChangePassword.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
        }
    }
}