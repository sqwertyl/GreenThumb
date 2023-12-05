package com.example.myapplication;

public class Plant {
    private String title;
    private String info;
    private int imageResId;
    private String plantId;

    public Plant() {}

    public Plant(String title, String info, int resId) {
        this.title = title;
        this.info = info;
        imageResId = resId;
        plantId = null;
    }

    public String getTitle() { return title; }
    public String getInfo() { return info; }

    public int getImageResId() { return imageResId; }
    public String getPlantId() { return plantId; }

    public void setTitle(String title) { this.title = title; }
    public void setInfo(String info) { this.info = info; }
    public void setImageResId(int id) { imageResId = id; }
    public void setPlantId(String id) { plantId = id; }
}
