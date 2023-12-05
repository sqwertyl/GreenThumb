package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantViewHolder> {
    private ArrayList<Plant> plantList; // Your data model

    public PlantAdapter() {
        plantList = new ArrayList<>();
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
        // Set the image for the ImageView
        // Example: holder.imageView.setImageResource(plant.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public void addPlant(Plant newPlant) {
        plantList.add(newPlant);
        notifyItemInserted(plantList.size() - 1);
    }

    public void removePlant(int position) {
        plantList.remove(position);
        notifyItemRemoved(position);
    }

    public void updatePlant(int pos, Plant updatedPlant) {
        plantList.set(pos, updatedPlant);
        notifyItemChanged(pos);
    }
}

