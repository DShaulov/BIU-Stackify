package com.example.stackify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SolutionViewActivity extends AppCompatActivity {
    private Button nextSegmentBtn;
    private Button prevSegmentBtn;
    private Button optionsBtn;
    private TextView segmentNumTextView;
    private Solution solution;
    private int segmentNum;
    private int containerHeight;
    private int containerWidth;
    private int bitmapHeight;
    private int bitmapWidth;
    private ImageView solImageView;
    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvas;
    private ArrayList<Integer> colors;
    private int appGreen;
    private int appBlack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_view);

        nextSegmentBtn = findViewById(R.id.nextSegmentBtn);
        prevSegmentBtn = findViewById(R.id.prevSegmentBtn);
        optionsBtn = findViewById(R.id.optionsBtn);
        segmentNumTextView = findViewById(R.id.segmentNumTextView);

        Bundle bundle = getIntent().getBundleExtra("Bundle");
        solution = (Solution) bundle.getSerializable("solution");
        containerHeight = solution.getContainerHeight();
        containerWidth = solution.getContainerWidth();
        bitmapHeight = solution.getContainerHeight();
        bitmapWidth = solution.getContainerWidth();
        segmentNum = 0;

        solImageView = findViewById(R.id.solImageView);
        paint = new Paint();
        paint.setTextSize(paint.getTextSize() * 4);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        solImageView.setImageBitmap(bitmap);
        canvas = new Canvas(bitmap);
        colors = new ArrayList<>();
        appGreen = Color.rgb(29, 185, 84);
        appBlack = Color.rgb(25, 20, 20);
        Integer[] colorList = new Integer[] {appGreen};
        colors.addAll(Arrays.asList(colorList));

        nextSegmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextSegment();
            }
        });
        prevSegmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPrevSegment();
            }
        });
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchOptionsDialog();
            }
        });

        updateSegmentNumTextView();
        drawBoxes(segmentNum);
    }

    public void drawBoxes(int segmentNum) {
        // Clear the bitmap of the canvas for a new drawing
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Random randomizer = new Random();
        Segment segment = solution.getSegmentList().get(segmentNum);
        for (Box box : segment.getBoxList()) {
            Integer randomColor = colors.get(randomizer.nextInt(colors.size()));
            // Since (0,0) is the top-left corner of the device, y-axis calculations need to take that into account
            int top = this.containerHeight - box.getBottomLeft().getY();
            int bottom = top - box.getHeight();
            int left = box.getBottomLeft().getX();
            int right = left + box.getWidth();
            paint.setColor(randomColor);
            canvas.drawRect(left, top, right, bottom, paint);

            // Draw a black frame around the rectangle
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            paint.setColor(appBlack);
            canvas.drawRect(left, top, right, bottom, paint);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(5);

            // Draw a label for the box
            paint.setColor(Color.BLACK);
            String formattedString = String.format("%d", box.getUnpackOrder());
            canvas.drawText(formattedString, (2 * left + box.getWidth()) / 2 , (2 * bottom + box.getHeight()) / 2, paint);

        }
    }

    public void showNextSegment() {
        if (segmentNum != solution.getNumOfSegments() - 1) {
            segmentNum += 1;
            updateSegmentNumTextView();
            drawBoxes(segmentNum);
        }
    }

    public void showPrevSegment() {
        if (segmentNum != 0) {
            segmentNum -= 1;
            updateSegmentNumTextView();
            drawBoxes(segmentNum);
        }
    }

    private void launchOptionsDialog() {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(this);
        dialogBuilder.setView(R.layout.dialog_solution_viewer);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button saveSolutionBtn = alertDialog.findViewById(R.id.saveSolutionBtn);
                Button solutionInfoBtn = alertDialog.findViewById(R.id.solutionInfoBtn);
                Button rearrangeBtn = alertDialog.findViewById(R.id.rearrangeManuallyBtn);

                saveSolutionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveSolution();
                    }
                });
                solutionInfoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showSolutionInfo();
                    }
                });
                rearrangeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rearrangeSolution();
                    }
                });
            }
        });
        alertDialog.show();
    }

    public void saveSolution() {

    }

    // TODO
    public void showSolutionInfo() {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(this);
        dialogBuilder.setView(R.layout.dialog_solution_viewer);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

            }
        });
        alertDialog.show();
    }

    public void rearrangeSolution() {

    }

    public void updateSegmentNumTextView() {
        String segmentNumString = String.format("Segment %d of %d", segmentNum + 1, solution.getNumOfSegments());
        segmentNumTextView.setText(segmentNumString);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}