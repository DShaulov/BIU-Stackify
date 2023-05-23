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
    public static DimNameValuePair minVarianceDim(ArrayList<Box> boxList) {
        // Edge case where there are no boxes in solution
        if (boxList.size() == 0) {
            return new DimNameValuePair("Height", 200);
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
            return new DimNameValuePair("Height", Collections.max(heightList));

        }
        else if (minVariance == widthVariance) {
            return new DimNameValuePair("Width", Collections.max(widthList));
        }
        else {
            return new DimNameValuePair("Length", Collections.max(lengthList));
        }
    }

    public static List<Integer> minVarianceDimAllDims(ArrayList<Box> boxList) {
        List<List<Integer>> lists = new ArrayList<>();
        for (Box box : boxList) {
            lists.add(box.getDimList());
        }
        List<Integer> bestDims = new ArrayList<>();
        double minVariance = Double.MAX_VALUE;

        int n = lists.size(); // number of lists
        int m = lists.get(0).size(); // size of each list (assumed to be the same for all lists)

        for (int i = 0; i < Math.pow(m, n); i++) {
            ArrayList<Integer> dims = new ArrayList<>();
            int tmp = i;

            for (int j = 0; j < n; j++) {
                dims.add(lists.get(j).get(tmp % m));
                tmp /= m;
            }

            double variance = getVariance(dims);
            if (variance < minVariance) {
                minVariance = variance;
                bestDims = dims;
            }
        }

        // Print the dims with minimum variance

        return bestDims;
    };
}
