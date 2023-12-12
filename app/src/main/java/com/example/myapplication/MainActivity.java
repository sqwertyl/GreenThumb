package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private PlantAdapter plants;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup authentication -> we need this here so the adapter doesn't error out
        mAuth = FirebaseAuth.getInstance();
        checkLogin();

        // setup notification channel
        createNotificationChannel();

        // bind custom recycler view for plants
        RecyclerView plantRecyclerView = findViewById(R.id.plantRecyclerView);
        plantRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        plants = new PlantAdapter(this);
        plantRecyclerView.setAdapter(plants);

        // button behaviors
        FloatingActionButton addPlantButton = findViewById(R.id.addPlantButton);
        addPlantButton.setOnClickListener(v -> {
            plants.addPlant(new Plant("New Plant", "A beautiful plant"));
        });
        ImageButton signOutButton = findViewById(R.id.btnSignOut);
        signOutButton.setOnClickListener(v -> handleSignOut());

        // water reminder switch behavior
        Switch notifSwitch = findViewById(R.id.notifSwitch);
        notifSwitch.setOnCheckedChangeListener((v, checked) -> {
            scheduleWaterReminder(checked);
        });
        if (getPreferences(Context.MODE_PRIVATE)
                .getBoolean("waterReminderSwitch", false))
            notifSwitch.setChecked(true);

    }

    /*
        checks if user is logged in:
            yes: all good
            no: send to LoginActivity
     */
    private void checkLogin() {
        if (mAuth.getCurrentUser() == null) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            finish();
        }
    }

    /*
        show alert to sign user out, then show login activity again
     */
    private void handleSignOut() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out? You will need to log in again to " +
                        "view your plants")
                .setPositiveButton("Sign Out", ((dialog, which) -> {
                    // sign out of current user and open login
                    mAuth.signOut();
                    checkLogin();
                }))
                .setNegativeButton("Cancel", null)
                .show();
    }

    /*
        schedules the daily reminders
     */
    private void scheduleWaterReminder(boolean enable) {
        // save switch state
        getPreferences(Context.MODE_PRIVATE).edit()
                .putBoolean("waterReminderSwitch", enable).apply();

        // request notification permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

        // use alarm service to track time
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, WaterReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);

        if (enable) {
            Log.d("GreenThumb: MainActivity", "Enabled water notifications");
            // set alarm at noon
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            calendar.add(Calendar.SECOND, 5);

            // repeat alarm daily
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d("GreenThumb: MainActivity", "Set reminder at " + calendar.getTime());
        } else {
            Log.d("GreenThumb: MainActivity", "Disabled water notifications");
            alarmManager.cancel(pendingIntent);
        }
    }

    /*
        create notification channel to send notification
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("GreenThumb",
                    "GreenThumb", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}