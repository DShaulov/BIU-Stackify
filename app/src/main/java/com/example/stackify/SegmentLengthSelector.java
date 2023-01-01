package com.example.stackify;

import java.util.ArrayList;

/**
 * Contains several algorithms for picking segment lengths
 */
public class SegmentLengthSelector {
    /**
     *  Selects the box that has the largest size (among all other boxes) of its smallest dimension (smallest of: width, height, length)
     *  Selects the segment length to be the max dimension of that box
     * @param boxList
     * @return segment length
     */
    public static int maxMinDim(ArrayList<Box> boxList) {
        // Find the box with largest smallest dimension
        Box smallestLargestBox = boxList.get(0);
        int largestDim = 0;
        for (Box box : boxList) {
            int boxMinDim = box.getMinDim();
            if (boxMinDim > largestDim) {
                largestDim = boxMinDim;
                smallestLargestBox = box;
            }
        }
        return smallestLargestBox.getMaxDim();
    }
}
