package com.example.stackify;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Box implements Serializable {
    private int height;
    private int width;
    private int length;
    private int unpackOrder;
    private boolean isPacked;
    private int segmentNum;
    private boolean manuallyPlaced;
    private boolean isTooLarge;
    private Coordinate bottomLeft;

    private int largeNum;


    public Box(int unpackOrder , int height, int width, int length) {
        this.height = height;
        this.width = width;
        this.length = length;
        this.unpackOrder = unpackOrder;
        this.manuallyPlaced = false;
        this.isTooLarge = false;
        this.largeNum = 147483647;
    }

    // Rotates the box to its maximum possible length
    public void rotateMaxLength() {
        int maxDimension = Math.max(height, Math.max(width, length));
        if (maxDimension == height) {
            int temp = height;
            height = length;
            length = temp;
        }
        else if (maxDimension == width) {
            int temp = width;
            width = length;
            length = temp;
        }
    };

    // Rotates the height and width dimensions of the box
    public void rotateHeightWidth() {
        int temp = height;
        height = width;
        width = temp;
    }

    // Rotates the height and length dimensions
    public void rotateHeightLength() {
        int temp = height;
        height = length;
        length = temp;
    }

    // Rotates the width and length dimensions of the box
    public void rotateWidthLength() {
        int temp = width;
        width = length;
        length = temp;
    }

    // If the height of the box is larger than the width, rotates it
    public void rotateToMaxWidth() {
        if (height > width) {
            rotateHeightWidth();
        }
    }

    // Rotates the box such that its length is smaller than, and as close as possible, to the segment length
    public void rotateToClosestDim(int segmentLength) {
        Integer heightDistance = segmentLength - height;
        Integer widthDistance = segmentLength - width;
        Integer lengthDistance = segmentLength - length;
        ArrayList<Integer> distanceArray = new ArrayList<>();
        distanceArray.add(heightDistance);
        distanceArray.add(widthDistance);
        distanceArray.add(lengthDistance);
        distanceArray.clone();
        // Among positive distances (meaning smaller than segmentLength), choose the smallest
        Iterator<Integer> iterator = distanceArray.iterator();
        while (iterator.hasNext()){
            Integer distance = iterator.next();
            if (distance < 0) {
                iterator.remove();
            }
        }
        Collections.sort(distanceArray);
        Integer smallestDistance = distanceArray.get(0);
        if (smallestDistance == heightDistance) {
            rotateHeightLength();
        }
        if (smallestDistance == widthDistance) {
            rotateWidthLength();
        }
    }

    public int getClosestDim(int segmentLength) {
        Integer heightDistance = segmentLength - height;
        Integer widthDistance = segmentLength - width;
        Integer lengthDistance = segmentLength - length;
        ArrayList<Integer> distanceArray = new ArrayList<>();
        distanceArray.add(heightDistance);
        distanceArray.add(widthDistance);
        distanceArray.add(lengthDistance);
        distanceArray.clone();
        // Among positive distances (meaning smaller than segmentLength), choose the smallest
        Iterator<Integer> iterator = distanceArray.iterator();
        while (iterator.hasNext()){
            Integer distance = iterator.next();
            if (distance < 0) {
                iterator.remove();
            }
        }
        // Returns a number near largest possible if there are no smaller dimensions.
        if (distanceArray.size() == 0) {
            return largeNum;
        }
        Collections.sort(distanceArray);
        Integer smallestDistance = distanceArray.get(0);
        return smallestDistance;
    };

    // Returns a list of all dims - [height, width, length]
    public List<Integer> getDimList() {
        List<Integer> list = new ArrayList<>();
        list.add(height);
        list.add(width);
        list.add(length);
        return list;
    };

    public int getMaxDim() {
        return Math.max(height, Math.max(width, length));
    }

    public int getMinDim() {
        return Math.min(height, Math.min(width, length));
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

    public int getUnpackOrder() {
        return unpackOrder;
    }

    public void setUnpackOrder(int unpackOrder) {
        this.unpackOrder = unpackOrder;
    }

    public Coordinate getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(Coordinate bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public boolean isPacked() {
        return isPacked;
    }

    public void setPacked(boolean packed) {
        isPacked = packed;
    }
    public int getSegmentNum() {
        return segmentNum;
    }

    public void setSegmentNum(int segmentNum) {
        this.segmentNum = segmentNum;
    }

    public boolean isManuallyPlaced() {
        return manuallyPlaced;
    }

    public void setManuallyPlaced(boolean manuallyPlaced) {
        this.manuallyPlaced = manuallyPlaced;
    }

    public boolean isTooLarge() {
        return isTooLarge;
    }

    public void setTooLarge(boolean tooLarge) {
        isTooLarge = tooLarge;
    }

}
