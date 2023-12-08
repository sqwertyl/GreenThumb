package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PlantViewHolder extends RecyclerView.ViewHolder {
    private ImageView thumbnail;
    private TextView titleText;
    private TextView infoText;
    private TextView heightText;
    private PlantAdapter adapter;

    /*
        Constructor for ViewHolder
     */
    public PlantViewHolder(View itemView, PlantAdapter plantAdapter) {
        super(itemView);
        thumbnail = itemView.findViewById(R.id.imageViewPlant);
        titleText = itemView.findViewById(R.id.textViewTitle);
        infoText = itemView.findViewById(R.id.textViewInfo);
        heightText = itemView.findViewById(R.id.textViewHeight);
        adapter = plantAdapter;
    }

    /*
        Called by adapter, sets views to reflect plant data and click events
     */
    public void bind(Plant plant) {
        // set title and info
        titleText.setText(plant.getTitle());
        infoText.setText(plant.getInfo());
        heightText.setText(plant.getCurrentHeight() == null ? "Height: ----" : "Height: "
                + plant.getCurrentHeight() + "cm");

        // if plant has custom image, use that
        if (plant.hasImage()) {
            thumbnail.setImageURI(Uri.parse(plant.getImageURL()));
        }

        // set on click listener to open up detailed view
        itemView.setOnClickListener(v -> {
            Context context = itemView.getContext();
            Intent intent = new Intent(context, PlantDetailedView.class);
            intent.putExtra("PLANT_ID", plant.getPlantId());
            context.startActivity(intent);
        });

        // set long click listener to delete plant
        itemView.setOnLongClickListener(v -> {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Delete Plant")
                        .setMessage("Are you sure you want to delete this plant?")
                        .setPositiveButton("Delete", ((dialog, which) -> {
                            adapter.removePlant(pos);
                        }))
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
            return false;
        });
    }
}
