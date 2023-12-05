package com.example.myapplication;

public class Plant {
    private String title;
    private String info;
    private int imageResId;

    public Plant(String title, String info, int resId) {
        this.title = title;
        this.info = info;
        imageResId = resId;
    }

    public String getTitle() { return title; }
    public String getInfo() { return info; }

    public int getImageResId() { return imageResId; }

    public void setTitle(String title) { this.title = title; }
    public void setInfo(String info) { this.info = info; }
    public void setImageResId(int id) { imageResId = id; }
}
