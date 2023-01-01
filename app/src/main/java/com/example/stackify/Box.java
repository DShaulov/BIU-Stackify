package com.example.stackify;


public class Box {
    private int height;
    private int width;
    private int length;
    private int unpackOrder;
    private Coordinate topLeft;
    private Coordinate topRight;
    private Coordinate bottomLeft;
    private Coordinate bottomRight;


    public Box(int unpackOrder , int height, int width, int length) {
        this.height = height;
        this.width = width;
        this.length = length;
        this.unpackOrder = unpackOrder;
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
}
