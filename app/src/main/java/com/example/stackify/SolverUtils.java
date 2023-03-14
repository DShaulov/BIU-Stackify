package com.example.stackify;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Contains utility methods for classes that implement the Solver interface
 */
public class SolverUtils {
    /**
     * Removes boxes that are too large to fit inside a segment.
     * @param boxList
     * @param containerHeight
     * @param containerWidth
     */
    public static void discardTooLarge(ArrayList<Box> boxList, int containerHeight, int containerWidth) {
        Iterator<Box> iterator = boxList.iterator();
        while (iterator.hasNext()){
            Box box = iterator.next();
            if (containerHeight < box.getHeight() || containerWidth < box.getWidth()) {
                iterator.remove();
            }
        }
    }

    public static boolean thereAreBoxesRemaining(int boxIndex, int totalBoxes) {
        if (boxIndex < totalBoxes) {
            return true;
        }
        return false;
    }

    public static boolean containerCanFitAnotherSegment(int boxIndex, int totalBoxes, int segmentLen, int remainingContainerLength) {
        if (!thereAreBoxesRemaining(boxIndex, totalBoxes)) {
            return false;
        }
        if (segmentLen <= remainingContainerLength ) {
            return true;
        }
        return false;
    }

    public static boolean segmentCanFitAnotherColumn(ArrayList<Box> boxList, int containerWidth ,int xPosition, int boxIndex, int totalBoxes) {
        if (!thereAreBoxesRemaining(boxIndex, totalBoxes)) {
            return false;
        }
        if (boxList.get(boxIndex).getWidth() + xPosition <= containerWidth) {
            return true;
        }
        return false;
    }

    public static boolean columnCanFitAnotherBox(ArrayList<Box> boxList, int containerHeight, int yPosition, int boxIndex, int totalBoxes) {
        if (!thereAreBoxesRemaining(boxIndex, totalBoxes)) {
            return false;
        }
        if (boxList.get(boxIndex).getHeight() + yPosition <= containerHeight) {
            return true;
        }
        return false;
    }


    /**
     * Checks if a box is out of bounds of a container at given position. (According to top-left coordinate)
     * @param box
     * @param containerHeight
     * @param containerWidth
     * @param xPosition
     * @param yPosition
     * @return
     */
    public static boolean boxOutOfBounds(Box box, int containerHeight, int containerWidth, int xPosition, int yPosition) {
        if (box.getHeight() + yPosition > containerHeight || box.getWidth() + xPosition > containerWidth) {
            return true;
        }
        return false;
    }
    /**
     * Checks if the bottom right and top left spaces are occupied
     * @param box
     * @param spaceMatrix
     * @param xPosition
     * @param yPosition
     * @return
     */
    public static boolean cornersAreOccupied(Box box ,boolean[][] spaceMatrix, int xPosition, int yPosition) {
        if (spaceMatrix[yPosition][xPosition] || spaceMatrix[yPosition + box.getHeight()][xPosition + box.getWidth()]) {
            return true;
        }
        return false;
    }


    /**
     * Checks if a rectangular space is unoccupied by another rectangle.
     * @param box
     * @param spaceMatrix
     * @param xPosition
     * @param yPosition
     * @return
     */
    public static boolean rectangleSpaceIsFree(Box box, boolean[][] spaceMatrix, int xPosition, int yPosition) {
        boolean isFree = true;
        for (int i = yPosition; i < yPosition + box.getHeight(); i++) {
            for (int j = xPosition; j < xPosition + box.getWidth(); j++) {
                if (spaceMatrix[i][j]) {
                    isFree = false;
                    break;
                }
            }
            if (!isFree) {
                break;
            }
        }
        return isFree;
    }

    /**
     * Marks space as occupied in the space matrix
     * @param box
     * @param spaceMatrix
     * @param xPosition
     * @param yPosition
     */
    public static void markSpaceAsOccupied(Box box,boolean[][] spaceMatrix, int xPosition, int yPosition) {
        for (int i = yPosition; i < yPosition + box.getHeight(); i++) {
            for (int j = xPosition; j < xPosition + box.getWidth(); j++) {
                spaceMatrix[i][j] = true;
            }
        }
    }
}
