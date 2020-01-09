package com.owt.mock.ssh;

import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;

public final class MockCommandCreator implements CommandFactory {

    @Override
    public Command createCommand(final ChannelSession channel, final String command) {
        return new MockCommand(command);
    }
}
