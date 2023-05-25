package com.example.stackify;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SegmentLengthSelectorTest extends TestCase {

    public void testGetVariance() {
        ArrayList<Integer> numList = new ArrayList<>();
        numList.add(200);
        numList.add(300);
        numList.add(400);
        numList.add(500);
        numList.add(600);
        assertEquals(20000.0, SegmentLengthSelector.getVariance(numList));
    }

    public void testMaxMinDim() {
        ArrayList<Box> testBoxList = new ArrayList<Box>();
        testBoxList.add(new Box(1, 200, 300, 400));
        testBoxList.add(new Box(1, 850, 900, 1000));
        testBoxList.add(new Box(1, 450, 750, 650));
        testBoxList.add(new Box(1, 200, 100, 600));
        testBoxList.add(new Box(1, 200, 200, 400));

        assertEquals(1000, SegmentLengthSelector.maxMinDim(testBoxList));
    }

    public void testMinVarianceDim() {
        ArrayList<Box> testBoxList = new ArrayList<Box>();
        testBoxList.add(new Box(1, 200, 300, 400));
        testBoxList.add(new Box(1, 250, 900, 1000));
        testBoxList.add(new Box(1, 150, 750, 650));
        testBoxList.add(new Box(1, 200, 100, 600));
        testBoxList.add(new Box(1, 225, 200, 400));

        assertEquals(250, SegmentLengthSelector.minVarianceDim(testBoxList));

        ArrayList<Box> testBoxList2 = new ArrayList<Box>();
        testBoxList2.add(new Box(1, 200, 300, 1200));
        testBoxList2.add(new Box(1, 250, 900, 1200));
        testBoxList2.add(new Box(1, 150, 750, 1200));
        testBoxList2.add(new Box(1, 200, 100, 1200));
        testBoxList2.add(new Box(1, 225, 200, 1180));

        assertEquals(1200, SegmentLengthSelector.minVarianceDim(testBoxList2));

    }
}