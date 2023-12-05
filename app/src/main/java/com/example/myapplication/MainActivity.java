package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PlantAdapter plants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView plantRecyclerView = findViewById(R.id.plantRecyclerView);
        plantRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        plants = new PlantAdapter();
        plantRecyclerView.setAdapter(plants);

        plantRecyclerView.setOnClickListener(v -> {

        });


        FloatingActionButton button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(v -> {
//            FirebaseDatabase.getInstance().getReference().child("test").child("testing").setValue("tested");
            plants.addPlant(new Plant("New Plant", "A beautiful plant", R.drawable.ic_launcher_background));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Plant rose = new Plant("rose", "a rose plant", R.drawable.ic_launcher_foreground);
        plants.addPlant(rose);
    }
}