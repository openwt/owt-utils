package com.owt.client;

import com.jcabi.ssh.Shell;
import com.jcabi.ssh.Ssh;
import org.apache.commons.io.input.NullInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import static org.apache.commons.lang3.StringUtils.isNoneEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * SshUtils, utility class for ssh activity (ssh/scp) using SSHClient
 *
 * @author DBO Open Web Technology
 */
public final class SshClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SshClient.class);

    private SshClient() {

    }

    public static Shell connect(final String hostname, final String username, final String privateKey) {
        return connect(hostname, username, Ssh.PORT, privateKey);
    }

    public static Shell connect(final String hostname, final String username, final int port, final String privateKey) {
        if (isNoneEmpty(hostname, username, privateKey)) {
            try {
                final Ssh shell = new Ssh(hostname, port, username, privateKey);

                if (isConnected(shell)) {
                    return shell;
                }
            } catch (final Exception e) {
                LOGGER.info("Fail to connect over ssh to {}", hostname, e);
            }
        }

        return null;
    }

    private static boolean isConnected(final Shell shell) throws IOException {
        return shell != null && executeRemoteCmd(shell, "echo 'connected?'") == 0;
    }

    public static boolean upload(final Shell shell, final String localPath, final String remotePath) {
        if (isNoneEmpty(localPath, remotePath)) {
            try {
                try (final InputStream inputStream = new FileInputStream(localPath)) {
                    return shell != null && executeRemoteCmd(shell, String.format("cat > %s", Ssh.escape(remotePath)), inputStream) == 0;
                }
            } catch (final IOException e) {
                LOGGER.info("Fail to scp upload {} to {}", localPath, remotePath, e);
            }
        }
        return false;
    }

    public static boolean createDirectory(final Shell shell, final String remoteDirectory) {
        if (isNotEmpty(remoteDirectory)) {
            try {
                return shell != null && executeRemoteCmd(shell, String.format("mkdir -p %s", Ssh.escape(remoteDirectory))) == 0;
            } catch (final IOException e) {
                LOGGER.info("Fail to create remote directory {}", remoteDirectory, e);
            }
        }
        return false;
    }

    public static boolean delete(final Shell shell, final String remotePath) {
        if (isNotEmpty(remotePath)) {
            try {
                return shell != null && executeRemoteCmd(shell, String.format("rm -f %s", Ssh.escape(remotePath))) == 0;
            } catch (final IOException e) {
                LOGGER.info("Fail to remote delete {}", remotePath, e);
            }
        }
        return false;
    }

    public static boolean deleteDirectory(final Shell shell, final String remotePath) {
        if (isNotEmpty(remotePath)) {
            try {
                return shell != null && executeRemoteCmd(shell, String.format("rm -rf %s", Ssh.escape(remotePath))) == 0;
            } catch (final IOException e) {
                LOGGER.info("Fail to remote delete {}", remotePath, e);
            }
        }
        return false;
    }

    private static int executeRemoteCmd(final Shell shell, final String cmd) throws IOException {
        try (final NullInputStream nullStream = new NullInputStream(0L)) {
            return executeRemoteCmd(shell, cmd, nullStream);
        }
    }

    private static int executeRemoteCmd(final Shell shell, final String cmd, final InputStream inputStream) throws IOException {
        return new Shell.Safe(shell)
                .exec(cmd,
                        inputStream,
                        com.jcabi.log.Logger.stream(Level.INFO, true),
                        com.jcabi.log.Logger.stream(Level.WARNING, true));
    }

}
