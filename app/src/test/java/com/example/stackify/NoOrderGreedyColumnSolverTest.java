package com.example.stackify;

import junit.framework.TestCase;

import java.util.ArrayList;

public class NoOrderGreedyColumnSolverTest extends TestCase {
    public void testSolutionOneSegment() {
        int containerHeight = 600;
        int containerWidth = 700;
        int containerLength = 200;
        ArrayList<Box> testBoxList = new ArrayList<Box>();
        Box box1 = new Box(1, 200, 400, 200);
        Box box2 = new Box(1, 200, 250, 200);
        Box box3 = new Box(1, 300, 300, 200);
        Box box4 = new Box(1, 300, 350, 200);

        testBoxList.add(box1);
        testBoxList.add(box2);
        testBoxList.add(box3);
        testBoxList.add(box4);

        Solver solver = new NoOrderGreedyColumnSolver(testBoxList, containerHeight, containerWidth, containerLength);
        solver.solve();

        assertEquals(0, box1.getBottomLeft().getX());
        assertEquals(0, box1.getBottomLeft().getY());

        assertEquals(0, box4.getBottomLeft().getX());
        assertEquals(200, box4.getBottomLeft().getY());

        assertEquals(400, box3.getBottomLeft().getX());
        assertEquals(0, box3.getBottomLeft().getY());

        assertEquals(400, box2.getBottomLeft().getX());
        assertEquals(300, box2.getBottomLeft().getY());
    }
}