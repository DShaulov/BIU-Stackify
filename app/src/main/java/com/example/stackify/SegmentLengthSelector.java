package com.example.stackify;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Contains algorithms for picking segment lengths
 */
public class SegmentLengthSelector {
    /**
     * Calculates the variance of a list of integers.
     * @param numList
     * @return the variance.
     */
    public static double getVariance(ArrayList<Integer> numList) {
        double sum = 0;
        double squaredSum = 0;
        for (Integer num : numList) {
            sum += num;
            squaredSum += (num * num);
        }
        int n = numList.size();
        double mean = sum / n;
        double variance = (squaredSum / n) - (mean * mean);
        return variance;
    }

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

    /**
     * Finds the dimension that has the least variance, and sets the segment length to be the maximum value in that dimension
     * @param boxList
     * @return segment length
     */
    public static int minVarianceDim(ArrayList<Box> boxList) {
        ArrayList<Integer> heightList = new ArrayList<>();
        ArrayList<Integer> widthList = new ArrayList<>();
        ArrayList<Integer> lengthList = new ArrayList<>();

        for (Box box : boxList) {
            heightList.add(box.getHeight());
            widthList.add(box.getWidth());
            lengthList.add(box.getLength());
        }

        double heightVariance = getVariance(heightList);
        double widthVariance = getVariance(widthList);
        double lengthVariance = getVariance(lengthList);

        double minVariance = Math.min(heightVariance, Math.min(widthVariance, lengthVariance));
        if (minVariance == heightVariance) {
            return Collections.max(heightList);
        }
        else if (minVariance == widthVariance) {
            return Collections.max(widthList);
        }
        else {
            return Collections.max(lengthList);
        }
    }
}
