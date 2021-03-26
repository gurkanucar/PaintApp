package com.example.cizimuygulamasi;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.slider.RangeSlider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import petrov.kristiyan.colorpicker.ColorPicker;


public class MainActivity extends AppCompatActivity {
    private MyCanvas canvas;
    private float size;
    private int brushColor= Color.RED;
    private int backgroundColor=Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnClear = findViewById(R.id.clear);
        Button colorPick = findViewById(R.id.changeColor);
        Button bgColor = findViewById(R.id.changeBackgorund);
        Button save = findViewById(R.id.save);
        Button undo = findViewById(R.id.undo);

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
                canvas.invalidate();
            }
        });


        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canvas.getPaths().size()>0){
                    canvas.getPaths().remove(canvas.getPaths().size()-1);
                    canvas.invalidate();
                }
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
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                Bitmap bmp = canvas.getBitmap();
                save(bmp);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void save(Bitmap bitmap) {
        requestPermissionForReadExtertalStorage();
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            OutputStream fOut = null;
            File file = new File(path, "image.png");
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions((Activity) MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    101);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}