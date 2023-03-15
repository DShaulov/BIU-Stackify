package com.example.stackify;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Solution implements Serializable {
    private List<Box> boxList;
    @PrimaryKey
    @NonNull
    private String solutionName;
    private String csvName;
    private int containerHeight;
    private int containerWidth;
    private int containerLength;
    private int numOfSegments;
    private List<Segment> segmentList;
    private int numOfBoxesTotal;
    private int numOfBoxesInSolution;




    public Solution(List<Box> boxList ,int containerHeight, int containerWidth, int containerLength, int numOfBoxesTotal) {
        this.boxList = boxList;
        this.solutionName = "Solution";
        this.containerHeight = containerHeight;
        this.containerWidth = containerWidth;
        this.containerLength = containerLength;
        this.segmentList = new ArrayList<>();
        this.numOfBoxesTotal = numOfBoxesTotal;
        this.numOfBoxesInSolution = 0;
    }

    public void addSegment(Segment segment) {
        segmentList.add(segment);
        numOfSegments += 1;
        numOfBoxesInSolution += segment.getNumOfBoxes();
    }

    /**
     * Marks the boxes inside the solution as packed.
     */
    public void markAsPacked() {
        for (Segment segment: segmentList) {
            for (Box box : segment.getBoxList()) {
                box.setPacked(true);
            }
        }
    }


    public List<Box> getBoxList() {
        return boxList;
    }

    public void setBoxList(List<Box> boxList) {
        this.boxList = boxList;
    }
    /**
     * Returns the percentage of boxes in the solution out of total number of boxes.
     * @return
     */
    public float getCoverage() {
        return (numOfBoxesInSolution / numOfBoxesTotal) * 100;
    }

    @NonNull
    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(@NonNull String solutionName) {
        this.solutionName = solutionName;
    }

    public String getCsvName() {
        return csvName;
    }

    public void setCsvName(String csvName) {
        this.csvName = csvName;
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

    public List<Segment> getSegmentList() {
        return segmentList;
    }

    public void setSegmentList(List<Segment> segmentList) {
        this.segmentList = segmentList;
    }

    public int getNumOfSegments() {
        return numOfSegments;
    }

    public void setNumOfSegments(int numOfSegments) {
        this.numOfSegments = numOfSegments;
    }

    public int getNumOfBoxesTotal() {
        return numOfBoxesTotal;
    }

    public void setNumOfBoxesTotal(int numOfBoxesTotal) {
        this.numOfBoxesTotal = numOfBoxesTotal;
    }

    public int getNumOfBoxesInSolution() {
        return numOfBoxesInSolution;
    }

    public void setNumOfBoxesInSolution(int numOfBoxesInSolution) {
        this.numOfBoxesInSolution = numOfBoxesInSolution;
    }
}
