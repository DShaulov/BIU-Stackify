package com.example.stackify;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

/**
 * A helper class containing methods for making manual changes to a solution
 */
public class CorrectiveActionsHelper {
    private static Context context;
    private static Solution solution;

    public CorrectiveActionsHelper(Context context, Solution solution) {
        this.context = context;
        this.solution = solution;
    }

    /**
     * Launches a dialog asking the user which box to pin
     */
    public static void launchMoveBoxDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(R.layout.dialog_move_box);
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

                enterSegmentNumEditText.setHint("Segment Number (1-" + solution.getNumOfSegments() + ")");
                enterHorizontalPosEditText.setHint("X Position (0-" + solution.getContainerWidth() + ")");
                enterVerticalPosEditText.setHint("Y Position (0-" + solution.getContainerHeight() + ")");

                rearrangeOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String boxNumString = enterBoxNumEditText.getText().toString();
                        String segmentNumString = enterSegmentNumEditText.getText().toString();
                        String xPositionString = enterHorizontalPosEditText.getText().toString();
                        String yPositionString = enterVerticalPosEditText.getText().toString();
                        if (boxNumString.isEmpty() || segmentNumString.isEmpty() || xPositionString.isEmpty() || yPositionString.isEmpty()) {
                            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Integer boxNum = Integer.parseInt(enterBoxNumEditText.getText().toString());
                        Integer segmentNum = Integer.parseInt(enterSegmentNumEditText.getText().toString());
                        Integer xPosition = Integer.parseInt(enterHorizontalPosEditText.getText().toString());
                        Integer yPosition = Integer.parseInt(enterVerticalPosEditText.getText().toString());

                        if (!solution.boxExists(boxNum)) {
                            Toast.makeText(context, "Box number " + boxNum + " does not exist", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (solution.getSegmentList().size() < segmentNum) {
                            Toast.makeText(context, "Segment Number must be (1-" + solution.getNumOfSegments() + ")", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (solution.getContainerWidth() < xPosition) {
                            Toast.makeText(context, "X Position must be (0-" + solution.getContainerWidth() + ")", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (solution.getContainerHeight() < yPosition) {
                            Toast.makeText(context, "Y Position must be (0-" + solution.getContainerHeight() + ")", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(context, "Box is out of bounds", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (spaceIsAlreadyTaken(manuallyPlacedBox, segmentNum, xPosition, yPosition)) {
                            Toast.makeText(context, "Space is already taken by another box", Toast.LENGTH_LONG).show();
                            return;
                        }
                        loadSolutionWithPinnedBox(manuallyPlacedBox, segmentNum, xPosition, yPosition);
                    }
                });
            }
        });
        alertDialog.show();
    }

    /**
     * Launches a dialog asking the user to provide a box number to unpin
     */
    public static void launchFreeBoxDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(R.layout.dialog_free_box);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                EditText freeBoxNumEditText = alertDialog.findViewById(R.id.freeBoxNumEditText);
                Button freeBoxOkBtn = alertDialog.findViewById(R.id.freeBoxOkBtn);

                freeBoxOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String boxNumString = freeBoxNumEditText.getText().toString();
                        if (boxNumString.isEmpty()) {
                            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Integer boxNum = Integer.parseInt(freeBoxNumEditText.getText().toString());
                        if (!solution.boxExists(boxNum)) {
                            Toast.makeText(context, "Box number " + boxNum + " does not exist.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Box freedBox = solution.getBoxByUnpackOrder(boxNum);
                        if (!freedBox.isManuallyPlaced()) {
                            Toast.makeText(context, "Box number " + boxNum + " is not pinned.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        loadSolutionWithFreedBox(freedBox);
                    }
                });
            }
        });
        alertDialog.show();
    }

    /**
     * Launches a dialog asking the user to input information for a new box
     */
    public static void launchAddBoxDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(R.layout.dialog_add_box);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                EditText unpackOrderEditText = alertDialog.findViewById(R.id.unpackOrderEditText);
                EditText heightEditText = alertDialog.findViewById(R.id.heightEditText);
                EditText widthEditText = alertDialog.findViewById(R.id.widthEditText);
                EditText lengthEditText = alertDialog.findViewById(R.id.lengthEditText);
                Button addBoxOkBtn = alertDialog.findViewById(R.id.addBoxOkBtn);

                addBoxOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String boxNumString = unpackOrderEditText.getText().toString();
                        String boxHeightString = heightEditText.getText().toString();
                        String boxWidthString = widthEditText.getText().toString();
                        String boxLengthString = lengthEditText.getText().toString();

                        if (boxNumString.isEmpty() || boxHeightString.isEmpty() || boxWidthString.isEmpty() || boxLengthString.isEmpty()) {
                            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Integer boxNum = Integer.parseInt(unpackOrderEditText.getText().toString());
                        Integer boxHeight = Integer.parseInt(heightEditText.getText().toString());
                        Integer boxWidth = Integer.parseInt(widthEditText.getText().toString());
                        Integer boxLength = Integer.parseInt(lengthEditText.getText().toString());

                        if (solution.boxExists(boxNum)) {
                            Toast.makeText(context, "Box with unpack order " + boxNum + "already exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Box newBox = new Box(boxNum, boxHeight, boxWidth, boxLength);
                        loadSolutionWithNewBox(newBox);
                    }
                });
            }
        });
        alertDialog.show();
    }

    /**
     * Launches a dialog asking the user to provide the number of the box to change
     */
    public static void launchChangeBoxDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(R.layout.dialog_change_box);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                EditText changeBoxNumEditText = alertDialog.findViewById(R.id.changeBoxNumEditText);
                Button changeBoxOkBtn = alertDialog.findViewById(R.id.changeBoxOkBtn);

                changeBoxOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String boxNumString = changeBoxNumEditText.getText().toString();
                        if (boxNumString.isEmpty()) {
                            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Integer boxNum = Integer.parseInt(changeBoxNumEditText.getText().toString());
                        if (!solution.boxExists(boxNum)) {
                            Toast.makeText(context, "Box number " + boxNum + " does not exist", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Box boxToChange = solution.getBoxByUnpackOrder(boxNum);
                        launchChangeBoxDimDialog(boxToChange);
                    }
                });
            }
        });
        alertDialog.show();
    }

    /**
     * Launches a dialog for changing box dimensions
     */
    public static void launchChangeBoxDimDialog(Box boxToChange) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(R.layout.dialog_change_box_dimensions);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                EditText unpackOrderEditText = alertDialog.findViewById(R.id.unpackOrderEditText);
                EditText heightEditText = alertDialog.findViewById(R.id.heightEditText);
                EditText widthEditText = alertDialog.findViewById(R.id.widthEditText);
                EditText lengthEditText = alertDialog.findViewById(R.id.lengthEditText);

                unpackOrderEditText.setHint("Unpack Order (" + boxToChange.getUnpackOrder() + ")");
                heightEditText.setHint("Height (" + boxToChange.getHeight() + ")");
                widthEditText.setHint("Width (" + boxToChange.getWidth() + ")");
                lengthEditText.setHint("Length (" + boxToChange.getLength() + ")");

                Button changeBoxOkBtn = alertDialog.findViewById(R.id.changeBoxOkBtn);

                changeBoxOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String boxNumString = unpackOrderEditText.getText().toString();
                        String boxHeightString = heightEditText.getText().toString();
                        String boxWidthString = widthEditText.getText().toString();
                        String boxLengthString = lengthEditText.getText().toString();

                        if (boxNumString.isEmpty() || boxHeightString.isEmpty() || boxWidthString.isEmpty() || boxLengthString.isEmpty()) {
                            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Integer boxNum = Integer.parseInt(unpackOrderEditText.getText().toString());
                        Integer boxHeight = Integer.parseInt(heightEditText.getText().toString());
                        Integer boxWidth = Integer.parseInt(widthEditText.getText().toString());
                        Integer boxLength = Integer.parseInt(lengthEditText.getText().toString());

                        if (solution.boxExists(boxNum) && boxNum != boxToChange.getUnpackOrder()) {
                            Toast.makeText(context, "Box with unpack order " + boxNum + "already exists", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        boxToChange.setUnpackOrder(boxNum);
                        boxToChange.setHeight(boxHeight);
                        boxToChange.setWidth(boxWidth);
                        boxToChange.setLength(boxLength);
                        reloadSolution();
                    }
                });
            }
        });
        alertDialog.show();
    }

    /**
     * Launches a dialog asking the user which box to remove
     */
    public static void launchRemoveBoxDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(R.layout.dialog_remove_box);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                EditText removeBoxEditText = alertDialog.findViewById(R.id.removeBoxNumEditText);
                Button removeBoxOkBtn = alertDialog.findViewById(R.id.removeBoxOkBtn);

                removeBoxOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String boxNumString = removeBoxEditText.getText().toString();
                        if (boxNumString.isEmpty()) {
                            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Integer boxNum = Integer.parseInt(removeBoxEditText.getText().toString());
                        if (!solution.boxExists(boxNum)) {
                            Toast.makeText(context, "Box number " + boxNum + " does not exist", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Box removedBox = solution.getBoxByUnpackOrder(boxNum);
                        loadSolutionWithRemovedBox(removedBox);
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
    public static boolean boxIsOutOfBounds(Box box, int xPosition, int yPosition) {
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
    public static boolean spaceIsAlreadyTaken(Box manuallyPlacedBox, int segmentNum, int xPosition, int yPosition) {
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
    public static boolean boxesOverlap(Box manuallyPlacedBox, Box segmentBox, int xPosition, int yPosition) {
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
     * Reloads a solution after alterations to existing boxes/container/type
     */
    public static void reloadSolution() {
        solution.markAsUnpacked();
        Intent intent = new Intent(context, SolutionLoadingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("boxList", (Serializable) solution.getBoxList());
        intent.putExtra("Bundle", bundle);
        intent.putExtra("isOrdered", solution.isOrdered());
        intent.putExtra("containerHeight", solution.getContainerHeight());
        intent.putExtra("containerWidth", solution.getContainerWidth());
        intent.putExtra("containerLength", solution.getContainerLength());
        context.startActivity(intent);
    }

    /**
     * Loads a new solution after pinning a box
     * @param manuallyPlacedBox
     * @param segmentNum
     * @param xPosition
     * @param yPosition
     */
    public static void loadSolutionWithPinnedBox(Box manuallyPlacedBox, int segmentNum, int xPosition, int yPosition) {
        solution.markAsUnpacked();

        if (manuallyPlacedBox != null) {
            manuallyPlacedBox.setManuallyPlaced(true);
            manuallyPlacedBox.setBottomLeft(new Coordinate(xPosition, yPosition));
            // Because segments are displayed in reverse - the segment number is also reverse.
            int actualSegmentNum = solution.getNumOfSegments() + 1 - segmentNum;
            manuallyPlacedBox.setSegmentNum(actualSegmentNum);
        }
        Intent intent = new Intent(context, SolutionLoadingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("solution", (Serializable) solution);
        intent.putExtra("Bundle", bundle);
        intent.putExtra("isRearrangedSolution", true);
        context.startActivity(intent);
    }

    /**
     * Loads a new solution after unpinning a box
     * @param freeBoxed
     */
    public static void loadSolutionWithFreedBox(Box freeBoxed) {
        solution.markAsUnpacked();
        freeBoxed.setManuallyPlaced(false);
        Intent intent = new Intent(context, SolutionLoadingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("solution", (Serializable) solution);
        intent.putExtra("Bundle", bundle);
        intent.putExtra("isRearrangedSolution", true);
        context.startActivity(intent);
    }

    /**
     * Loads a new solution after removing a box
     * @param removedBox
     */
    public static void loadSolutionWithRemovedBox(Box removedBox) {
        solution.markAsUnpacked();
        solution.removeBoxByUnpackOrder(removedBox.getUnpackOrder());
        Intent intent = new Intent(context, SolutionLoadingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("solution", (Serializable) solution);
        intent.putExtra("Bundle", bundle);
        intent.putExtra("isRearrangedSolution", true);
        context.startActivity(intent);
    }

    /**
     * Loads a new solution with a new box
     * @param newBox
     */
    public static void loadSolutionWithNewBox(Box newBox) {
        solution.markAsUnpacked();
        solution.getBoxList().add(newBox);
        Intent intent = new Intent(context, SolutionLoadingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("solution", (Serializable) solution);
        intent.putExtra("Bundle", bundle);
        intent.putExtra("isRearrangedSolution", true);
        context.startActivity(intent);
    }
}
