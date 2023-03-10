package com.example.hangoutfinance_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;

    private TextView move;
    private EditText email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        move= findViewById(R.id.textView2);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();

            }
        });

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

    }

    public void doRegister(){
            Intent intent = new Intent(this, Register_Activity.class);
            startActivity(intent);
            finish();
    }

    public void login_buttonClicked(View view){
        login_user();
    }

    private void login_user() {
        String l_email = email.getText().toString().trim();
        String l_password = password.getText().toString().trim();
        if (l_email.isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(l_email).matches()) {
            email.setError("Email is invalid!");
            email.requestFocus();
            return;
        }
        if (l_password.isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(l_email, l_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("MainActivity", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //user has been signed in, use an intent to move to the next activity
                            if(user != null){
                                Intent intent = new Intent(MainActivity.this, EntryPage_Activity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("MainActivity", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Login failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}