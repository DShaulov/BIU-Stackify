package com.example.stackify;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
    private boolean isOrdered;
    private LocalDate date;



    public Solution(List<Box> boxList ,int containerHeight, int containerWidth, int containerLength, int numOfBoxesTotal) {
        this.boxList = boxList;
        this.solutionName = "Solution";
        this.containerHeight = containerHeight;
        this.containerWidth = containerWidth;
        this.containerLength = containerLength;
        this.segmentList = new ArrayList<>();
        this.numOfBoxesTotal = numOfBoxesTotal;
        this.numOfBoxesInSolution = 0;
        this.date = LocalDate.now();
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

    /**
     * Marks all boxes in the box list as unpacked.
     */
    public void markAsUnpacked() {
        for (Box box : boxList) {
            box.setPacked(false);
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
        return (((float)numOfBoxesInSolution / (float)numOfBoxesTotal) * 100);
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

    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean ordered) {
        isOrdered = ordered;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void updateNumOfBoxesInSolution() {
        int count = 0;
        for (Segment segment : segmentList) {
            for (Box box : segment.getBoxList()) {
                count += 1;
            }
        }
        setNumOfBoxesInSolution(count);
    }
}
