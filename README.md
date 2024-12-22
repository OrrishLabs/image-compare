<h1 align="center">Orrish Image Compare</h1>
<h3 align="center">Plain Java library to compare two images.</h3>

It compares two same sized images and can return diff statistics. It can also save the diff file with differences shown in the configured colors.
This has been tested with png and JPG images.

### Usage
###### Maven
Add the repository in pom.xml.
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Include it as dependency like this
```xml
<dependency>
    <groupId>com.github.OrrishLabs</groupId>
    <artifactId>image-compare</artifactId>
    <version>0.0.3</version>
</dependency>
```
###### Usage example
```java
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

        compareResultDetailed.isSameWithIgnoreArea();
        // Similarly, use other results from CompareResult

```

