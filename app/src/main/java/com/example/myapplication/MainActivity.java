package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PlantAdapter plants;
    private DatabaseReference plantDB = FirebaseDatabase.getInstance().getReference("Plants");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind custom recycler view for plants
        RecyclerView plantRecyclerView = findViewById(R.id.plantRecyclerView);
        plantRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        plants = new PlantAdapter(this);
        plantRecyclerView.setAdapter(plants);

        plantRecyclerView.setOnClickListener(v -> {
            // [TODO] add plant dialog
        });


        // add plant button
        FloatingActionButton button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(v -> {
            plants.addPlant(new Plant("New Plant", "A beautiful plant",
                    R.drawable.ic_launcher_background));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Plant rose = new Plant("rose", "a rose plant",
//                R.drawable.ic_launcher_foreground);
//        plants.addPlant(rose);
    }

    private void updateFromDB() {
//        plantdb.
    }

}