package com.owt.test.utils;

import static com.owt.test.ThrowableAssertion.assertThrown;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.IIOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.owt.utils.ImageUtils;

/**
 * Created by Open Web Technology.
 *
 * @author Loïc Bernollin Open Web Technology
 * @since 15 oct. 2015
 * 
 */
public class ImageUtilsTest
{
    // TODO: use something less volatile than a linkedin image
    private static final String REMOTE_IMG_URL = "https://media.licdn.com/mpr/mpr/shrink_200_200/AAEAAQAAAAAAAAWVAAAAJDE3ZTM2OTM2LTQ0NmUtNGViNS1hN2RkLWQxZjFmZWU3ZmEwMA.png";

    private static final String TMP_DIRECTORY_PATH = "/tmp/test_image_utils";
    private static final File TMP_DIRECTORY = new File(TMP_DIRECTORY_PATH);

    @Before
    public void setup() throws Exception
    {
        // setup temporary directory
        FileUtils.deleteDirectory(TMP_DIRECTORY);
        TMP_DIRECTORY.mkdir();
    }

    @After
    public void tearDown() throws Exception
    {
        // delete tmp dir
        FileUtils.deleteDirectory(TMP_DIRECTORY);
    }

    @Test
    public void testLoadImage() throws IOException
    {
        final File testImage = new ClassPathResource("testImage.png").getFile();
        assertTrue(testImage.exists());

        final BufferedImage result = ImageUtils.loadImage(testImage);

        assertNotNull(result);
        assertEquals(200, result.getWidth());
        assertEquals(200, result.getHeight());
    }

