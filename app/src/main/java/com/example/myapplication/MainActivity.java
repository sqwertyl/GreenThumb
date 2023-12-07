package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private PlantAdapter plants;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind custom recycler view for plants
        RecyclerView plantRecyclerView = findViewById(R.id.plantRecyclerView);
        plantRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        plants = new PlantAdapter(this);
        plantRecyclerView.setAdapter(plants);

        // add plant button
        FloatingActionButton button = findViewById(R.id.addPlantButton);
        button.setOnClickListener(v -> {
            plants.addPlant(new Plant("New Plant", "A beautiful plant"));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

}