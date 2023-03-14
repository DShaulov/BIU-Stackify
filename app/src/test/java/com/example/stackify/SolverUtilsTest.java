package com.example.stackify;

import junit.framework.TestCase;

public class SolverUtilsTest extends TestCase {

    public void testBoxOutOfBounds() {
        int containerHeight = 300;
        int containerWidth = 200;
        int xPosition = 50;
        int yPosition = 50;

        Box InBoundsBox = new Box(0, 200, 100, 100);
        Box HeightOutOfBoundsBox = new Box(0, 300, 100, 100);
        Box WidthOutOfBoundsBox = new Box(0, 200, 200, 100);

        assertEquals(false, SolverUtils.boxOutOfBounds(InBoundsBox, containerHeight, containerWidth, xPosition, yPosition));
        assertEquals(true, SolverUtils.boxOutOfBounds(HeightOutOfBoundsBox, containerHeight, containerWidth, xPosition, yPosition));
        assertEquals(true, SolverUtils.boxOutOfBounds(WidthOutOfBoundsBox, containerHeight, containerWidth, xPosition, yPosition));
    }

    public void testCornersAreOccupied() {
        int containerHeight = 300;
        int containerWidth = 200;
        int xPosition = 50;
        int yPosition = 50;

        boolean[][] spaceMatrix = new boolean[containerHeight][containerWidth];
        spaceMatrix[yPosition][xPosition + 10] = true;
        Box box = new Box(0, 200, 100, 100);

        assertEquals(false, SolverUtils.cornersAreOccupied(box, spaceMatrix, xPosition, yPosition));
        assertEquals(true, SolverUtils.cornersAreOccupied(box, spaceMatrix, xPosition + 10, yPosition));
    }

    public void testRectangleSpaceIsFree() {
        int containerHeight = 300;
        int containerWidth = 200;
        int xPosition = 50;
        int yPosition = 50;
        Box box = new Box(0, 200, 100, 100);

        boolean[][] spaceMatrixFree = new boolean[containerHeight][containerWidth];
        boolean[][] spaceMatrixOccupied = new boolean[containerHeight][containerWidth];
        spaceMatrixOccupied[124][101] = true;

        assertEquals(true, SolverUtils.rectangleSpaceIsFree(box, spaceMatrixFree, xPosition, yPosition));
        assertEquals(false, SolverUtils.rectangleSpaceIsFree(box, spaceMatrixOccupied, xPosition, yPosition));
    }
}