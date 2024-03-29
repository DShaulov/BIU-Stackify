package com.example.stackify;

import java.io.Serializable;
import java.util.ArrayList;

public class Segment implements Serializable {
    private int height;
    private int width;
    private int length;
    private int numOfBoxes;
    private ArrayList<Box> boxList;

    public Segment(int height, int width, int length) {
        this.height = height;
        this.width = width;
        this.length = length;
        this.numOfBoxes = 0;
        this.boxList = new ArrayList<>();
    }

    public void addBox(Box box) {
        boxList.add(box);
        numOfBoxes += 1;
    }

    /**
     * Receives as input x,y coordinates and returns the box number which occupies these coordinates
     * @param x
     * @param y
     * @return
     */
    public int whichBoxWasClicked(int x, int y) {
        int[][] spaceArray = new int[height][width];
        for (Box box : boxList) {
            int startX = box.getBottomLeft().getX();
            int startY = box.getBottomLeft().getY();
            for (int i = startX; i < startX + box.getHeight(); i++) {
                for (int j = startY; j < startY + box.getWidth(); j++ ) {
                    spaceArray[i][j] = box.getUnpackOrder();
                }
            }
        }
        return spaceArray[x][y];
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getNumOfBoxes() {
        return numOfBoxes;
    }

    public void setNumOfBoxes(int numOfBoxes) {
        this.numOfBoxes = numOfBoxes;
    }

    public ArrayList<Box> getBoxList() {
        return boxList;
    }

    public void setBoxList(ArrayList<Box> boxList) {
        this.boxList = boxList;
    }
}
