package com.example.stackify;


public class BoxModel {
    private int height;
    private int width;
    private int length;
    private int unpackOrder;
    private int positionX;
    private int positionY;
    private int positionZ;


    public BoxModel(int unpackOrder ,int height, int width, int length) {
        this.height = height;
        this.width = width;
        this.length = length;
        this.unpackOrder = unpackOrder;
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

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getPositionZ() {
        return positionZ;
    }

    public void setPositionZ(int positionZ) {
        this.positionZ = positionZ;
    }
}
