package com.example.cizimuygulamasi;

import android.graphics.Path;

public class Stroke {

    public int color;
    public float strokeWidth;
    public Path path;

    public Stroke(int color, float strokeWidth, Path path) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
}