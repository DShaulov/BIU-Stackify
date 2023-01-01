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

    public void testGetMaxDim() {
        Box testBox = new Box(1, 200, 1500, 300);
        assertEquals(1500, testBox.getMaxDim());
    }

    public void testGetMinDim() {
        Box testBox = new Box(1, 200, 1500, 300);
        assertEquals(200, testBox.getMinDim());
    }
}