    @Test
    public void testLoadImage_failure()
    {
        final File testImage = null;
        assertThrown(() -> ImageUtils.loadImage(testImage)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testLoadLocaleImage() throws IOException
    {
        final String testImage = new ClassPathResource("testImage.png").getFile().getPath();

        final BufferedImage result = ImageUtils.loadLocalImage(testImage);

        assertNotNull(result);
        assertEquals(200, result.getWidth());
        assertEquals(200, result.getHeight());
    }

    @Test
    public void testLoadLocaleImage_failure()
    {
        assertThrown(() -> ImageUtils.loadLocalImage("/tmp/not/exist/something_wrong")).isInstanceOf(IIOException.class);
    }

    @Test
    public void testLoadRemoteImage() throws IOException
    {
        final BufferedImage result = ImageUtils.loadRemoteImage(REMOTE_IMG_URL);

        assertNotNull(result);
        assertEquals(200, result.getWidth());
        assertEquals(200, result.getHeight());
    }

    @Test
    public void testLoadRemoteImage_failure()
    {
        assertThrown(() -> ImageUtils.loadRemoteImage("http://127.0.0.1:12346/not_found.jpg")).isInstanceOf(IIOException.class);
    }

    @Test
    public void testCompressImage() throws IOException
    {
        final File testImage = new ClassPathResource("testImage.png").getFile();
        assertTrue(testImage.exists());

        final File outputImage = new File(TMP_DIRECTORY_PATH + "/compressed.jpg");
        ImageUtils.compressImage(ImageUtils.loadImage(testImage), outputImage, 0.5f);

        assertTrue(outputImage.exists());
        assertTrue(testImage.length() > outputImage.length());
    }

    @Test
    public void testCompressImageFromString() throws IOException
    {
        final File testImage = new ClassPathResource("testImage.png").getFile();
        assertTrue(testImage.exists());
        final String outputPath = TMP_DIRECTORY_PATH + "/compressed.jpg";

        final File outputImage = new File(outputPath);
        ImageUtils.compressImage(ImageUtils.loadImage(testImage), TMP_DIRECTORY_PATH + "/compressed.jpg", 0.5f);

        assertTrue(outputImage.exists());
        assertTrue(testImage.length() > outputImage.length());
    }

    @Test
    public void testCompressImage_outputNotFound() throws IOException
    {
        final File testImage = new ClassPathResource("testImage.png").getFile();
        assertTrue(testImage.exists());

        assertThrown(() -> ImageUtils.compressImage(ImageUtils.loadImage(testImage), "/tmp/not/exist/file", 0.5f)).isInstanceOf(FileNotFoundException.class);
    }

    @Test
    public void testCompressImage_badQualityValue() throws IOException
    {
        final File testImage = new ClassPathResource("testImage.png").getFile();
        assertTrue(testImage.exists());
        assertThrown(() -> ImageUtils.compressImage(ImageUtils.loadImage(testImage), TMP_DIRECTORY_PATH + "/compressed.jpg", 22f)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testResizeImage() throws IOException
    {
        final File inputImage = new ClassPathResource("testImage.png").getFile();
        final File outputImage = new File(TMP_DIRECTORY_PATH + "/resized.jpg");

        final BufferedImage imgToResize = ImageUtils.loadImage(inputImage);
        assertNotNull(imgToResize);
        assertEquals(200, imgToResize.getWidth());
        assertEquals(200, imgToResize.getHeight());

        ImageUtils.resizeImageToFile(imgToResize, outputImage, 100);

        final BufferedImage imgResized = ImageUtils.loadImage(outputImage);
        assertNotNull(imgResized);
        assertEquals(100, imgResized.getWidth());
        assertEquals(100, imgResized.getHeight());
    }

    @Test
    public void testResizeImage_withFitExact() throws IOException
    {
        final File inputImage = new ClassPathResource("testImage.png").getFile();
        final File outputImage = new File(TMP_DIRECTORY_PATH + "/resized.jpg");

        final BufferedImage imgToResize = ImageUtils.loadImage(inputImage);
        assertNotNull(imgToResize);
        assertEquals(200, imgToResize.getWidth());
        assertEquals(200, imgToResize.getHeight());

        ImageUtils.resizeImageToFile(imgToResize, outputImage, 100, 150);

        final BufferedImage imgResized = ImageUtils.loadImage(outputImage);
        assertNotNull(imgResized);
        assertEquals(100, imgResized.getWidth());
        assertEquals(150, imgResized.getHeight());
    }

    @Test
    public void testResizeImageAndCompress() throws IOException
    {
        final File inputImage = new ClassPathResource("testImage.png").getFile();
        final File outputImage = new File(TMP_DIRECTORY_PATH + "/resized_compressed.jpg");

        final BufferedImage imgToResize = ImageUtils.loadImage(inputImage);
        assertNotNull(imgToResize);
        assertEquals(200, imgToResize.getWidth());
        assertEquals(200, imgToResize.getHeight());

        ImageUtils.resizeAndCompressImage(imgToResize, outputImage, 100, 0.5f);

        final BufferedImage imgResized = ImageUtils.loadImage(outputImage);
        assertNotNull(imgResized);
        assertEquals(100, imgResized.getWidth());
        assertEquals(100, imgResized.getHeight());
        assertTrue(inputImage.length() > outputImage.length());
    }

    @Test
    public void testResizeImageAndCompress_withFitExact() throws IOException
    {
        final File inputImage = new ClassPathResource("testImage.png").getFile();
        final File outputImage = new File(TMP_DIRECTORY_PATH + "/resized_compressed.jpg");

        final BufferedImage imgToResize = ImageUtils.loadImage(inputImage);
        assertNotNull(imgToResize);
        assertEquals(200, imgToResize.getWidth());
        assertEquals(200, imgToResize.getHeight());

        ImageUtils.resizeAndCompressImage(imgToResize, outputImage, 100, 150, 0.5f);

        final BufferedImage imgResized = ImageUtils.loadImage(outputImage);
        assertNotNull(imgResized);
        assertEquals(100, imgResized.getWidth());
        assertEquals(150, imgResized.getHeight());
        assertTrue(inputImage.length() > outputImage.length());
    }

    @Test
    public void testResizeImage_failure()
    {
        assertThrown(() -> ImageUtils.resizeImageToFile(null, new File(TMP_DIRECTORY_PATH + "/resized.jpg"), 100))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testResizeImage_withFitExact_failure()
    {
        assertThrown(() -> ImageUtils.resizeImageToFile(null, new File(TMP_DIRECTORY_PATH + "/resized.jpg"), 100, 100))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
