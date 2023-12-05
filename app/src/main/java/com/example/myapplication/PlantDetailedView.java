package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class PlantDetailedView extends AppCompatActivity {
    private Plant plant;
    private Uri photoURI = null;
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private static final int CAMERA_PERMISSION_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detailed_view);

        // request permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            // Request camera permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        }

        // get intent data (plant id to retrieve data from)
        String plantId = getIntent().getStringExtra("PLANT_ID");
        if (plantId != null) {
            Log.d("GreenThumb: PlantDetailedView", "Valid plant id, fetching data...");
            fetchPlantData(plantId);
        } else {
            Log.d("GreenThumb: PlantDetailedView", "Error plant id ):");
        }

        // on click events
        Button confirmButton = findViewById(R.id.buttonConfirm); // confirm changes, update db
        confirmButton.setOnClickListener(v -> {
            confirmUpdate();
            finish();
        });
        Button cancelButton = findViewById(R.id.buttonCancel); // cancel view, nothing changes
        cancelButton.setOnClickListener(v -> {
            finish();
        });
        FloatingActionButton fabEditImage = findViewById(R.id.fabEditImage); // launch camera
        fabEditImage.setOnClickListener(v -> {
            takePicture();
        });
        ImageView plantImage = findViewById(R.id.imageViewPlantDetail);
        plantImage.setOnClickListener(v -> {
            // [TODO] show full size image
        });

        // setup camera activity
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),result -> {
            if (result) plantImage.setImageURI(photoURI);
            else photoURI = null;
        });
    }

    /*
        Obtains data from db and stores it into local plant object
     */
    private void fetchPlantData(String plantId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Plants");
        ref.child(plantId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                plant = snapshot.getValue(Plant.class);
                setPlantViewData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("GreenThumb: PlantDetailedView", "Error fetching data ):");
            }
        });

    }

    /*
        Sets fetched data to views
     */
    private void setPlantViewData() {
        // [TODO] update for more views later

        // if plant exists then set views to reflect plant data
        if (plant != null || plant.getPlantId() != null) {
            ImageView image = findViewById(R.id.imageViewPlantDetail);
            EditText title = findViewById(R.id.editTextPlantTitle);
            EditText info = findViewById(R.id.editTextPlantInfo);

            title.setText(plant.getTitle());
            info.setText(plant.getInfo());
            // check if custom image, otherwise keep default
            if (plant.hasImage()) image.setImageURI(Uri.parse(plant.getImageURL()));

            Log.d("GreenThumb: PlantDetailedView", "Fetched data!");
        } else {
            Log.d("GreenThumb: PlantDetailedView", "Error fetching data ):");
        }
    }

    /*
        User clicked confirm button, update db
     */
    private void confirmUpdate() {
        // [TODO] update for more info later

        // get objects to grab data from
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Plants");
        EditText title = findViewById(R.id.editTextPlantTitle);
        EditText info = findViewById(R.id.editTextPlantInfo);

        // set plant data to data from views
        plant.setTitle(title.getText().toString());
        plant.setInfo(info.getText().toString());
        plant.setImageURL(photoURI != null ? photoURI.toString() : null); // if no update, then still null

        // update on database
        ref.child(plant.getPlantId()).setValue(plant);
        Log.d("GreenThumb: PlantDetailedView", "Updated plant " + plant.getPlantId());
    }

    /*
        Launches camera
     */
    private void takePicture() {
        // create temp photo file for uri
        File tempPhotoFile = null;
        try { tempPhotoFile = createImageFile(); }
        catch (IOException exception) { /* error */ }

        // get uri of temp file to save captured image to
        if (tempPhotoFile != null) {
            photoURI = FileProvider.getUriForFile(this,
                    "com.example.myapplication",
                    tempPhotoFile);
            takePictureLauncher.launch(photoURI);
        }
    }

    /*
        Create temp image file to store image taken by camera
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "JPEG_" + plant.getPlantId() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Save a file for use with camera
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

//    private void uploadImage(Uri fileUri) {
//        StorageReference plantImageRef = FirebaseStorage.getInstance().getReference().
//                child("images/" + plant.getPlantId() + ".jpg");
//        plantImageRef.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {
//            plantImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                plant.setImageURL(uri.toString());
//            });
//        });
//    }
}