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

    public NoOrderGreedyColumnSolver(ArrayList<Box> boxList, int containerHeight, int containerWidth, int containerLength, int remainingContainerLength) {
        this.boxList = boxList;
        this.containerHeight = containerHeight;
        this.containerWidth = containerWidth;
        this.containerLength = containerLength;
        this.remainingContainerLength = remainingContainerLength;
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
        int boxesPacked = 0;
        while ((remainingContainerLength > segmentLen) && (true)) {

        }
    }

    public boolean thereAreBoxesRemaining(int boxesPacked) {
        if (boxesPacked < totalBoxes) {
            return true;
        }
        return false;
    }

    @Override
    public Solution getSolution() {
        return null;
    }
}
