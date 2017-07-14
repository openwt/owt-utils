package com.owt.mock.ssh;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.auth.pubkey.UserAuthPublicKeyFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * Builder creating mock SSH servers.
 *
 */
public class MockSshServerBuilder
{

    /**
     * SSH port.
     */
    private final transient int sshport;

    /**
     * User auth factories.
     */
    private final transient List<NamedFactory<UserAuth>> authfactories;

    /**
     * Optional password authenticator.
     */
    private transient Optional<PasswordAuthenticator> passwordauth;

    /**
     * Optional public key authenticator.
     */
    private transient Optional<PublickeyAuthenticator> publickeyauth;

    /**
     * Constructor with a SSH port number.
     *
     * @param port The port number for SSH server
     */
    public MockSshServerBuilder(final int port)
    {
        this.sshport = port;
        this.authfactories = new ArrayList<>(2);
        this.passwordauth = Optional.empty();
        this.publickeyauth = Optional.empty();
    }

    /**
     * Builds a new instance of SSH server.
     *
     * @return SSH server.
     * @throws IOException
     */
    public SshServer build()
    {
        final SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(this.sshport);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File("/tmp/hostkey.ser")));
        sshd.setUserAuthFactories(this.authfactories);
        sshd.setPasswordAuthenticator(this.passwordauth.orElse(null));
        sshd.setPublickeyAuthenticator(this.publickeyauth.orElse(null));
        return sshd;
    }

    /**
     * Setup a password authentication.
     *
     * @param login Login for an authentication.
     * @param password Password for an authentication.
     * @return This instance of builder.
     */
    public MockSshServerBuilder usePasswordAuthentication(
            final String login, final String password)
    {
        this.authfactories.add(new UserAuthPasswordFactory());
        final PasswordAuthenticator authenticator = Mockito.mock(PasswordAuthenticator.class);
        Mockito.when(
                authenticator.authenticate(
                        Matchers.eq(login),
                        Matchers.eq(password),
                        Matchers.any(ServerSession.class)))
                .thenReturn(true);
        this.passwordauth = Optional.of(authenticator);
        return this;
    }

    /**
     * Setup a public key authentication.
     *
     * @return This instance of builder.
     */
    public MockSshServerBuilder usePublicKeyAuthentication()
    {
        this.authfactories.add(new UserAuthPublicKeyFactory());
        final PublickeyAuthenticator authenticator = Mockito.mock(PublickeyAuthenticator.class);
        Mockito.when(
                authenticator.authenticate(
                        Matchers.anyString(),
                        Matchers.any(PublicKey.class),
                        Matchers.any(ServerSession.class)))
                .thenReturn(true);
        this.publickeyauth = Optional.of(authenticator);
        return this;
    }

}