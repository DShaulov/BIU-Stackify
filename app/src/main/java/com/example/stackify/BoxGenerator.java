package com.example.stackify;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contains methods for generating lists of boxes
 */
public class BoxGenerator {
    Random random;
    int typeAStd;
    int typeAHeightMean;
    int typeAWidthMean;
    int typeALengthMean;

    public BoxGenerator() {
        random = new Random();
        typeAStd = 50;
        typeAHeightMean = 200;
        typeAWidthMean = 200;
        typeALengthMean = 400;
    }

    /**
     * Generates 50 type A boxes
     * @return
     */
    public ArrayList<Box> generateTypeABoxes() {
        ArrayList<Box> boxList = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            int unpackOrder = i;
            int height = (Math.abs((int) (random.nextGaussian() * typeAStd + typeAHeightMean)) / 100) * 100 ;
            int width = (Math.abs((int) (random.nextGaussian() * typeAStd + typeAWidthMean)) / 100 ) * 100;
            int length = (Math.abs((int) (random.nextGaussian() * typeAStd + typeALengthMean)) / 100) * 100;
            Box box = new Box(unpackOrder, height, width, length);
            boxList.add(box);
        }
        return boxList;
    }

    public ArrayList<Box> generateTypeABoxesMany() {
        ArrayList<Box> boxList = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            int unpackOrder = i;
            int height = (Math.abs((int) (random.nextGaussian() * typeAStd + typeAHeightMean)) / 100) * 100 ;
            int width = (Math.abs((int) (random.nextGaussian() * typeAStd + typeAWidthMean)) / 100 ) * 100;
            int length = (Math.abs((int) (random.nextGaussian() * typeAStd + typeALengthMean)) / 100) * 100;
            Box box = new Box(unpackOrder, height, width, length);
            boxList.add(box);
        }
        return boxList;
    }

    /**
     * Returns the measurement of a type A container
     * @return
     */
    public List<Integer> generateTypeAContainer() {
        List<Integer> dimList = new ArrayList<>();
        Integer height = 600;
        Integer width = 800;
        Integer length = 1600;
        dimList.add(height);
        dimList.add(width);
        dimList.add(length);
        return dimList;
    }

    public List<Integer> generateTypeAContainerMany() {
        List<Integer> dimList = new ArrayList<>();
        Integer height = 600;
        Integer width = 800;
        Integer length = 3200;
        dimList.add(height);
        dimList.add(width);
        dimList.add(length);
        return dimList;
    }
}
