package com.example.stackify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Contains methods for sorting lists of boxes according to their properties
 */
public class BoxSorter {
    public static void sortByHeight(ArrayList<Box> boxList) {
        Collections.sort(boxList, new Comparator<Box>() {
            @Override
            public int compare(Box box1, Box box2) {
                return box2.getHeight() - box1.getHeight();
            }
        });
    }
    public static void sortByWidth(ArrayList<Box> boxList) {
        Collections.sort(boxList, new Comparator<Box>() {
            @Override
            public int compare(Box box1, Box box2) {
                return box2.getWidth() - box1.getWidth();
            }
        });
    }
    public static void sortByLength(ArrayList<Box> boxList) {
        Collections.sort(boxList, new Comparator<Box>() {
            @Override
            public int compare(Box box1, Box box2) {
                return box2.getLength() - box1.getLength();
            }
        });
    }
    public static void sortByUnpackOrder(ArrayList<Box> boxList) {
        Collections.sort(boxList, new Comparator<Box>() {
            @Override
            public int compare(Box box1, Box box2) {
                return box2.getUnpackOrder() - box1.getUnpackOrder();
            }
        });
    }
    public static void sortByAreaAscending(ArrayList<Box> boxList) {
        Collections.sort(boxList, new Comparator<Box>() {
            @Override
            public int compare(Box box1, Box box2) {
                return (box1.getHeight() * box1.getWidth()) - (box2.getHeight() * box2.getWidth());
            }
        });
    }
    public static void sortByAreaDescending(ArrayList<Box> boxList) {
        Collections.sort(boxList, new Comparator<Box>() {
            @Override
            public int compare(Box box1, Box box2) {
                return (box2.getHeight() * box2.getWidth()) - (box1.getHeight() * box1.getWidth());
            }
        });
    }
    public static void sortByBottomLeftAscending(ArrayList<Box> boxList) {
        Collections.sort(boxList, new Comparator<Box>() {
            @Override
            public int compare(Box box1, Box box2) {
                if (box2.getBottomLeft().getY() != box1.getBottomLeft().getY()) {
                    return box1.getBottomLeft().getY() - box2.getBottomLeft().getY();
                }
                return box1.getBottomLeft().getX() - box2.getBottomLeft().getX();
            }
        });
    }
}
