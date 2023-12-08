package com.example.myapplication;

public class Plant {
    private String title;
    private String info;
    private String plantId;
    private String imageURL;
    private String currentHeight;
    private String previousHeights;
    private static final int imageResId = R.drawable.ic_launcher_foreground;

    /*
        Empty, no-arg constructor for Firebase
     */
    public Plant() {}

    /*
        Constructor for plant object
     */
    public Plant(String title, String info) {
        this.title = title;
        this.info = info;
        imageURL = null;
        plantId = null;
    }

    public String getTitle() { return title; }
    public String getInfo() { return info; }

//    public int getImageResId() { return imageResId; }
    public String getPlantId() { return plantId; }
    public String getImageURL() { return imageURL; }
    public String getCurrentHeight() { return currentHeight; }
    public String getPreviousHeights() { return previousHeights; }

    public void setTitle(String title) { this.title = title; }
    public void setInfo(String info) { this.info = info; }
    public void setPlantId(String id) { plantId = id; }
    public void setImageURL(String newImageURL) { imageURL = newImageURL; }
    public void setCurrentHeight(String height) { currentHeight = height; }
    public void setPreviousHeights(String heights) { previousHeights = heights; }
    public Boolean hasImage() { return imageURL != null; }
}
