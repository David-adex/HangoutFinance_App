package com.example.hangoutfinance_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.NotificationManager;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;

public class NewTrips_Activity extends AppCompatActivity {
    private TextView Return;
    private FirebaseAuth mAuth;

    private EditText newTrips_name, newTrips_members;
    List<String> data = new ArrayList<>();

    int notifier_counter = 0;
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trips);

        mAuth = FirebaseAuth.getInstance();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        newTrips_name = (EditText) findViewById(R.id.trip_name);
        newTrips_members = (EditText) findViewById(R.id.member_name);

        Return = (TextView) findViewById(R.id.back2);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTrips_Activity.this, EntryPage_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void submit_buttonClicked(View view) {
        save_details();
    }

    public void done_buttonClicked(View view) {
        Intent intent = new Intent(this, Home_Activity.class);
        startActivity(intent);
        finish();
    }

    public void save_details() {
        EditText newTrips_size = (EditText) findViewById(R.id.group_size);

        String t_name = newTrips_name.getText().toString().trim();
        String m_name = newTrips_members.getText().toString().trim();
        String g_size = newTrips_size.getText().toString().trim();

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("TRIP NAME", t_name);
        dataMap.put("Member name", m_name);
        dataMap.put("GROUP SIZE", g_size);

        FirebaseDatabase.getInstance().getReference("MyApp Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Trips Info")
                .push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("NewTrips_Activity", "createNewTrip:success");
                        }
                    }
                });
        newTrips_size.setText("");
        newTrips_members.setText("");
        newTrips_name.setText("");

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("my_channel_id", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setDescription("My notification channel");
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//// Build the notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(NewTrips_Activity.this, "my_channel_id")
//                .setContentTitle("New Trips")
//                .setContentText("Your new trip has been created successfully")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//// Show the notification
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NewTrips_Activity.this);
//        if (ActivityCompat.checkSelfPermission(NewTrips_Activity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        notificationManager.notify(0, builder.build());
    }
}