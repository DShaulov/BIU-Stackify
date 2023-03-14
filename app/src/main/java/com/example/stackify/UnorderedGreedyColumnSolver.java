package com.example.stackify;

import java.util.ArrayList;


/**
 * Computes a solution by dividing the segment into columns and packing each column as high as possible
 * Boxes are sorted by width
 */
public class UnorderedGreedyColumnSolver implements Solver{
    private ArrayList<Box> boxList;
    private int containerHeight;
    private int containerWidth;
    private int containerLength;
    private int remainingContainerLength;
    private int totalBoxes;
    private Solution solution;

    public UnorderedGreedyColumnSolver(ArrayList<Box> boxList, int containerHeight, int containerWidth, int containerLength) {
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
        // Sort all boxes by width
        BoxSorter.sortByWidth(boxList);
        int boxIndex = 0;
        int xPosition = 0;
        int yPosition = 0;
        while (SolverUtils.containerCanFitAnotherSegment(boxIndex, totalBoxes, segmentLen, remainingContainerLength)) {
            Segment segment = new Segment(containerHeight, containerWidth, segmentLen);
            while (SolverUtils.segmentCanFitAnotherColumn(boxList, containerWidth, xPosition, boxIndex, totalBoxes)) {
                int columnWidth = boxList.get(boxIndex).getWidth();
                while (SolverUtils.columnCanFitAnotherBox(boxList, containerHeight,yPosition, boxIndex, totalBoxes)) {
                    Box box = boxList.get(boxIndex);
                    box.setBottomLeft(new Coordinate(xPosition, yPosition));
                    segment.addBox(box);
                    yPosition += box.getHeight();
                    boxIndex += 1;
                }
                xPosition += columnWidth;
                yPosition = 0;
            }
            xPosition = 0;
            solution.addSegment(segment);
            remainingContainerLength -= segmentLen;
        }
    }

    @Override
    public Solution getSolution() {
        return solution;
    }
}
