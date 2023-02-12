<h1 align="center">Orrish Image Compare</h1>
<h3 align="center">Plain Java library to compare two images.</h3>

It compares two same sized images and can return diff statistics. It can also save the diff file with differences shown in the configured colors.
This has been tested with png and JPG images.

### Usage
###### Maven
Include it as dependency like this
```xml
<dependency>
    <groupId>com.orrish</groupId>
    <artifactId>image-compare</artifactId>
    <version>0.0.1</version>
</dependency>
```
###### Usage example
```java
CompareResult compareResult = new ImageCompare()
    .setBaseline(ImageIO.read(new File("Expected.png")))
    .setActual(ImageIO.read(new File("Actual.png")))
    .setIgnoreRegion(0, 0, 1047, 100)
    .compareImage();

compareResult.isSame();
compareResult.getDiffPixelCount();
compareResult.getDiffPixelPercentage();

compareResult.setDiffColor(Color.RED)
    .setDiffFile("diff.png")
    .saveDiffImage();


```

