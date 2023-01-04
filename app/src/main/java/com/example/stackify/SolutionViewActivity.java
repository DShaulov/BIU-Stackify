package com.example.stackify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

public class SolutionViewActivity extends AppCompatActivity {
    private Solution solution;
    private int bitmapHeight;
    private int bitmapWidth;
    private ImageView solImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_view);

        Bundle bundle = getIntent().getBundleExtra("Bundle");
        solution = (Solution) bundle.getSerializable("solution");
        bitmapHeight = solution.getContainerHeight();
        bitmapWidth = solution.getContainerWidth();
        solImageView = findViewById(R.id.solImageView);

        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1f, -1f, bitmapWidth / 2f, bitmapHeight / 2f);
        canvas.drawCircle(bitmapWidth / 2, bitmapHeight / 2, bitmapWidth / 5, paint);
        Rect rect = new Rect(0, 100, 100, 0);
        canvas.drawRect(rect, paint);

        solImageView.setImageBitmap(bitmap);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}