package com.owt.utils;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import static org.imgscalr.Scalr.resize;

/**
 * ImageUtils, utility class for images manipulation
 *
 * @author DBO Open Web Technology
 */
public final class ImageUtils {
    private static final String DEFAULT_OUTPUT_FORMAT = "JPG";

    private ImageUtils() {

    }

    public static BufferedImage loadImage(final File image) throws IOException {
        return ImageIO.read(image);
    }

    public static BufferedImage loadLocalImage(final String imagePath) throws IOException {
        return loadImage(new File(imagePath));
    }

    public static BufferedImage loadRemoteImage(final String imageRemoteUrl) throws IOException {
        return ImageIO.read(new URL(imageRemoteUrl));
    }

    public static BufferedImage resizeImage(final BufferedImage inputImage, final int targetWidth, final int targetHeight) {
        return resize(inputImage, Method.QUALITY, Mode.FIT_EXACT, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }

    public static BufferedImage resizeImage(final BufferedImage inputImage, final int targetSize) {
        return resize(inputImage, Method.QUALITY, targetSize, Scalr.OP_ANTIALIAS);
    }

    public static void resizeImageToFile(final BufferedImage inputImage, final File output, final int targetWidth, final int targetHeight) throws IOException {
        writeImage(resizeImage(inputImage, targetWidth, targetHeight), output);
    }

    public static void resizeImageToFile(final BufferedImage inputImage, final File output, final int targetSize) throws IOException {
        writeImage(resizeImage(inputImage, targetSize), output);
    }

    public static BufferedImage fillTransparentPixels(final BufferedImage image) {
        final int w = image.getWidth();
        final int h = image.getHeight();
        final BufferedImage updatedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = updatedImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.drawRenderedImage(image, null);
        g.dispose();
        return updatedImage;
    }

    private static boolean isNotOpaque(final BufferedImage inputImage) {
        return inputImage.getColorModel().getTransparency() != Transparency.OPAQUE;
    }

    public static void writeImage(final BufferedImage inputImage, final File output) throws IOException {
        // ensure the image is writable
        final BufferedImage writableInputImage = isNotOpaque(inputImage) ? fillTransparentPixels(inputImage) : inputImage;
        // finally write the image !
        writeImage(writableInputImage, output, DEFAULT_OUTPUT_FORMAT);
    }

    public static void writeImage(final BufferedImage inputImage, final File output, final String outputFormat) throws IOException {
        ImageIO.write(inputImage, outputFormat, output);
    }

    public static void resizeAndCompressImage(final BufferedImage inputImage, final File outputImage, final int targetWidth, final int targetHeight, final float quality)
            throws IOException {
        compressImage(resizeImage(inputImage, targetWidth, targetHeight), outputImage, quality);
    }

    public static void resizeAndCompressImage(final BufferedImage inputImage, final File outputImage, final int targetSize, final float quality) throws IOException {
        compressImage(resizeImage(inputImage, targetSize), outputImage, quality);
    }

    public static void compressImage(final BufferedImage inputImage, final File outputImage, final float quality) throws IOException {
        try (final OutputStream os = new FileOutputStream(outputImage)) {

            // ensure the image is writable
            final BufferedImage writableInputImage = isNotOpaque(inputImage) ? fillTransparentPixels(inputImage) : inputImage;

            // configure the writer compression
            final ImageWriter writer = ImageIO.getImageWritersByFormatName(DEFAULT_OUTPUT_FORMAT).next();
            writer.setOutput(ImageIO.createImageOutputStream(os));

            final ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            // finally write the image !
            writer.write(null, new IIOImage(writableInputImage, null, null), param);
            writer.dispose();
        }
    }

    public static void compressImage(final BufferedImage image, final String outputPath, final float quality) throws IOException {
        compressImage(image, new File(outputPath), quality);
    }
}
