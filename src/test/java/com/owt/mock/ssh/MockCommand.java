package com.owt.mock.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

/**
 * Mock of a command that displays its name.
 */
public final class MockCommand implements Command
{
    /**
     * Command being executed.
     */
    private final transient String command;

    /**
     * Exit callback.
     */
    private transient ExitCallback callback;

    /**
     * Output stream for use by command.
     */
    private transient OutputStream output;

    /**
     * Constructor.
     *
     * @param cmd Command to echo.
     */
    MockCommand(final String cmd)
    {
        this.command = cmd;
    }

    @Override
    public void setInputStream(final InputStream input)
    {
        // do nothing
    }

    @Override
    public void setOutputStream(final OutputStream stream)
    {
        this.output = stream;
    }

    @Override
    public void setErrorStream(final OutputStream err)
    {
        // do nothing
    }

    @Override
    public void setExitCallback(final ExitCallback cllbck)
    {
        this.callback = cllbck;
    }

    @Override
    public void start(final Environment env) throws IOException
    {
        IOUtils.write(this.command, this.output, "UTF-8");
        this.output.flush();
        this.callback.onExit(0);
    }

    @Override
    public void destroy()
    {
        // do nothing
    }
}