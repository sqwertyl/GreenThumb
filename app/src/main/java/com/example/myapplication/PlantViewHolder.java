package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PlantViewHolder extends RecyclerView.ViewHolder {
    public ImageView thumbnail;
    public TextView titleText;
    public TextView infoText;
    public PlantViewHolder(View itemView) {
        super(itemView);
        thumbnail = itemView.findViewById(R.id.imageViewPlant);
        titleText = itemView.findViewById(R.id.textViewTitle);
        infoText = itemView.findViewById(R.id.textViewInfo);
    }
}
