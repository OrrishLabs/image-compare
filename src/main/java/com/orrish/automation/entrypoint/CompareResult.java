package com.orrish.automation.entrypoint;

import java.math.BigDecimal;
import java.nio.file.Paths;

public class CompareResult {

    private final int[][] matrix;
    private int diffPixelCount = 0;
    private int totalPixelCount = 0;
    private String baselineImagePath;
    private String actualImagePath;
    private String diffImagePath;
    private int diffThresholdAllowed = -1;
    private int maxDiffPixelsAllowed = -1;
    private boolean isSameStrict;
    private final int imageHeight;
    private final int imageWidth;

    public CompareResult(int[][] matrix, int imageWidth, int imageHeight) {
        this.matrix = matrix;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    int[][] getMatrix() {
        return this.matrix;
    }

    CompareResult setActualImagePath(String actualImagePath) {
        this.actualImagePath = actualImagePath;
        return this;
    }

    public String getActualImagePath() {
        return actualImagePath;
    }

    CompareResult setBaselineImagePath(String baselineImagePath) {
        this.baselineImagePath = baselineImagePath;
        return this;
    }

    public String getBaselineImagePath() {
        return baselineImagePath;
    }

    CompareResult setDiffImagePath(String diffImagePath) {
        this.diffImagePath = diffImagePath;
        return this;
    }

    public String getDiffImagePath() {
        return diffImagePath;
    }

    public String getBaselineImageName() {
        return Paths.get(this.baselineImagePath).getFileName().toString();
    }

    public String getActualImageName() {
        return Paths.get(this.actualImagePath).getFileName().toString();
    }

    public String getDiffImageName() {
        return Paths.get(this.diffImagePath).getFileName().toString();
    }

    CompareResult setDiffThresholdAllowed(int diffThresholdAllowed) {
        this.diffThresholdAllowed = diffThresholdAllowed;
        return this;
    }

    CompareResult setMaxDiffPixelsAllowed(int maxDiffPixelsAllowed) {
        this.maxDiffPixelsAllowed = maxDiffPixelsAllowed;
        return this;
    }

    public boolean isSameWithIgnoreArea() {
        if (totalPixelCount == 0) populatePixelCounts();
        return diffPixelCount == 0;
    }

    public boolean isSameStrict() {
        return this.isSameStrict;
    }

    CompareResult setSameStrict(boolean sameStrict) {
        isSameStrict = sameStrict;
        return this;
    }

    public boolean isSameWithAllowance() {
        float actualDiffPercent = getDiffPixelPercentage().floatValue() * 100;
        boolean isWithinThresholdAllowed = actualDiffPercent <= diffThresholdAllowed;
        if (maxDiffPixelsAllowed > -1 && diffThresholdAllowed > -1) {
            return diffPixelCount < maxDiffPixelsAllowed && isWithinThresholdAllowed;
        } else if (maxDiffPixelsAllowed > -1) {
            return diffPixelCount < maxDiffPixelsAllowed;
        } else if (diffThresholdAllowed > -1) {
            return isWithinThresholdAllowed;
        }
        return isSameWithIgnoreArea();
    }

    public int getDiffPixelCount() {
        if (totalPixelCount == 0) populatePixelCounts();
        return diffPixelCount;
    }

    public BigDecimal getDiffPixelPercentage() {
        if (totalPixelCount == 0) populatePixelCounts();
        float value = (diffPixelCount * 100.0f) / totalPixelCount;
        BigDecimal bigDecimal = new BigDecimal(Float.toString(value));
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bigDecimal;
    }

    public int getTotalPixelCount() {
        if (totalPixelCount == 0) populatePixelCounts();
        return totalPixelCount;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    private void populatePixelCounts() {
        for (int[] ints : matrix) {
            for (int column = 0; column < matrix[0].length; column++) {
                totalPixelCount++;
                if (ints[column] == 1) {
                    diffPixelCount++;
                }
            }
        }
    }

}
