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
}
