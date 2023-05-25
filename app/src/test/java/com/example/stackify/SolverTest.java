package com.example.stackify;

import junit.framework.TestCase;

import java.util.ArrayList;

public class SolverTest extends TestCase {

    public void testNoOrderMaxMinDimSolverRotation() {
        ArrayList<Box> testBoxList = new ArrayList<Box>();
        testBoxList.add(new Box(1, 200, 270, 400));
        testBoxList.add(new Box(1, 250, 900, 1000));
        testBoxList.add(new Box(1, 150, 750, 650));
        testBoxList.add(new Box(1, 200, 240, 600));
        testBoxList.add(new Box(1, 225, 200, 400));

        int segmentLen = SegmentLengthSelector.minVarianceDim(testBoxList);
        for (Box box : testBoxList) {
            box.rotateToClosestDim(segmentLen);
        }
        assertEquals(200, testBoxList.get(0).getLength());
        assertEquals(250, testBoxList.get(1).getLength());
        assertEquals(150, testBoxList.get(2).getLength());
        assertEquals(240, testBoxList.get(3).getLength());
        assertEquals(225, testBoxList.get(4).getLength());
    }

}