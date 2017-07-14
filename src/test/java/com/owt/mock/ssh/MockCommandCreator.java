package com.owt.mock.ssh;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.CommandFactory;

public final class MockCommandCreator implements CommandFactory
{
    @Override
    public Command createCommand(final String command)
    {
        return new MockCommand(command);
    }
}
