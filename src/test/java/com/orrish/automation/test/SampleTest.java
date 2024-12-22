package com.orrish.automation.test;

import com.orrish.automation.entrypoint.CompareImage;
import com.orrish.automation.entrypoint.CompareResult;

import java.awt.*;

public class SampleTest {

    public static void main(String[] args) throws Exception {

        // Simplest way
        CompareResult compareResultSimple = new CompareImage()
                .setBaselineFile("expected.png")
                .setActualFile("actual.png")
                .setSaveDiffFile(false)
                .compare();

        // Detailed way
        CompareResult compareResultDetailed = new CompareImage()
                .setBaselineFile("expected.png")
                .setActualFile("actual.png")
                .setDiffFile("diff.png")
                .setSaveDiffFile(true)
                .setDiffColor(Color.RED)
                .setIgnoreRegion(0, 0, 1000, 500)
                .setMaxDiffPixels(90)
                .setDiffThreshold(2)
                .compare();

        System.out.println("Total pix diff: " + compareResultDetailed.getDiffPixelCount());
        System.out.println("Total pix : " + compareResultDetailed.getTotalPixelCount());
        System.out.println("Total pix diff %age: " + compareResultDetailed.getDiffPixelPercentage());
        System.out.println("isSameStrict : " + compareResultDetailed.isSameStrict());
        System.out.println("isSameWithIgnoreArea : " + compareResultDetailed.isSameWithIgnoreArea());
        System.out.println("isSameWithAllowance: " + compareResultDetailed.isSameWithAllowance());

        System.out.println("Baseline file name: " + compareResultDetailed.getBaselineImageName());
        System.out.println("Actual file name: " + compareResultDetailed.getActualImageName());
        System.out.println("Diff file name: " + compareResultDetailed.getDiffImageName());
        System.out.println("getImageWidth: " + compareResultDetailed.getImageWidth());
        System.out.println("getImageHeight: " + compareResultDetailed.getImageHeight());
    }

}
