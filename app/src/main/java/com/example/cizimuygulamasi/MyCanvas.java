package com.example.cizimuygulamasi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MyCanvas extends View {

    private Paint paint;
    private Path path;

    private ArrayList<Stroke> paths = new ArrayList<>();

    float size = 4f;
    int brushColor = Color.RED;
    int backGroundColor = Color.WHITE;

    public void setSize(float size) {
        this.size = size;
    }

    public void setColor(int color) {
        this.brushColor = color;
    }

    public void setBg(int bg) {
        this.backGroundColor = bg;
    }

    public MyCanvas(Context context) {
        super(context);
        paint = new Paint();
        path = new Path();
        paint.setAntiAlias(true);
        paint.setColor(this.brushColor);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(this.size);
    }


    public Bitmap getBitmap() {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        return bmp;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backGroundColor);
        for (Stroke temp : paths) {
            paint.setColor(temp.color);
            paint.setStrokeWidth(temp.strokeWidth);
            canvas.drawPath(temp.path, paint);
        }
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            path = new Path();
            Stroke fp = new Stroke(this.brushColor, this.size, path);
            paths.add(fp);
            path.moveTo(x, y);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            path.lineTo(x, y);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            path.lineTo(x, y);
        }

        invalidate();
        return true;
    }
}
