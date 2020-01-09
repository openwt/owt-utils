package com.owt.mock.ssh;

import org.apache.commons.io.IOUtils;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Mock of a command that displays its name.
 */
public final class MockCommand implements Command {
    /**
     * Command being executed.
     */
    private final String command;

    /**
     * Exit callback.
     */
    private ExitCallback callback;

    /**
     * Output stream for use by command.
     */
    private OutputStream output;

    /**
     * Constructor.
     *
     * @param cmd Command to echo.
     */
    MockCommand(final String cmd) {
        command = cmd;
    }

    @Override
    public void setInputStream(final InputStream input) {
        // do nothing
    }

    @Override
    public void setOutputStream(final OutputStream stream) {
        output = stream;
    }

    @Override
    public void setErrorStream(final OutputStream err) {
        // do nothing
    }

    @Override
    public void setExitCallback(final ExitCallback cllbck) {
        callback = cllbck;
    }

    @Override
    public void start(final ChannelSession channel, final Environment env) throws IOException {
        IOUtils.write(command, output, StandardCharsets.UTF_8);
        output.flush();
        callback.onExit(0);
    }

    @Override
    public void destroy(final ChannelSession channel) {
        // do nothing
    }

}
