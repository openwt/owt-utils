package com.owt.mock.ssh;

import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.auth.pubkey.UserAuthPublicKeyFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

/**
 * Builder creating mock SSH servers.
 */
public class MockSshServerBuilder {

    /**
     * SSH port.
     */
    private final int sshport;

    /**
     * User auth factories.
     */
    private final List<NamedFactory<UserAuth>> authfactories;

    /**
     * Optional password authenticator.
     */
    private PasswordAuthenticator passwordauth;

    /**
     * Optional public key authenticator.
     */
    private PublickeyAuthenticator publickeyauth;

    /**
     * Constructor with a SSH port number.
     *
     * @param port The port number for SSH server
     */
    public MockSshServerBuilder(final int port) {
        sshport = port;
        authfactories = new ArrayList<>(2);
        passwordauth = null;
        publickeyauth = null;
    }

    /**
     * Builds a new instance of SSH server.
     *
     * @return SSH server.
     */
    public SshServer build() {
        final SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(sshport);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Path.of("/tmp/hostkey.ser")));
        sshd.setUserAuthFactories(authfactories);
        sshd.setPasswordAuthenticator(passwordauth);
        sshd.setPublickeyAuthenticator(publickeyauth);
        return sshd;
    }

    /**
     * Setup a password authentication.
     *
     * @param login    Login for an authentication.
     * @param password Password for an authentication.
     * @return This instance of builder.
     */
    public MockSshServerBuilder usePasswordAuthentication(final String login, final String password) {
        authfactories.add(new UserAuthPasswordFactory());
        final PasswordAuthenticator authenticator = Mockito.mock(PasswordAuthenticator.class);
        Mockito.when(
                authenticator.authenticate(
                        eq(login),
                        eq(password),
                        any(ServerSession.class)))
                .thenReturn(true);
        passwordauth = authenticator;
        return this;
    }

    /**
     * Setup a public key authentication.
     *
     * @return This instance of builder.
     */
    public MockSshServerBuilder usePublicKeyAuthentication() {
        authfactories.add(new UserAuthPublicKeyFactory());
        final PublickeyAuthenticator authenticator = Mockito.mock(PublickeyAuthenticator.class);
        Mockito.when(
                authenticator.authenticate(
                        anyString(),
                        any(PublicKey.class),
                        any(ServerSession.class)))
                .thenReturn(true);
        publickeyauth = authenticator;
        return this;
    }

}
