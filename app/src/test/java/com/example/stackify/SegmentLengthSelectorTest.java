package com.example.stackify;

import junit.framework.TestCase;

import java.util.ArrayList;

public class SegmentLengthSelectorTest extends TestCase {


    public void testMaxMinDim() {
        ArrayList<Box> testBoxList = new ArrayList<Box>();
        testBoxList.add(new Box(1, 200, 300, 400));
        testBoxList.add(new Box(1, 850, 900, 1000));
        testBoxList.add(new Box(1, 450, 750, 650));
        testBoxList.add(new Box(1, 200, 100, 600));
        testBoxList.add(new Box(1, 200, 200, 400));

        assertEquals(1000, SegmentLengthSelector.maxMinDim(testBoxList));
    }
}