package com.example.stackify;

import junit.framework.TestCase;

import java.util.ArrayList;

public class BoxSorterTest extends TestCase {

    public void testSortByHeight() {
        ArrayList<Box> testBoxList = new ArrayList<Box>();
        testBoxList.add(new Box(1, 200, 300, 400));
        testBoxList.add(new Box(1, 850, 900, 1000));
        testBoxList.add(new Box(1, 450, 750, 650));
        testBoxList.add(new Box(1, 200, 100, 600));
        testBoxList.add(new Box(1, 200, 200, 400));

        BoxSorter.sortByHeight(testBoxList);
        assertEquals(850, testBoxList.get(0).getHeight());
    }

    public void testSortByWidth() {
        ArrayList<Box> testBoxList = new ArrayList<Box>();
        testBoxList.add(new Box(1, 200, 300, 400));
        testBoxList.add(new Box(1, 850, 900, 1000));
        testBoxList.add(new Box(1, 450, 750, 650));
        testBoxList.add(new Box(1, 200, 100, 600));
        testBoxList.add(new Box(1, 200, 200, 400));

        BoxSorter.sortByWidth(testBoxList);
        assertEquals(900, testBoxList.get(0).getWidth());
    }

    public void testSortByLength() {
        ArrayList<Box> testBoxList = new ArrayList<Box>();
        testBoxList.add(new Box(1, 200, 300, 400));
        testBoxList.add(new Box(1, 850, 900, 1000));
        testBoxList.add(new Box(1, 450, 750, 650));
        testBoxList.add(new Box(1, 200, 100, 600));
        testBoxList.add(new Box(1, 200, 200, 400));

        BoxSorter.sortByLength(testBoxList);
        assertEquals(1000, testBoxList.get(0).getLength());
    }

    public void testSortByUnpackOrder() {
        ArrayList<Box> testBoxList = new ArrayList<Box>();
        testBoxList.add(new Box(1, 200, 300, 400));
        testBoxList.add(new Box(2, 850, 900, 1000));
        testBoxList.add(new Box(3, 450, 750, 650));
        testBoxList.add(new Box(4, 200, 100, 600));
        testBoxList.add(new Box(5, 200, 200, 400));

        BoxSorter.sortByUnpackOrder(testBoxList);
        assertEquals(5, testBoxList.get(0).getUnpackOrder());
    }
}