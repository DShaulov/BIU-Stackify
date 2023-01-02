package com.example.stackify;

import junit.framework.TestCase;

public class BoxTest extends TestCase {

    public void testRotateMaxLength() {
        Box testBox = new Box(1, 200, 1500, 300);
        testBox.rotateMaxLength();
        assertEquals(1500, testBox.getLength());
        assertEquals(300, testBox.getWidth());
    }

    public void testRotateHeightWidth() {
        Box testBox = new Box(1, 200, 1500, 300);
        testBox.rotateHeightWidth();
        assertEquals(1500, testBox.getHeight());
        assertEquals(200, testBox.getWidth());
    }

    public void testRotateHeightLength() {
        Box testBox = new Box(1, 200, 1500, 300);
        testBox.rotateHeightLength();
        assertEquals(300, testBox.getHeight());
        assertEquals(200, testBox.getLength());
    }

    public void testRotateWidthLength() {
        Box testBox = new Box(1, 200, 1500, 300);
        testBox.rotateWidthLength();
        assertEquals(300, testBox.getWidth());
        assertEquals(1500, testBox.getLength());
    }

    public void testGetMaxDim() {
        Box testBox = new Box(1, 200, 1500, 300);
        assertEquals(1500, testBox.getMaxDim());
    }

    public void testGetMinDim() {
        Box testBox = new Box(1, 200, 1500, 300);
        assertEquals(200, testBox.getMinDim());
    }

    public void testRotateToClosestSmallerDim() {
        Box testBox = new Box(1, 750, 1500, 825);
        testBox.rotateToClosestSmallerDim(800);
        assertEquals(750, testBox.getLength());
    }
}