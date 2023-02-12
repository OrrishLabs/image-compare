package com.orrish.automation.entrypoint;

import com.orrish.automation.model.IgnoreRegion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageCompare {

    private BufferedImage baselineImage;
    private BufferedImage actualImage;
    private ArrayList<IgnoreRegion> ignoreRegionList = new ArrayList<IgnoreRegion>();

    public ImageCompare setIgnoreRegion(int startX, int startY, int endX, int endY) {
        ignoreRegionList.add(new IgnoreRegion(startX, startY, endX, endY));
        return this;
    }

    public ImageCompare setActual(BufferedImage bufferedImage) {
        this.actualImage = bufferedImage;
        return this;
    }

    public ImageCompare setBaseline(BufferedImage bufferedImage) {
        this.baselineImage = bufferedImage;
        return this;
    }

    public CompareResult compareImage() throws Exception {
        int expectedWidth = baselineImage.getWidth();
        int expectedHeight = baselineImage.getHeight();
        int actualWidth = actualImage.getWidth();
        int actualHeight = actualImage.getHeight();
        int[][] pixelDiffMatrix = new int[expectedHeight][expectedWidth];
        if (expectedWidth != actualWidth || expectedHeight != actualHeight) {
            throw new Exception("Dimension don't match. Cannot compare.");
        } else {
            for (int row = 0; row < expectedHeight; row++) {
                for (int column = 0; column < expectedWidth; column++) {
                    boolean inIgnoreRegion = false;
                    for (IgnoreRegion ignoreRegion : ignoreRegionList) {
                        if (isWithinIgnoreRegion(column, row, ignoreRegion)) {
                            pixelDiffMatrix[row][column] = -1;
                            inIgnoreRegion = true;
                            break;
                        }
                    }
                    if (isPixelDifferent(column, row) && !inIgnoreRegion) {
                        pixelDiffMatrix[row][column] = 1;
                    }
                }
            }
        }
        return new CompareResult(pixelDiffMatrix);
    }

    public boolean isPixelDifferent(int column, int row) {
        int pixelExpected = baselineImage.getRGB(column, row);
        int pixelActual = actualImage.getRGB(column, row);

        Color colorExpected = new Color(pixelExpected, true);
        int redExpected = colorExpected.getRed();
        int greenExpected = colorExpected.getGreen();
        int blueExpected = colorExpected.getBlue();

        Color colorActual = new Color(pixelActual, true);
        int redActual = colorActual.getRed();
        int greenActual = colorActual.getGreen();
        int blueActual = colorActual.getBlue();

        return redExpected != redActual || greenExpected != greenActual || blueExpected != blueActual;
    }

    public boolean isWithinIgnoreRegion(int x, int y, IgnoreRegion ignoreRegion) {
        return (x > ignoreRegion.startX && x < ignoreRegion.endX && y > ignoreRegion.startY && y < ignoreRegion.endY);
    }
}
