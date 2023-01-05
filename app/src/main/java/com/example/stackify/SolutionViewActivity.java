package com.example.stackify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SolutionViewActivity extends AppCompatActivity {
    private Solution solution;
    private int bitmapHeight;
    private int bitmapWidth;
    private ImageView solImageView;
    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvas;
    private ArrayList<Integer> colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_view);

        Bundle bundle = getIntent().getBundleExtra("Bundle");
        solution = (Solution) bundle.getSerializable("solution");
        bitmapHeight = solution.getContainerHeight();
        bitmapWidth = solution.getContainerWidth();
        solImageView = findViewById(R.id.solImageView);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        solImageView.setImageBitmap(bitmap);
        canvas = new Canvas(bitmap);
        // Flip the canvas so that (0,0) is at the bottom left of screen
        canvas.scale(1f, -1f, bitmapWidth / 2f, bitmapHeight / 2f);
        colors = new ArrayList<>();
        Integer[] colorList = new Integer[] {Color.RED, Color.BLUE, Color.CYAN, Color.YELLOW, Color.MAGENTA};
        colors.addAll(Arrays.asList(colorList));



        int containerHeight = 600;
        int containerWidth = 700;
        int containerLength = 200;
        ArrayList<Box> testBoxList = new ArrayList<Box>();
        Box box1 = new Box(1, 200, 400, 200);
        Box box2 = new Box(2, 200, 250, 200);
        Box box3 = new Box(3, 300, 300, 200);
        Box box4 = new Box(4, 300, 350, 200);

        testBoxList.add(box1);
        testBoxList.add(box2);
        testBoxList.add(box3);
        testBoxList.add(box4);

        Solver solver = new NoOrderGreedyColumnSolver(testBoxList, containerHeight, containerWidth, containerLength);
        solver.solve();
        solution = solver.getSolution();
        drawBoxes(0);
    }

    public void drawBoxes(int segmentNum) {
        // Clear the bitmap of the canvas for a new drawing
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Random randomizer = new Random();
        Segment segment = solution.getSegmentList().get(segmentNum);
        for (Box box : segment.getBoxList()) {
            Integer randomColor = colors.get(randomizer.nextInt(colors.size()));
            int bottom = box.getBottomLeft().getY();
            int left = box.getBottomLeft().getX();
            paint.setColor(randomColor);
            canvas.drawRect(left, bottom + box.getHeight(), left + box.getWidth(), bottom, paint);

            // Draw a label for the box
            paint.setColor(Color.BLACK);
            String formattedString = String.format("%d", box.getUnpackOrder());
            canvas.drawText(formattedString, (left + box.getWidth()) / 2 , (bottom + box.getHeight()) / 2, paint);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}