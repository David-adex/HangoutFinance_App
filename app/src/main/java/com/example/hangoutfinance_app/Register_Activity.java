package com.example.hangoutfinance_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register_Activity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private EditText register_age, register_firstname, register_surname, register_email,register_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        register_age = findViewById(R.id.register_age);
        register_firstname = findViewById(R.id.register_firstname);
        register_surname = findViewById(R.id.register_surname);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
    }


    public void signupButtonClicked(View view){
        register_User();
    }

    private void register_User() {
        String email = register_email.getText().toString().trim();
        String password = register_password.getText().toString().trim();
        String name = register_firstname.getText().toString().trim();
        String Age = register_age.getText().toString().trim();
        String surname = register_surname.getText().toString().trim();

        if (name.isEmpty()) {
            register_firstname.setError("First Name is required!");
            register_firstname.requestFocus();
            return;
        }
        if (surname.isEmpty()) {
            register_surname.setError("Surname is required!");
            register_surname.requestFocus();
            return;
        }
        if (Age.isEmpty()) {
            register_age.setError("Age is required!");
            register_age.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            register_email.setError("Email is required!");
            register_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            register_email.setError("Email is invalid!");
            register_email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            register_password.setError("Password is required!");
            register_password.requestFocus();
            return;
        }
        if(password.length()<10){
            register_password.setError("Password length must be up to 10 Characters");
            register_password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(name, Age, surname, email);

                            FirebaseDatabase.getInstance().getReference("MyApp Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("Register_Activity", "createUserWithEmail:success");
                                                //user has been signed in, use an intent to move to the next activity
                                                Intent intent = new Intent(Register_Activity.this, EntryPage_Activity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // If sign up fails, display a message to the user.
                                                Log.w("Register_Activity", "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(Register_Activity.this, "Registration failed.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }else {
                            // If sign up fails, display a message to the user.
                            Log.w("Register_Activity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register_Activity.this, "Registration failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}