package com.owt.test.client;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;

import org.apache.commons.io.input.NullInputStream;
import org.apache.sshd.server.SshServer;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.jcabi.ssh.SSH;
import com.jcabi.ssh.Shell;
import com.owt.client.SshClient;
import com.owt.mock.ssh.MockCommandCreator;
import com.owt.mock.ssh.MockSshServerBuilder;

@RunWith(SpringRunner.class)
public class SshClientTest
{

    /**
     * test connect ssh with a private key
     *
     * @throws IOException
     */
    @Test
    public void testSSh() throws IOException
    {
        final int port = getPort();

        final MockSshServerBuilder mockSshServerBuilder = new MockSshServerBuilder(port).usePublicKeyAuthentication();

        try (final SshServer sshd = mockSshServerBuilder.build()) {
            sshd.setCommandFactory(new MockCommandCreator());
            sshd.start();

            final String privateKey = "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDDosdMm9d8w+h3dO5V7OvUw9t+tzXgXbCoyDwFq/pAjEl6bI1LXEO5Bcm6TDzCEGjFDnwGR82e567zKDmlW3QCHNz8qNhKS+ro+6WWHIawLSY++7CkgzcEqppv2OFuJ/MmX6BurkCVHUL2HrMWw6fWEQPVCo9B5/s4pt+qW3mBnH9xcLwk79qqFvAhqw1JJoTr67LM+cvOOwbKEhdx18ndhEow3Qz1QmXlov0+xR5apReezqxMlAuyNBiBbo+ewTknV6lnOZSt1ffNzmKGRPPxCmxOERVREH1Li6vaGRPdYds4wo9bn6sE0oKbUN4wfFMfeNaSj5SIpsfOwz71nUmpAgMBAAECggEAY5k6p9jrUvyWHELul9Y/lhmEZLW3exuXtcYDMMwGYthJDEe5g3VckRTOR8dKY25hOHmQUM47c7v2CJshBftSS0UQiJ0ZSHHwDV8YRR0OwSCrSQNwQcD/wKNubqTW1/MEyjODBPY3sPKtxYmJmXxQtR4CenKezmnhL01dr2hUqtPxY1Dp8C0NkqREPoGtoV22sY3NQ4vN4FE/d69h6K6mcke58BkxUm8f6gYppyGK1s1oTTUlB9VMkiActGv5+qoFFAFiCqYH8uAkOSS+Ujvh4Onj3R6MIbIyBEgSOecBVG96S7WF3BkH5Hyvhe18uPLMMw+BLbIejLYuMZhtyIRHAQKBgQD74MtOXiovEi2bus+JUlzojOamNt5xXFXyZTtmI90HjRh+QBLGE/iTtIXez4+TZ3hNw2qa6MHAjITXLJ38lcte1YYSGJ1BAKkSOJ4H/3YHQCNLU7VbsIWsp2Nt4xRK9ijQtIzeDpKE82K8/ST2SfMse8Dtu4VM/C65qtJyuiPcSQKBgQDG1l2mDhr8LqaX5IeHaKHxRIWlK79JAvY61a6uueBm/ITG/jdlGw95n+ZA+YYLirl1jV0Zam7MZAkUt9Nq+Ipv1HFtuIJECFOU9KsZG/bYRoe4aggNYWyMYZKey3OF8qA9uQHOKytlBMuTaxQvPPEbfAecoBvi8BAVQyvj3VxCYQKBgCpUfxZukNxUS8HuRU6ovuB+PY0Vj5CxmeggxzjFMjJArisZjY4ocRAxuIXXkCOPshIvk0zxbQ51DyYyY2K9se4AKIYGCPeRqthbF4P0lNtnFgdkzAfaxLxdbUJcdV6bvARHM51mCY26cY+dY4bYqruBGM1bRG3Ju/iMEs4hJkGxAoGAens+ALYbpVBPSq15Vs37Q5WEBbSnBiCO8LsD+ZmCQ641VCbwlJVQuQjoWwnZqm5vw5uqx6cqTTkp5ts9nl0vWaexn3bSt3mxksYMR1FUgvEfVVfrUeK7bNYF7U9CADbuwlaeaBNrjbSgelSxgE+37cg/IBn4U5ixL4fLF3gaBsECgYEAhlA8PzR9U+essidvYKZivhoNY+hQtm0f7zSLhOYsaXXFREeZsbRSRPzRS+N66/L/fRcxCwZVYX7nlqtXvTjf7RqeQbVy9SuJHE7ZTspiAHbbr+VXqbcDfJlur1KRDa+OgQ/Gfg/GnSz2HLuhMt1/t+125aRICa7pdvcMxO4UmWI=\n-----END PRIVATE KEY-----";

            final String cmd = "some test command";
            final Shell client = SshClient.connect(InetAddress.getLocalHost().getCanonicalHostName(), "test", port, privateKey);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();

            try (InputStream is = new NullInputStream(0L)) {
                final int exit = client.exec(
                        cmd,
                        is,
                        output,
                        com.jcabi.log.Logger.stream(java.util.logging.Level.WARNING, true));
                MatcherAssert.assertThat(exit, Matchers.is(0));
                MatcherAssert.assertThat(output.toString(), Matchers.is(cmd));
            }
        }
    }

    @Test
    public void testSSh_BadPrivateKey() throws IOException
    {
        final int port = getPort();

        final MockSshServerBuilder mockSshServerBuilder = new MockSshServerBuilder(port).usePublicKeyAuthentication();

        try (final SshServer sshd = mockSshServerBuilder.build()) {
            sshd.setCommandFactory(new MockCommandCreator());
            sshd.start();

            final String privateKey = "this private key is wrong!";
            final Shell client = SshClient.connect(InetAddress.getLocalHost().getCanonicalHostName(), "test", port, privateKey);
            assertNull(client);
        }
    }

    @Test
    public void testSSh_InvalidHost() throws IOException
    {
        final int port = getPort();

        final MockSshServerBuilder mockSshServerBuilder = new MockSshServerBuilder(port).usePublicKeyAuthentication();

        try (final SshServer sshd = mockSshServerBuilder.build()) {
            sshd.setCommandFactory(new MockCommandCreator());
            sshd.start();

            final String privateKey = "some_key";
            final Shell client = SshClient.connect("123.123.123.123", "test", port, privateKey);
            assertNull(client);
        }
    }

    /**
     * Allocate free port.
     *
     * @return Found port.
     * @throws IOException In case of error.
     */
    private static int getPort() throws IOException
    {
        int port = SSH.PORT;

        try (final ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }

        return port;
    }
}
