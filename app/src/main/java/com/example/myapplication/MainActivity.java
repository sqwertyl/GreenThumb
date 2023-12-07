package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

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

}