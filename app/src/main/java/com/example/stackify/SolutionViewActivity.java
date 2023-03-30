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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SolutionViewActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private AppDB db;
    private SolutionViewDrawHelper drawHelper;
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
    int offset;

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
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getBundleExtra("Bundle");
        solution = (Solution) bundle.getSerializable("solution");
        containerHeight = solution.getContainerHeight();
        containerWidth = solution.getContainerWidth();
        bitmapHeight = solution.getContainerHeight();
        bitmapWidth = solution.getContainerWidth();
        segmentNum = 0;

        solImageView = findViewById(R.id.solImageView);
        offset = 50;
        bitmap = Bitmap.createBitmap(bitmapWidth + offset, bitmapHeight + offset, Bitmap.Config.ARGB_8888);
        solImageView.setImageBitmap(bitmap);
        canvas = new Canvas(bitmap);

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

        drawHelper = new SolutionViewDrawHelper(canvas);
        updateSegmentNumTextView();
        drawBoxes(segmentNum);
    }

    public void drawBoxes(int segmentNum) {
        // Clear the bitmap of the canvas for a new drawing
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Segment segment = solution.getSegmentList().get(segmentNum);
        // Draw the 3d box first to avoid painting over
        BoxSorter.sortByBottomLeftAscending(segment.getBoxList());
        for (Box box : segment.getBoxList()) {
            drawHelper.drawBox3D(box, containerHeight, offset);
        }
        for (Box box : segment.getBoxList()) {
            drawHelper.drawBox(box, containerHeight, offset);
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
                            Toast.makeText(SolutionViewActivity.this, "Name Cannot Be Empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!stringIsAlphabetical(solutionName)) {
                            Toast.makeText(SolutionViewActivity.this, "Name Can Contain Only Letters.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        solution.setDate(LocalDate.now());
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
            if(!Character.isLetter(c)) {
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