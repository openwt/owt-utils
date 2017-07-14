package com.owt.utils;

import static org.imgscalr.Scalr.resize;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

/**
 * ImageUtils, utility class for images manipulation
 *
 * @author DBO Open Web Technology
 *
 */
public final class ImageUtils
{
    private static final String OUTPUT_FORMAT = "JPG";

    private ImageUtils()
    {

    }

    public static BufferedImage loadImage(final File image) throws IOException
    {
        return ImageIO.read(image);
    }

    public static BufferedImage loadLocalImage(final String imagePath) throws IOException
    {
        return loadImage(new File(imagePath));
    }

    public static BufferedImage loadRemoteImage(final String imageRemoteUrl) throws IOException
    {
        return ImageIO.read(new URL(imageRemoteUrl));
    }

    public static BufferedImage resizeImage(final BufferedImage inputImage, final int targetWidth, final int targetHeight)
    {
        return resize(inputImage, Method.QUALITY, Mode.FIT_EXACT, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }

    public static BufferedImage resizeImage(final BufferedImage inputImage, final int targetSize)
    {
        return resize(inputImage, Method.QUALITY, targetSize, Scalr.OP_ANTIALIAS);
    }

    public static void resizeImageToFile(final BufferedImage inputImage, final File output, final int targetWidth, final int targetHeight) throws IOException
    {
        ImageIO.write(resizeImage(inputImage, targetWidth, targetHeight), OUTPUT_FORMAT, output);
    }

    public static void resizeImageToFile(final BufferedImage inputImage, final File output, final int targetSize) throws IOException
    {
        ImageIO.write(resizeImage(inputImage, targetSize), OUTPUT_FORMAT, output);
    }

    public static void resizeAndCompressImage(final BufferedImage inputImage, final File outputImage, final int targetWidth, final int targetHeight, final float quality)
            throws IOException
    {
        compressImage(resizeImage(inputImage, targetWidth, targetHeight), outputImage, quality);
    }

    public static void resizeAndCompressImage(final BufferedImage inputImage, final File outputImage, final int targetSize, final float quality) throws IOException
    {
        compressImage(resizeImage(inputImage, targetSize), outputImage, quality);
    }

    public static void compressImage(final BufferedImage image, final File outputImage, final float quality) throws IOException
    {
        try (OutputStream os = new FileOutputStream(outputImage)) {

            final ImageWriter writer = ImageIO.getImageWritersByFormatName(OUTPUT_FORMAT).next();
            writer.setOutput(ImageIO.createImageOutputStream(os));

            final ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
            writer.write(null, new IIOImage(image, null, null), param);
            os.close();
            writer.dispose();
        }
    }

    public static void compressImage(final BufferedImage image, final String outputPath, final float quality) throws IOException
    {
        compressImage(image, new File(outputPath), quality);
    }
}
