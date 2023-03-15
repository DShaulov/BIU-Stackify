package com.example.stackify;

import java.util.ArrayList;

public class OrderedScannerSolver implements Solver {
    private ArrayList<Box> boxList;
    private int containerHeight;
    private int containerWidth;
    private int containerLength;
    private int remainingContainerLength;
    private int totalBoxes;
    private Solution solution;

    public OrderedScannerSolver(ArrayList<Box> boxList, int containerHeight, int containerWidth, int containerLength){
        this.boxList = boxList;
        this.containerHeight = containerHeight;
        this.containerWidth = containerWidth;
        this.containerLength = containerLength;
        this.remainingContainerLength = containerLength;
        this.totalBoxes = boxList.size();
        this.solution = new Solution(boxList, containerHeight, containerWidth, containerLength, boxList.size());
    }

    @Override
    public void solve() {
        // Calculate segment length and rotate boxes to fill as much of the depth as possible
        int segmentLen = SegmentLengthSelector.minVarianceDim(boxList).getDimValue();
        for (Box box : boxList) {
            box.rotateToClosestDim(segmentLen);
            box.rotateToMaxWidth();
        }
        SolverUtils.discardTooLarge(boxList, containerHeight, containerWidth);
        // Sort all boxes by unpack order
        BoxSorter.sortByUnpackOrder(boxList);
        int boxIndex = 0;

        while (SolverUtils.containerCanFitAnotherSegment(boxIndex, totalBoxes, segmentLen, remainingContainerLength)) {
            boolean[][] spaceMatrix = new boolean[containerHeight + 1][containerWidth + 1];
            Segment segment = new Segment(containerHeight, containerWidth, segmentLen);

            boolean segmentHasRoom = true;
            while (segmentHasRoom) {
                Box currentBox = boxList.get(boxIndex);
                // Go over every pixel and look for open space
                boolean foundSpace = false;
                for (int yPosition = 0; yPosition < containerHeight; yPosition++) {
                    for (int xPosition = 0; xPosition < containerWidth; xPosition++) {
                        // If box will be out of bounds, continue
                        if (SolverUtils.boxOutOfBounds(currentBox, containerHeight, containerWidth, xPosition, yPosition)) {
                            continue;
                        }
                        if(!SolverUtils.cornersAreOccupied(currentBox, spaceMatrix, xPosition, yPosition)) {
                            if (SolverUtils.rectangleSpaceIsFree(currentBox, spaceMatrix, xPosition, yPosition)) {
                                currentBox.setBottomLeft(new Coordinate(xPosition, yPosition));
                                segment.addBox(currentBox);
                                SolverUtils.markSpaceAsOccupied(currentBox, spaceMatrix, xPosition, yPosition);
                                boxIndex += 1;
                                currentBox = boxList.get(boxIndex);
                                foundSpace = true;
                            }
                        }
                        // If box already placed, continue to next iteration
                        if (foundSpace) {
                            break;
                        }
                    }
                    // If box already placed, continue to next iteration
                    if (foundSpace) {
                        break;
                    }
                }
                if (!foundSpace) {
                    segmentHasRoom = false;
                    solution.addSegment(segment);
                    remainingContainerLength -= segmentLen;
                }
            }
        }

    }

    @Override
    public Solution getSolution() {
        return solution;
    }
}
