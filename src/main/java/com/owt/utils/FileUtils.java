package com.owt.utils;

import java.io.File;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileUtils, utility class for files manipulation
 *
 * @author DBO Open Web Technology
 *
 */
public final class FileUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils()
    {

    }

    public static boolean downloadRemoteFile(final String remoteUrl, final File outputFile)
    {
        try {
            org.apache.commons.io.FileUtils.copyInputStreamToFile(new URL(remoteUrl).openStream(), outputFile);

            if (outputFile.exists() && outputFile.isFile()) {
                LOGGER.debug("File {} downloaded successfully in {}", remoteUrl, outputFile.getPath());
                return true;
            }
        }
        catch (final Exception e) {
            LOGGER.warn("Fail to download {}", remoteUrl, e);
        }
        return false;
    }
}
