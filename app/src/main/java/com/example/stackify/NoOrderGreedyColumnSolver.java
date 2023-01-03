package com.example.stackify;

import java.util.ArrayList;

public class NoOrderGreedyColumnSolver implements Solver{
    private ArrayList<Box> boxList;
    private int containerHeight;
    private int containerWidth;
    private int containerLength;
    private int remainingContainerLength;
    private int totalBoxes;
    private Solution solution;

    public NoOrderGreedyColumnSolver(ArrayList<Box> boxList, int containerHeight, int containerWidth, int containerLength) {
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
        }
        // Sort all boxes by width
        BoxSorter.sortByWidth(boxList);
        int boxIndex = 0;
        int xPosition = 0;
        int yPosition = 0;
        while (containerCanFitAnotherSegment(segmentLen, boxIndex)) {
            Segment segment = new Segment(containerHeight, containerWidth, segmentLen);
            while (segmentCanFitAnotherColumn(xPosition, boxIndex)) {
                int columnWidth = boxList.get(boxIndex).getWidth();
                while (columnCanFitAnotherBox(yPosition, boxIndex)) {
                    Box box = boxList.get(boxIndex);
                    box.setBottomLeft(new Coordinate(xPosition, yPosition));
                    segment.addBox(box);
                    yPosition += box.getHeight();
                    boxIndex += 1;
                }
                xPosition += columnWidth;
                yPosition = 0;
            }
            solution.addSegment(segment);
            remainingContainerLength -= segmentLen;
        }
    }

    public boolean thereAreBoxesRemaining(int boxIndex) {
        if (boxIndex < totalBoxes) {
            return true;
        }
        return false;
    }

    public boolean containerCanFitAnotherSegment(int segmentLen, int boxIndex) {
        if (!thereAreBoxesRemaining(boxIndex)) {
            return false;
        }
        if (segmentLen <= remainingContainerLength ) {
            return true;
        }
        return false;
    }

    public boolean segmentCanFitAnotherColumn(int xPosition, int boxIndex) {
        if (!thereAreBoxesRemaining(boxIndex)) {
            return false;
        }
        if (boxList.get(boxIndex).getWidth() + xPosition <= containerWidth) {
           return true;
        }
        return false;
    }

    public boolean columnCanFitAnotherBox(int yPosition, int boxIndex) {
        if (!thereAreBoxesRemaining(boxIndex)) {
            return false;
        }
        if (boxList.get(boxIndex).getHeight() + yPosition <= containerHeight) {
            return true;
        }
        return false;
    }

    @Override
    public Solution getSolution() {
        return solution;
    }
}
