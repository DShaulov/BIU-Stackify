package com.example.stackify;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SolutionModel {
    @PrimaryKey
    @NonNull
    private String solutionName;
    private String csvName;
    private int containerHeight;
    private int containerWidth;
    private int containerLength;
    private List<BoxModel> boxList;

    public SolutionModel(int containerHeight, int containerWidth, int containerLength, List<BoxModel> boxList) {
        this.containerHeight = containerHeight;
        this.containerWidth = containerWidth;
        this.containerLength = containerLength;
        this.boxList = boxList;
    }

    public int getMaxHeight() {
        int maxHeight = 0;
        for (BoxModel box : boxList) {
            if (box.getHeight() > maxHeight) {
                maxHeight = box.getHeight();
            }
        }
        return maxHeight;
    }
    public int getMaxWidth() {
        int maxWidth = 0;
        for (BoxModel box : boxList) {
            if (box.getWidth() > maxWidth) {
                maxWidth = box.getWidth();
            }
        }
        return maxWidth;
    }
    public int getMaxLength() {
        int maxLength = 0;
        for (BoxModel box : boxList) {
            if (box.getLength() > maxLength) {
                maxLength = box.getHeight();
            }
        }
        return maxLength;
    }

    public int calculateMaxBoxAmount() {
        return 1;
    }

    public boolean isLegal() {
        if ((getMaxHeight() > containerHeight) || (getMaxWidth() > containerWidth) || (getMaxLength() > containerLength)) {
            return false;
        }
        return true;
    }
    // Checks that the solution returns the optimal amount of boxes that can fit.
    public boolean isOptimal() {
        if (boxList.size() < calculateMaxBoxAmount()) {
            return false;
        }
        return true;
    }

    public int getContainerHeight() {
        return containerHeight;
    }

    public void setContainerHeight(int containerHeight) {
        this.containerHeight = containerHeight;
    }

    public int getContainerWidth() {
        return containerWidth;
    }

    public void setContainerWidth(int containerWidth) {
        this.containerWidth = containerWidth;
    }

    public int getContainerLength() {
        return containerLength;
    }

    public void setContainerLength(int containerLength) {
        this.containerLength = containerLength;
    }

    public List<BoxModel> getBoxList() {
        return boxList;
    }

    public void setBoxList(List<BoxModel> boxList) {
        this.boxList = boxList;
    }
    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public String getCsvName() {
        return csvName;
    }

    public void setCsvName(String csvName) {
        this.csvName = csvName;
    }
}
