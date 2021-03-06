package com.owt.client;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.owt.utils.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * FTPClient encapsulates all the functionality necessary to store and retrieve files from an FTP
 * server. It's a facade of org.apache.commons.net.ftp.FTPClient
 */
public class FtpClient extends FTPClient {
    private static final String DELIMITER = "/";

    private static final String FTP_DISCONNECTED_MSG = "Unable to list remote directory, ftp is disconnected";

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpClient.class);

    // common ftp configuration
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 10000;
    private static final boolean REMOTE_FINGERPRINT_VERIFICATION = false;
    private static final boolean AUTODETECT_UTF8 = true;

    public void connect(final String url, final int port, final String username, final String password) throws IOException {
        LOGGER.info("Start ftp connection to {}", url);

        // skip the remote verification => trust the server
        setRemoteVerificationEnabled(REMOTE_FINGERPRINT_VERIFICATION);
        setConnectTimeout(CONNECTION_TIMEOUT);
        setDefaultTimeout(READ_TIMEOUT);

        //try to connect
        connect(url, port);

        //login to server
        if (!login(username, password)) {
            logout();
        }
        // check the connection is successful
        if (FTPReply.isPositiveCompletion(getReplyCode())) {

            setFileType(FTP.BINARY_FILE_TYPE);
            setAutodetectUTF8(AUTODETECT_UTF8);

            //enter passive mode
            enterLocalPassiveMode();

            LOGGER.info("Succesfully  connected to {}, Remote system is {}", url, getSystemType());
        } else {
            LOGGER.error("Unable to connect ftp succesfully  to {}", url);
            disconnect();
        }
    }

    public void cd(final String directory) throws IOException {
        if (!isConnected()) {
            throw new IOException("Unable to change remote directory, ftp is disconnected");
        }

        if (isNotEmpty(directory)) {
            // change current directory
            changeWorkingDirectory(directory);
            final String currentDir = printWorkingDirectory();
            LOGGER.info("Current directory is now {}", currentDir);
        }
    }

    public List<FTPFile> ls(final String remoteDirectory) throws IOException {
        LOGGER.debug("List files from remote directory {}", remoteDirectory);

        if (!isConnected()) {
            throw new IOException(FTP_DISCONNECTED_MSG);
        }

        return List.of(super.listFiles(remoteDirectory));
    }

    public List<FTPFile> ls(final String remoteDirectory, final String contains) throws IOException {
        LOGGER.debug("List files from remote directory {} contains {}", remoteDirectory, contains);

        if (!isConnected()) {
            throw new IOException(FTP_DISCONNECTED_MSG);
        }

        final FTPFileFilter filter = ftpFile -> ftpFile.isFile() && ftpFile.getName().contains(contains != null ? contains : "");
        return List.of(listFiles(remoteDirectory, filter));
    }

    public List<FTPFile> ls(final String remoteDirectory, final String contains, final Instant dateLimit) throws IOException {
        LOGGER.debug("List files from remote directory {} contains {} and after {}", remoteDirectory, contains, dateLimit);

        if (!isConnected()) {
            throw new IOException(FTP_DISCONNECTED_MSG);
        }

        final FTPFileFilter filters = ftpFile -> ftpFile.isFile()
                && ftpFile.getName().contains(contains != null ? contains : "")
                && ftpFile.getTimestamp().toInstant().isAfter(dateLimit);

        return Stream.of(listFiles(remoteDirectory, filters))
                .sorted(Comparator.comparingLong(f -> f.getTimestamp().getTimeInMillis()))
                .collect(Collectors.toList());
    }

    public void get(final FTPFile ftpFile, final String remoteDirectory, final String outputDirectory) throws IOException {
        if (!isConnected()) {
            throw new IOException("Unable to get remote file, ftp is disconnected");
        }

        if (ftpFile != null && ftpFile.isFile()) {
            final String remoteFilePath = remoteDirectory + DELIMITER + ftpFile.getName();
            final String filePath = outputDirectory + DELIMITER + ftpFile.getName();
            LOGGER.debug("Get ftp file {} to local file {}", remoteFilePath, filePath);

            try (final FileOutputStream fos = new FileOutputStream(filePath)) {
                try (final InputStream is = retrieveFileStream(remoteFilePath)) {
                    IOUtils.copy(is, fos);
                    fos.flush();
                }
            }

            if (completePendingCommand()) {
                LOGGER.info("Sucessfully downloaded ftp file {}", ftpFile.getName());
            }
        }

    }

    public void mget(final List<FTPFile> ftpFiles, final String remoteDirectory, final String outputDirectory) throws IOException {
        if (!isConnected()) {
            throw new IOException("Unable to get remote files, ftp is disconnected");
        }

        if (isNotEmpty(ftpFiles)) {
            for (final FTPFile ftpFile : ftpFiles) {
                get(ftpFile, remoteDirectory, outputDirectory);
            }
        }
    }

    public void mget(final List<FTPFile> ftpFiles, final String remoteDirectory, final String outputDirectory, final int limit) throws IOException {
        if (!isConnected()) {
            throw new IOException("Unable to get remote files, ftp is disconnected");
        }

        if (isNotEmpty(ftpFiles)) {

            final List<FTPFile> filesList = ftpFiles.stream().limit(limit).collect(Collectors.toList());

            for (final FTPFile ftpFile : filesList) {
                get(ftpFile, remoteDirectory, outputDirectory);
            }
        }
    }

    public void close() throws IOException {
        LOGGER.info("Closing ftp connecting");
        logout();
        disconnect();
    }
}
