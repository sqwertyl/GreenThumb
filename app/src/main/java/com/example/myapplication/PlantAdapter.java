package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantViewHolder> {
    // ---------> variables <--------
    private ArrayList<Plant> plantList;
    DatabaseReference plantDB = FirebaseDatabase.getInstance().getReference("Plants");
    private Context context;

    // --------> methods <--------
    public PlantAdapter(Context context) {
        // instantiate new arraylist
        plantList = new ArrayList<>();
        this.context = context;

        // listener to update local from cloud
        plantDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // if data exists...
                if (snapshot.exists()) {
                    plantList.clear();
                    // add each instance of a plant
                    for (DataSnapshot children :  snapshot.getChildren()) {
                        Plant dbPlant = children.getValue(Plant.class);
                        plantList.add(dbPlant);
                    }
                    Toast.makeText(context, "Added " + getItemCount() + " plants", Toast.LENGTH_SHORT).show();

                    notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    public PlantAdapter(ArrayList<Plant> plantList) {
        this.plantList = plantList;
    }


    @Override
    public PlantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant_item, parent, false);
        return new PlantViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(PlantViewHolder holder, int position) {
        Plant plant = plantList.get(position);
        holder.titleText.setText(plant.getTitle());
        holder.infoText.setText(plant.getInfo());
        // [TODO] Set the thumbnail for plant
        // Example: holder.imageView.setImageResource(plant.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public void addPlant(Plant newPlant) {
        // add plant to arraylist
        plantList.add(newPlant);

        // obtain firebase reference for plants

        String key = plantDB.push().getKey(); // unique key for plant

        // set id per plant add add to db
        newPlant.setPlantId(key);
        plantDB.child(key).setValue(newPlant);

        // notify adapter that smth changed
        notifyItemInserted(plantList.size() - 1);
    }

    public void removePlant(int position) {
        plantList.remove(position);

        // [TODO] remove from firebase db
        notifyItemRemoved(position);
    }

    public void updatePlant(int pos, Plant updatedPlant) {
        plantList.set(pos, updatedPlant);
        notifyItemChanged(pos);
    }
}

