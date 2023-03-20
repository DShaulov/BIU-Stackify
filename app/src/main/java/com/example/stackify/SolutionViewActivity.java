package com.example.stackify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SolutionViewActivity extends AppCompatActivity {
    private AppDB db;
    private SolutionDao solutionDao;
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
    private int cardboardDark;
    private int cardboardLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_view);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "StackifyDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        solutionDao = db.solutionDao();

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
        cardboardLight = Color.rgb(154, 113, 65);
        cardboardDark = Color.rgb(126, 92, 53);
        Integer[] colorList = new Integer[] {cardboardLight};
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
        int backgroundOffset = 50;
        for (Box box : segment.getBoxList()) {
            Integer randomColor = colors.get(randomizer.nextInt(colors.size()));

            // Draw a background rectangle at offset
//            int top = this.containerHeight - box.getBottomLeft().getY() - backgroundOffset;
//            int bottom = top - box.getHeight();
//            int left = box.getBottomLeft().getX();
//            int right = left + box.getWidth() + backgroundOffset;
//            paint.setColor(appGreen);
//            canvas.drawRect(left, top, right, bottom, paint);

            // Since (0,0) is the top-left corner of the device, y-axis calculations need to take that into account
            int top = this.containerHeight - box.getBottomLeft().getY();
            int bottom = top - box.getHeight();
            int left = box.getBottomLeft().getX();
            int right = left + box.getWidth();
            paint.setColor(randomColor);
            paint.setShadowLayer(10.0f, 2.0f, 2.0f, 0xFF000000);
            canvas.drawRect(left, top, right, bottom, paint);
            paint.setShadowLayer(0, 0, 0, 0);

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
        // Prompt user to enter name for solution
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(this);
        dialogBuilder.setView(R.layout.dialog_solution_saver);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                EditText enterNameEditText = alertDialog.findViewById(R.id.enterNameEditText);
                Button enterNameSaveBtn = alertDialog.findViewById(R.id.enterNameSaveBtn);

                enterNameSaveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String solutionName = enterNameEditText.getText().toString();
                        if (solutionName.isEmpty()) {
                            enterNameEditText.setHint("*Cannot be empty");
                            return;
                        }
                        solution.setDate(LocalDate.now());
                        solution.setSolutionName(solutionName);
                        solutionDao.insert(solution);
                        alertDialog.cancel();
                    }
                });
            }
        });
        alertDialog.show();
    }

    // TODO
    public void showSolutionInfo() {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(this);
        dialogBuilder.setView(R.layout.dialog_solution_info_viewer);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                ProgressBar progressBar = alertDialog.findViewById(R.id.progressBar);
                TextView progressBarPercentTextView = alertDialog.findViewById(R.id.progressBarPercentTextView);
                TextView unpackedBoxesNumberTextView = alertDialog.findViewById(R.id.unpackedBoxesNumberTextView);

                Integer percentPacked = Math.round(solution.getCoverage());
                progressBarPercentTextView.setText(percentPacked.toString() + "%");
                long paddingLeftDp = Math.round(((float)percentPacked / 100.0) * 206) - 30;
                float scale = getResources().getDisplayMetrics().density;
                int paddingLeftPx = (int) (paddingLeftDp * scale + 0.5f);
                progressBarPercentTextView.setPadding(paddingLeftPx, 0, 0, 0);
                progressBar.setProgress(percentPacked);
                String unpackedBoxesString = "";
                for (Box box : solution.getBoxList()) {
                    if (!box.isPacked()) {
                        Integer unpackedOrder = box.getUnpackOrder();
                        unpackedBoxesString = unpackedBoxesString + unpackedOrder + ", ";
                    }
                }
                unpackedBoxesNumberTextView.setText(unpackedBoxesString.substring(0, unpackedBoxesString.length() - 2));
            }
        });
        alertDialog.show();
    }

    public void rearrangeSolution() {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(this);
        dialogBuilder.setView(R.layout.dialog_solution_rearrange_selection);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                EditText enterBoxNumEditText = alertDialog.findViewById(R.id.enterBoxNumEditText);
                EditText enterSegmentNumEditText = alertDialog.findViewById(R.id.enterSegmentNumEditText);
                EditText enterHorizontalPosEditText = alertDialog.findViewById(R.id.enterHorizontalPosEditText);
                EditText enterVerticalPosEditText = alertDialog.findViewById(R.id.enterVerticalPosEditText);
                Button rearrangeOkBtn = alertDialog.findViewById(R.id.rearrangeOkBtn);

                enterSegmentNumEditText.setHint("*Segment Number (1-" + solution.getNumOfSegments() + ")");
                enterHorizontalPosEditText.setHint("*X Position (0-" + solution.getContainerWidth() + ")");
                enterVerticalPosEditText.setHint("*Y Position (0-" + solution.getContainerHeight() + ")");

                rearrangeOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String boxNumString = enterBoxNumEditText.getText().toString();
                        String segmentNumString = enterSegmentNumEditText.getText().toString();
                        String xPositionString = enterHorizontalPosEditText.getText().toString();
                        String yPositionString = enterVerticalPosEditText.getText().toString();
                        if (boxNumString.isEmpty() || segmentNumString.isEmpty() || xPositionString.isEmpty() || yPositionString.isEmpty()) {
                            Toast.makeText(SolutionViewActivity.this,"Fields cannot be empty", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Integer boxNum = Integer.parseInt(enterBoxNumEditText.getText().toString());
                        Integer segmentNum = Integer.parseInt(enterSegmentNumEditText.getText().toString());
                        Integer xPosition = Integer.parseInt(enterHorizontalPosEditText.getText().toString());
                        Integer yPosition = Integer.parseInt(enterVerticalPosEditText.getText().toString());

                        if (solution.getBoxList().size() < boxNum) {
                            Toast.makeText(SolutionViewActivity.this,"Box number " + boxNum + " does not exist", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (solution.getSegmentList().size() < segmentNum) {
                            Toast.makeText(SolutionViewActivity.this,"Segment Number must be (1-" + solution.getNumOfSegments() + ")", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (solution.getContainerWidth() < xPosition) {
                            Toast.makeText(SolutionViewActivity.this,"X Position must be (0-" + solution.getContainerWidth() + ")", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (solution.getContainerHeight() < yPosition) {
                            Toast.makeText(SolutionViewActivity.this,"Y Position must be (0-" + solution.getContainerHeight() + ")", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Box manuallyPlacedBox = null;
                        for (Box box : solution.getBoxList()) {
                            if (box.getUnpackOrder() == boxNum) {
                                manuallyPlacedBox = box;
                                break;
                            }
                        }
                        if (boxIsOutOfBounds(manuallyPlacedBox, xPosition, yPosition)) {
                            Toast.makeText(SolutionViewActivity.this,"Box is out of bounds", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (spaceIsAlreadyTaken(manuallyPlacedBox, segmentNum, xPosition, yPosition)) {
                            Toast.makeText(SolutionViewActivity.this,"Space is already taken by another box", Toast.LENGTH_LONG).show();
                            return;
                        }
                        loadNewSolution(manuallyPlacedBox, segmentNum, xPosition, yPosition);
                    }
                });
            }
        });
        alertDialog.show();
    }

    /**
     * Checks if manually placed box is out of segment bounds.
     * @param box
     * @param xPosition
     * @param yPosition
     * @return
     */
    public boolean boxIsOutOfBounds(Box box, int xPosition, int yPosition) {
        boolean outOfBoundsOnX = solution.getContainerWidth() < xPosition + box.getWidth();
        boolean outOfBoundsOnY = solution.getContainerHeight() < yPosition + box.getHeight();
        if (outOfBoundsOnX || outOfBoundsOnY) {
            return true;
        }
        return false;
    }

    /**
     * Checks if manually placed box occupies a taken space.
     */
    public boolean spaceIsAlreadyTaken(Box manuallyPlacedBox, int segmentNum, int xPosition, int yPosition) {
        Segment chosenSegment = solution.getSegmentList().get(segmentNum - 1);
        boolean isTaken = false;
        for (Box box : chosenSegment.getBoxList()) {
            if (box.isManuallyPlaced()) {
                if (boxesOverlap(manuallyPlacedBox, box, xPosition, yPosition)) {
                    isTaken = true;
                    break;
                }
            }
        }
        return isTaken;
    }

    /**
     * Checks if two placed boxes overlap
     * @param manuallyPlacedBox
     * @param segmentBox
     * @param xPosition
     * @param yPosition
     * @return
     */
    public boolean boxesOverlap(Box manuallyPlacedBox, Box segmentBox, int xPosition, int yPosition) {
        Coordinate bottomLeftBox1 = new Coordinate(xPosition, yPosition);
        Coordinate topRightBox1 = new Coordinate(xPosition + manuallyPlacedBox.getWidth(), yPosition + manuallyPlacedBox.getHeight());
        Coordinate bottomLeftBox2 = segmentBox.getBottomLeft();

        Coordinate topRightBox2 = new Coordinate(bottomLeftBox2.getX() + segmentBox.getWidth(), bottomLeftBox2.getY() + segmentBox.getHeight());
        // If one box is to the side of another, they do not overlap
        if (topRightBox1.getX() < bottomLeftBox2.getX() || topRightBox2.getX() < bottomLeftBox1.getX()) {
            return false;
        }
        // If one box is above another, they do not overlap
        if (topRightBox1.getY() < bottomLeftBox2.getY() || topRightBox2.getY() < bottomLeftBox1.getY()) {
            return false;
        }
        return true;
    }

    /**
     * Loads a new solution after manual rearrangement
     * @param manuallyPlacedBox
     * @param segmentNum
     * @param xPosition
     * @param yPosition
     */
    public void loadNewSolution(Box manuallyPlacedBox, int segmentNum, int xPosition, int yPosition) {
        solution.markAsUnpacked();

        if (manuallyPlacedBox != null) {
            manuallyPlacedBox.setManuallyPlaced(true);
            manuallyPlacedBox.setBottomLeft(new Coordinate(xPosition, yPosition));
            manuallyPlacedBox.setSegmentNum(segmentNum);
        }
        Intent intent = new Intent(this, SolutionLoadingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("solution", (Serializable) solution);
        intent.putExtra("Bundle", bundle);
        intent.putExtra("isRearrangedSolution", true);
        startActivity(intent);
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