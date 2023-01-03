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

}
