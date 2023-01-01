package com.example.stackify;

import java.util.ArrayList;

public class Segment {
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
