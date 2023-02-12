package com.orrish.automation.entrypoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class SampleTest {

    public static void main(String[] args) throws Exception {
        CompareResult compareResult = new ImageCompare()
                .setBaseline(ImageIO.read(new File("Expected.JPG")))
                .setActual(ImageIO.read(new File("Actual.JPG")))
                .setIgnoreRegion(0, 0, 10, 10)
                .compareImage();
        compareResult.setDiffColor(Color.BLUE)
                .setDiffFile("diff.JPG")
                .saveDiffImage();
        System.out.println("Total pix diff: " + compareResult.getDiffPixelCount());
        System.out.println("Total pix : " + compareResult.getTotalPixelCount());
        System.out.println("Total pix diff %age: " + compareResult.getDiffPixelPercentage());
        System.out.println("isSame: " + compareResult.isSame());
    }
}
