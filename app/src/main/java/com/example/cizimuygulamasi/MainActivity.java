package com.example.cizimuygulamasi;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.RangeSlider;

import java.io.OutputStream;

import petrov.kristiyan.colorpicker.ColorPicker;


public class MainActivity extends AppCompatActivity {
    private MyCanvas canvas;
    private  float size;
    private  int brushColor;
    private  int backgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnClear = findViewById(R.id.clear);
        Button colorPick = findViewById(R.id.changeColor);
        Button bgColor = findViewById(R.id.changeBackgorund);
        Button save = findViewById(R.id.save);

        RangeSlider rangeSlider = (RangeSlider) findViewById(R.id.rangebar);
        rangeSlider.setValueFrom(0.0f);
        rangeSlider.setValueTo(200.0f);

        LinearLayout cizimYeri = findViewById(R.id.cizimYeri);

        canvas = new MyCanvas(MainActivity.this);
        cizimYeri.addView(canvas);

        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                canvas.setSize(value);
                size = value;
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cizimYeri.removeAllViews();
                canvas = new MyCanvas(MainActivity.this);
                cizimYeri.addView(canvas);
                canvas.setSize(size);
                canvas.setColor(brushColor);
                canvas.setBg(backgroundColor);
            }
        });


        colorPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ColorPicker colorPicker = new ColorPicker(MainActivity.this);
                colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                    @Override
                    public void setOnFastChooseColorListener(int position, int color) {
                        canvas.setColor(color);
                        brushColor = color;
                    }

                    @Override
                    public void onCancel() {
                        colorPicker.dismissDialog();
                    }
                })
                        .disableDefaultButtons(true)
                        .setColumns(10)
                        .show();
            }
        });


        bgColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ColorPicker colorPicker = new ColorPicker(MainActivity.this);
                colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                    @Override
                    public void setOnFastChooseColorListener(int position, int color) {
                        canvas.setBg(color);
                        backgroundColor = color;
                        canvas.invalidate();
                    }

                    @Override
                    public void onCancel() {
                        colorPicker.dismissDialog();
                    }
                })
                        .disableDefaultButtons(true)
                        .setColumns(10)
                        .show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bmp = canvas.getBitmap();
                save(bmp);
            }
        });
    }

    private void save(Bitmap bitmap) {
        OutputStream imageOutStream = null;
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png");
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        try {
            imageOutStream = getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);
            imageOutStream.close();
            Toast.makeText(MainActivity.this, "Kaydedildi", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}