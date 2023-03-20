package com.example.stackify;

import java.util.ArrayList;
import java.util.List;

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
        this.solution = new Solution(boxList, containerHeight, containerWidth, containerLength, boxList.size());
        this.solution.setOrdered(false);
    }

    @Override
    public void solve() {
        // Calculate segment length
        int segmentLen = SegmentLengthSelector.minVarianceDim(boxList).getDimValue();

        // Create a space matrix for each segment
        int numOfSegments = containerLength / segmentLen;
        List<Segment> segmentList = new ArrayList<>();
        List<boolean[][]> spaceMatrixList = new ArrayList<>();
        for (int i = 0; i < numOfSegments; i++) {
            segmentList.add(new Segment(containerHeight, containerWidth, segmentLen));
            spaceMatrixList.add(new boolean[containerHeight + 1][containerWidth + 1]);
        }

        // Place manually placed boxes and rotate boxes to fill as much of segment space as possible
        for (Box box : boxList) {
            box.rotateToClosestDim(segmentLen);
            box.rotateToMaxWidth();
            if (box.isManuallyPlaced()) {
                int segmentNum = box.getSegmentNum();
                Segment boxSegment = segmentList.get(segmentNum - 1);
                boolean[][] boxSpaceMatrix = spaceMatrixList.get(segmentNum - 1);

                boxSegment.addBox(box);
                SolverUtils.markSpaceAsOccupied(box, boxSpaceMatrix, box.getBottomLeft().getX(), box.getBottomLeft().getY());
            }
        }

        // Remove boxes too large to fit
        SolverUtils.discardTooLarge(boxList, containerHeight, containerWidth);

        // Sort all boxes by area - ascending
        BoxSorter.sortByAreaAscending(boxList);
        int boxIndex = 0;

        for (int i = 0; i < numOfSegments; i++) {
            boolean[][] spaceMatrix = spaceMatrixList.get(i);
            Segment segment = segmentList.get(i);

            boolean segmentHasRoom = true;
            while (segmentHasRoom) {
                Box currentBox = boxList.get(boxIndex);
                // If box is manually placed or is too large to fit, continue
                if (currentBox.isManuallyPlaced() || currentBox.isTooLarge()) {
                    boxIndex += 1;
                    continue;
                }
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
        solution.markAsPacked();
    }

    @Override
    public Solution getSolution() {
        return solution;
    }
}
