package com.owt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * FileUtils, utility class for files manipulation
 *
 * @author DBO Open Web Technology
 */
public final class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {

    }

    public static boolean downloadRemoteFile(final String remoteUrl, final File outputFile) {
        try {
            org.apache.commons.io.FileUtils.copyInputStreamToFile(new URL(remoteUrl).openStream(), outputFile);

            if (outputFile.exists() && outputFile.isFile()) {
                LOGGER.debug("File {} downloaded successfully in {}", remoteUrl, outputFile.getPath());
                return true;
            }
        } catch (final Exception e) {
            LOGGER.warn("Fail to download {}", remoteUrl, e);
        }
        return false;
    }

    /**
     * Create a file with content
     *
     * @param filePath
     * @param fileContent
     * @return if the file has been created
     */
    public static boolean createFileWithContent(final File file, final String fileContent) {
        try {
            if (file != null && isNotBlank(fileContent)) {
                LOGGER.debug("Write {} into file {}", fileContent, file.getName());
                org.apache.commons.io.FileUtils.writeStringToFile(file, fileContent, StandardCharsets.UTF_8);
                return true;
            }
        } catch (final IOException e) {
            LOGGER.warn("Cannot create file", e);

        }
        return false;
    }

    /**
     * Get the content of a remote file
     *
     * @param remoteUrl
     * @return each lines of the file
     */
    public static List<String> getRemoteFileContent(final String remoteUrl) {
        try {
            if (isNotBlank(remoteUrl)) {
                final InputStream remoteInputStream = new URL(remoteUrl).openStream();
                try (final BufferedReader reader = new BufferedReader(new InputStreamReader(remoteInputStream))) {
                    return reader.lines().collect(Collectors.toList());
                }
            }
        } catch (final FileNotFoundException fileNotFoundException) {
            LOGGER.info("Failed to get remote file content for {} cause {}", remoteUrl, LOGGER.isDebugEnabled() ? fileNotFoundException : fileNotFoundException.getMessage());
        } catch (final IOException e) {
            LOGGER.warn("Failed to get remote file content for {}", remoteUrl, e);
        }

        return new ArrayList<>();
    }
}
