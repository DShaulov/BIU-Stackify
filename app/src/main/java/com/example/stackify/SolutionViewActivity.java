package com.example.stackify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SolutionViewActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private AppDB db;
    private Button viewSwitchBtn;
    private DrawHelper drawHelper;
    private CorrectiveActionsHelper correctiveActionsHelper;
    private SolutionDao solutionDao;
    private Button optionsBtn;
    private Button correctionsBtn;
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
    private int offset;
    private int offset3D;
    private int minDimValue;
    private float x1, x2;
    static final int MIN_DISTANCE = 150;

    private boolean isInSegmentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_view);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "StackifyDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        solutionDao = db.solutionDao();

        isInSegmentView = false;
        optionsBtn = findViewById(R.id.optionsBtn);
        correctionsBtn = findViewById(R.id.correctionBtn);
        viewSwitchBtn = findViewById(R.id.viewSwitchBtn);
        segmentNumTextView = findViewById(R.id.segmentNumTextView);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getBundleExtra("Bundle");
        solution = (Solution) bundle.getSerializable("solution");
        containerHeight = solution.getContainerHeight();
        containerWidth = solution.getContainerWidth();
        bitmapHeight = solution.getContainerHeight();
        bitmapWidth = solution.getContainerWidth();
        segmentNum = 0;

        correctiveActionsHelper = new CorrectiveActionsHelper(this, solution);

        solImageView = findViewById(R.id.solImageView);

        // Set the offset to be a quarter of the segment length
        minDimValue = Math.min(containerHeight, containerWidth);
        // Edge case where there are no segments in the solution
        if (solution.getNumOfSegments() == 0) {
            offset = 0;
        }
        else {
            offset = (int) (float) (solution.getSegmentList().get(0).getLength() * 0.25);
        }
        offset3D = (int) (float) (offset * 0.5);

        bitmap = Bitmap.createBitmap(bitmapWidth + offset, bitmapHeight + offset, Bitmap.Config.ARGB_8888);
        solImageView.setImageBitmap(bitmap);
        canvas = new Canvas(bitmap);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchOptionsDialog();
            }
        });
        correctionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCorrectionsDialog();
            }
        });
        viewSwitchBtn.setOnClickListener(view -> {
            if (!isInSegmentView) {
                 isInSegmentView = true;
                drawBoxes(segmentNum);
                viewSwitchBtn.setText("3D View");
            }
            else {
                isInSegmentView = false;
                drawBoxes(segmentNum);
                viewSwitchBtn.setText("Segment View");
            }
        });

        drawHelper = new DrawHelper(canvas);
        updateSegmentNumTextView();
        drawBoxes(segmentNum);
    }

    public void drawBoxes(int segmentNum) {
        // Clear the bitmap of the canvas for a new drawing
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        // Edge case where there are no segments
        if (solution.getNumOfSegments() == 0) {
            return;
        }
        // Draw the 3d box first to avoid painting over
        if (isInSegmentView) {
            Segment segment = solution.getSegmentList().get(segmentNum);
            BoxSorter.sortByBottomLeftAscending(segment.getBoxList());
            for (Box box : segment.getBoxList()) {
                drawHelper.drawBoxDepth(box, containerHeight, offset, segment.getLength());
            }
            for (Box box : segment.getBoxList()) {
                drawHelper.drawBox(box, containerHeight, offset);
            }
        }
        else {
            int numOfSegments = solution.getNumOfSegments();
            float shrinkFactor = 1 - (float) (offset3D * numOfSegments) / (float) (minDimValue);
            for (int i = numOfSegments - 1; i >= segmentNum; i--) {
                int bottomLeftOffset = offset3D * i;
                Segment segment = solution.getSegmentList().get(i);
                BoxSorter.sortByBottomLeftAscending(segment.getBoxList());
                for (Box box : segment.getBoxList()) {
                    int shrunkHeight = (int) ((float) box.getHeight() * shrinkFactor);
                    int shrunkWidth = (int) ((float) box.getWidth() * shrinkFactor);
                    int shrunkXPos = (int) ((float) box.getBottomLeft() .getX() * shrinkFactor) + bottomLeftOffset;
                    int shrunkYPos = (int) ((float) box.getBottomLeft() .getY() * shrinkFactor) + bottomLeftOffset;
                    Box shrunkBox = new Box(box.getUnpackOrder(), shrunkHeight, shrunkWidth, box.getLength());
                    shrunkBox.setBottomLeft(new Coordinate(shrunkXPos, shrunkYPos));
                    drawHelper.drawBoxDepth(shrunkBox, containerHeight, offset3D, segment.getLength());
                }
                for (Box box : segment.getBoxList()) {
                    int shrunkHeight = (int) ((float) box.getHeight() * shrinkFactor);
                    int shrunkWidth = (int) ((float) box.getWidth() * shrinkFactor);
                    int shrunkXPos = (int) ((float) box.getBottomLeft() .getX() * shrinkFactor) + bottomLeftOffset;
                    int shrunkYPos = (int) ((float) box.getBottomLeft() .getY() * shrinkFactor) + bottomLeftOffset;
                    Box shrunkBox = new Box(box.getUnpackOrder(), shrunkHeight, shrunkWidth, box.getLength());
                    shrunkBox.setBottomLeft(new Coordinate(shrunkXPos, shrunkYPos));
                    drawHelper.drawBox3D(shrunkBox, containerHeight, offset3D, shrinkFactor);
                }
            }
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
            }
        });
        alertDialog.show();
    }

    private void launchCorrectionsDialog() {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(this);
        dialogBuilder.setView(R.layout.dialog_solution_corrections);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button moveBoxBtn = alertDialog.findViewById(R.id.moveBoxBt);
                Button freeBoxBtn = alertDialog.findViewById(R.id.freeBoxBtn);
                Button addBoxBtn = alertDialog.findViewById(R.id.addBoxBtn);
                Button removeBoxBtn = alertDialog.findViewById(R.id.removeBoxBtn);

                moveBoxBtn.setOnClickListener(view -> {CorrectiveActionsHelper.launchMoveBoxDialog();});
                freeBoxBtn.setOnClickListener(view -> {CorrectiveActionsHelper.launchFreeBoxDialog();});
                addBoxBtn.setOnClickListener(view -> {CorrectiveActionsHelper.launchAddBoxDialog();});
                removeBoxBtn.setOnClickListener(view -> {CorrectiveActionsHelper.launchRemoveBoxDialog();});
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
                            Toast.makeText(SolutionViewActivity.this, "Name Cannot Be Empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!stringIsAlphabetical(solutionName)) {
                            Toast.makeText(SolutionViewActivity.this, "Name Can Contain Only Letters.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LocalDate date = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String formattedDate = date.format(formatter);
                        solution.setDate(formattedDate);
                        solution.setSolutionName(solutionName);
                        solutionDao.insert(solution);
                        FirebaseUser user = auth.getCurrentUser();
                        saveSolutionToCloud(user.getUid(), solution);
                        alertDialog.cancel();
                    }
                });
            }
        });
        alertDialog.show();
    }

    public boolean stringIsAlphabetical(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c) && !Character.isSpaceChar(c)) {
                return false;
            }
        }
        return true;
    }

    public void saveSolutionToCloud(String userId, Solution solution) {
        Gson gson = new Gson();
        String solAsJson = gson.toJson(solution);
        databaseReference.child("users").child(userId).child("solutions").child(solution.getSolutionName()).setValue(solAsJson);
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
                if (unpackedBoxesString.equals("")) {
                    unpackedBoxesNumberTextView.setText("None");
                }
                else {
                    unpackedBoxesNumberTextView.setText(unpackedBoxesString.substring(0, unpackedBoxesString.length() - 2));
                }
            }
        });
        alertDialog.show();
    }

    public void updateSegmentNumTextView() {
        // Edge case where there are no segments
        String segmentNumString = "";
        if (solution.getNumOfBoxesInSolution() == 0) {
            segmentNumString = "No Boxes Packed";
        }
        else {
            segmentNumString = String.format("Segment %d of %d", segmentNum + 1, solution.getNumOfSegments());
        }
        segmentNumTextView.setText(segmentNumString);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Case of left to right swipe
                    if (x2 > x1) {
                        showPrevSegment();
                    }
                    // Case of right to left swipe
                    else {
                        showNextSegment();
                    }
                }
                else if (Math.abs(deltaX) < MIN_DISTANCE) {
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}