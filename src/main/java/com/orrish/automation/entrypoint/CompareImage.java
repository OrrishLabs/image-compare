package com.orrish.automation.entrypoint;

import com.orrish.automation.model.IgnoreRegion;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class CompareImage {

    private String baselineImagePath;
    private String actualImagePath;
    private String diffImagePath = "diff.png";
    private boolean saveDiff = true;
    private Color color = Color.RED;
    private int diffThreshold = 0;
    private int maxDiffPixels = 0;
    private CompareResult compareResult;
    private final ArrayList<IgnoreRegion> ignoreRegionList = new ArrayList<IgnoreRegion>();

    public CompareImage setActualFile(String actualImagePath) {
        this.actualImagePath = actualImagePath;
        return this;
    }

    public CompareImage setBaselineFile(String baselineImagePath) {
        this.baselineImagePath = baselineImagePath;
        return this;
    }

    public CompareImage setDiffFile(String diffImagePath) {
        this.diffImagePath = diffImagePath;
        return this;
    }

    public CompareImage setIgnoreRegion(int startX, int startY, int endX, int endY) {
        ignoreRegionList.add(new IgnoreRegion(startX, startY, endX, endY));
        return this;
    }

    public CompareImage setDiffColor(Color color) {
        this.color = color;
        return this;
    }

    /***
     * Enter a value between 0 and 100 corresponding to the percentage, does not compare ignore area
     * @param value
     */
    public CompareImage setDiffThreshold(int value) {
        this.diffThreshold = value;
        return this;
    }

    /***
     * Does not compare ignore area.
     * @param value
     */
    public CompareImage setMaxDiffPixels(int value) {
        this.maxDiffPixels = value;
        return this;
    }

    public CompareImage setSaveDiffFile(boolean saveDiff) throws Exception {
        this.saveDiff = saveDiff;
        return this;
    }

    private void generateCompareResult() throws Exception {
        boolean isStrictSame = true;
        BufferedImage baselineBufferedImage = ImageIO.read(new File(this.baselineImagePath));
        BufferedImage actualBufferedImage = ImageIO.read(new File(this.actualImagePath));
        int expectedWidth = baselineBufferedImage.getWidth();
        int expectedHeight = baselineBufferedImage.getHeight();
        int actualWidth = actualBufferedImage.getWidth();
        int actualHeight = actualBufferedImage.getHeight();
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
                            if (isPixelDifferent(column, row, baselineBufferedImage, actualBufferedImage)) {
                                isStrictSame = false;
                            }
                            break;
                        }
                    }
                    if (isPixelDifferent(column, row, baselineBufferedImage, actualBufferedImage) && !inIgnoreRegion) {
                        pixelDiffMatrix[row][column] = 1;
                        isStrictSame = false;
                    }
                }
            }
        }
        this.compareResult = new CompareResult(pixelDiffMatrix, actualWidth, actualHeight)
                .setBaselineImagePath(this.baselineImagePath)
                .setActualImagePath(this.actualImagePath)
                .setDiffThresholdAllowed(this.diffThreshold)
                .setMaxDiffPixelsAllowed(this.maxDiffPixels)
                .setSameStrict(isStrictSame);
    }

    public CompareResult compare() throws Exception {
        generateCompareResult();
        int[][] matrix = this.compareResult.getMatrix();
        BufferedImage diffImage = new BufferedImage(matrix[0].length, matrix.length, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[0].length; column++) {
                if (matrix[row][column] == 1) {
                    //Difference
                    diffImage.setRGB(column, row, this.color.getRGB());
                } else if (matrix[row][column] == -1) {
                    //Ignore area
                    diffImage.setRGB(column, row, Color.LIGHT_GRAY.getRGB());
                } else {
                    //Same
                    diffImage.setRGB(column, row, Color.WHITE.getRGB());
                }
            }
        }
        if (saveDiff) {
            this.compareResult.setDiffImagePath(this.diffImagePath);
            ImageIO.write(diffImage, "png", new File(diffImagePath));
        }
        return this.compareResult;
    }

    private boolean isPixelDifferent(int column, int row, BufferedImage baselineImage, BufferedImage actualImage) {
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

    private boolean isWithinIgnoreRegion(int x, int y, IgnoreRegion ignoreRegion) {
        return (x > ignoreRegion.startX && x < ignoreRegion.endX && y > ignoreRegion.startY && y < ignoreRegion.endY);
    }
}
