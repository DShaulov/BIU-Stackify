package com.example.stackify;

import java.util.ArrayList;

public class UnorderedScannerSolver implements Solver{
    private ArrayList<Box> boxList;
    private int containerHeight;
    private int containerWidth;
    private int containerLength;
    private int remainingContainerLength;
    private int totalBoxes;
    private Solution solution;

    public UnorderedScannerSolver(ArrayList<Box> boxList, int containerHeight, int containerWidth, int containerLength){
        this.boxList = boxList;
        this.containerHeight = containerHeight;
        this.containerWidth = containerWidth;
        this.containerLength = containerLength;
        this.remainingContainerLength = containerLength;
        this.totalBoxes = boxList.size();
        this.solution = new Solution(containerHeight, containerWidth, containerLength, boxList.size());
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
        BoxSorter.sortByWidth(boxList);
        int boxIndex = 0;

        boolean[][] spaceMatrix = new boolean[containerHeight][containerWidth];
        while (SolverUtils.containerCanFitAnotherSegment(boxIndex, totalBoxes, segmentLen, remainingContainerLength)) {
            Segment segment = new Segment(containerHeight, containerWidth, segmentLen);
            Box currentBox = boxList.get(boxIndex);
            // Go over every pixel and look for open space
            for (int yPosition = 0; yPosition < containerHeight; yPosition++) {
                for (int xPosition = 0; xPosition < containerWidth; xPosition++) {
                    // If box will be out of bounds, continue
                    if (SolverUtils.boxOutOfBounds(currentBox, containerHeight, containerWidth, xPosition, yPosition)) {
                        continue;
                    }
                    if(!SolverUtils.cornersAreOccupied(currentBox, spaceMatrix, xPosition, yPosition)) {

                    }
                }
            }
            solution.addSegment(segment);
            remainingContainerLength -= segmentLen;
        }

    }

    @Override
    public Solution getSolution() {
        return solution;
    }
}