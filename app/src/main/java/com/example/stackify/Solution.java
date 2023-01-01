package com.example.stackify;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Solution {
    @PrimaryKey
    @NonNull
    private String solutionName;
    private String csvName;
    private int containerHeight;
    private int containerWidth;
    private int containerLength;
    private int numOfSegments;
    private List<Segment> segmentList;

    public Solution(int containerHeight, int containerWidth, int containerLength) {
        this.solutionName = "Solution";
        this.containerHeight = containerHeight;
        this.containerWidth = containerWidth;
        this.containerLength = containerLength;
        this.segmentList = new ArrayList<>();
    }

    public void addSegment(Segment segment) {
        segmentList.add(segment);
        numOfSegments += 1;
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
}
