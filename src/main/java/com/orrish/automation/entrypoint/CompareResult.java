package com.orrish.automation.entrypoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class CompareResult {

    int[][] matrix = null;
    Color color = Color.RED;
    File file = new File("diff.png");
    int diffPixelCount = 0;
    int totalPixelCount = 0;

    public CompareResult(int[][] matrix) {
        this.matrix = matrix;
    }

    public CompareResult setDiffColor(Color color) {
        this.color = color;
        return this;
    }

    public CompareResult setDiffFile(String fileName) {
        this.file = new File(fileName);
        return this;
    }

    public void populatePixelCounts() {
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[0].length; column++) {
                totalPixelCount++;
                if (matrix[row][column] == 1) {
                    diffPixelCount++;
                }
            }
        }
    }

    public boolean isSame() {
        if (totalPixelCount == 0) populatePixelCounts();
        return diffPixelCount == 0;
    }

    public int getDiffPixelCount() {
        if (totalPixelCount == 0) populatePixelCounts();
        return diffPixelCount;
    }

    public int getTotalPixelCount() {
        if (totalPixelCount == 0) populatePixelCounts();
        return totalPixelCount;
    }

    public BigDecimal getDiffPixelPercentage() {
        if (totalPixelCount == 0) populatePixelCounts();
        float value = (diffPixelCount * 100.0f) / totalPixelCount;
        BigDecimal bigDecimal = new BigDecimal(Float.toString(value));
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bigDecimal;
    }

    public void saveDiffImage() throws IOException {
        BufferedImage diffImage = new BufferedImage(matrix[0].length, matrix.length, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[0].length; column++) {
                if (matrix[row][column] == 1) {
                    //Difference
                    diffImage.setRGB(column, row, color.getRGB());
                } else if (matrix[row][column] == -1) {
                    //Ignore area
                    diffImage.setRGB(column, row, Color.LIGHT_GRAY.getRGB());
                } else {
                    //Same
                    diffImage.setRGB(column, row, Color.WHITE.getRGB());
                }
            }
        }
        ImageIO.write(diffImage, "png", file);
    }
}
