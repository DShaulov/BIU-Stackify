package com.example.stackify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        // Edge case where there are no boxes in solution
        if (boxList.size() == 0) {
            return 200;
        }
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
            return new DimNameValuePair("Height", Collections.max(heightList)).getDimValue();

        }
        else if (minVariance == widthVariance) {
            return new DimNameValuePair("Width", Collections.max(widthList)).getDimValue();
        }
        else {
            return new DimNameValuePair("Length", Collections.max(lengthList)).getDimValue();
        }
    }

    /**
     * Checks all multiples of 100 until the container length, to find the one with the minimal sum of box dimension distances to it
     * @return
     */
    public static int closestSumSegmentLength(ArrayList<Box> boxList, int containerLength) {
        // If the container length is not a multiple of 100, use minVarianceDim
        if (containerLength % 100 != 0) {
            return minVarianceDim(boxList);
        }
        int smallestDistanceSum = 40000;
        // Arbitrarily large number
        int bestSegmentLen = 100;
        for (int i = 100; i <= containerLength; i += 100) {
            // If container cannot be evenly split in units of i, continue
            if (containerLength % i != 0) {
                continue;
            }
            int distanceSum = 0;
            for (Box box : boxList) {
                distanceSum += box.getClosestDim(i);
            }
            if (distanceSum < smallestDistanceSum) {
                smallestDistanceSum = distanceSum;
                bestSegmentLen = i;
            }
        }
        return bestSegmentLen;
    };
}
