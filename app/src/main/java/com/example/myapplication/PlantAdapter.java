package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantViewHolder> {

    private Context context;
    private ArrayList<Plant> plantList;
    private FirebaseUser currentUser;
    private DatabaseReference plantDBuser;
    private static final DatabaseReference plantDB = FirebaseDatabase.getInstance()
            .getReference();

    /*
        Constructor that sets up ArrayList and event listeners to update the RecyclerView
     */
    public PlantAdapter(Context context) {
        // instantiate new arraylist
        plantList = new ArrayList<>();
        this.context = context;
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // check if user valid, or else app will crash lol
        if (currentUser != null) {
            plantDBuser = plantDB.child(currentUser.getUid());
            plantDBuser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // if data exists...
                    if (snapshot.exists()) {
                        plantList.clear();
                        // add each instance of a plant
                        if (currentUser != null) {
                            for (DataSnapshot children : snapshot.getChildren()) {
                                String UID = currentUser.getUid();
                                Plant dbPlant = children.getValue(Plant.class);
                                plantList.add(dbPlant);
                            }
                        }

//                    Toast.makeText(context, "Added " + getItemCount() + " plants", Toast.LENGTH_SHORT).show();

                        // notify that data changed
                        notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }

    }

    /*
        Override to show our custom PlantViewHolder
     */
    @Override
    public PlantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant_item, parent, false);
        return new PlantViewHolder(itemView, this);
    }

    /*
        Called once ViewHolder is created, we can use to pass plant data
     */
    @Override
    public void onBindViewHolder(PlantViewHolder holder, int position) {
        Plant plant = plantList.get(position);
        holder.bind(plant);
    }

    /*
        Returns how many items in list
     */
    @Override
    public int getItemCount() {
        return plantList.size();
    }

    /*
        Adds a new plant to the list and adds to Firebase
     */
    public void addPlant(Plant newPlant) {
        // add plant to arraylist
        plantList.add(newPlant);

        // obtain unique key for plant
        String key = plantDBuser.push().getKey();

        // set id per plant add add to db
        newPlant.setPlantId(key);
        plantDBuser.child(key).setValue(newPlant);

        // notify adapter that data changed
        notifyItemInserted(plantList.size() - 1);
    }

    /*
        Removes plant from list and Firebase
     */
    public void removePlant(int position) {
        // temp to get residual data
        Plant temp = plantList.get(position);

        // remove from firebase and list
        plantDBuser.child(temp.getPlantId()).removeValue();
        plantList.remove(position);
        // delete stored image
        if (temp.hasImage()) removeStoredImage(Uri.parse(temp.getImageURL()));

        Toast.makeText(context, "Removed " + temp.getTitle(), Toast.LENGTH_SHORT);
        Log.d("GreenThumb", "Removed plant: \"" + temp.getTitle() + "\" id: " +
                temp.getPlantId());

        // notify that data changed
        notifyItemRemoved(position);
    }

    /*
        Remove the locally stored image of plant (user created)
     */
    private void removeStoredImage(Uri imageUri) {
        // get the image file of plant
        File imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String fileName = imageUri.getLastPathSegment();
        File imageFile = new File (imagesDir, fileName);

        // if exists, try to delete
        if (imageFile.exists()) {
            Log.d("GreenThumb", "Attempting to delete image uri");
            boolean deleted = imageFile.delete();
            // if deleted, update content resolver to reflect deletion
            if (deleted) {
                context.getContentResolver().delete(imageUri, null, null);
                Log.d("GreenThumb", "Removed stored image");
            } else {
                Log.d("GreenThumb", "error removing image");
            }
        } else {
            Log.d("GreenThumb", "img uri does not exist?" + imageFile.getAbsolutePath());
        }
    }

//    public void updatePlant(int pos, Plant updatedPlant) {
//        plantList.set(pos, updatedPlant);
//        notifyItemChanged(pos);
//    }


}

