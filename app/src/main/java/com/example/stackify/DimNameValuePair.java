package com.example.stackify;

public class DimNameValuePair {
    private String dimName;
    private int dimValue;

    public DimNameValuePair(String dimName, int dimValue) {
        this.dimName = dimName;
        this.dimValue = dimValue;
    }

    public String getDimName() {
        return dimName;
    }

    public void setDimName(String dimName) {
        this.dimName = dimName;
    }

    public int getDimValue() {
        return dimValue;
    }

    public void setDimValue(int dimValue) {
        this.dimValue = dimValue;
    }
}
