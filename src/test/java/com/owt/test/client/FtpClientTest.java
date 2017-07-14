package com.owt.test.client;

import static com.owt.test.ThrowableAssertion.assertThrown;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.test.context.junit4.SpringRunner;

import com.owt.client.FtpClient;

@RunWith(SpringRunner.class)
public class FtpClientTest
{
    private static final String HOST = "localhost";
    private static final int PORT = 22222;
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String REMOTE_ROOT_DIR = "/";
    private static final String REMOTE_DIR = "/dir";
    private static final String LOCAL_DIR = "/tmp/ftp_output";
    private static final File TMP_DIRECTORY = new File(LOCAL_DIR);
    private static final String FILE2 = "sample.txt";
    private static final String FILE = "test.log";

    private static FakeFtpServer fakeFtpServer;

    @BeforeClass
    public static void setup() throws Exception
    {
        // setup local directory
        FileUtils.deleteDirectory(TMP_DIRECTORY);
        TMP_DIRECTORY.mkdir();

        // setup mock ftp
        fakeFtpServer = new FakeFtpServer();

        fakeFtpServer.setServerControlPort(PORT); // use any free port

        final FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new FileEntry(REMOTE_DIR + "/" + FILE, "abcdef 1234567890"));
        fileSystem.add(new FileEntry(REMOTE_DIR + "/" + FILE2, "trololo"));
        fakeFtpServer.setFileSystem(fileSystem);

        final UserAccount userAccount = new UserAccount(USERNAME, PASSWORD, REMOTE_ROOT_DIR);
        fakeFtpServer.addUserAccount(userAccount);

        fakeFtpServer.start();
    }

    @After
    public void tearDown() throws Exception
    {
        // delete tmp dir
        FileUtils.deleteDirectory(TMP_DIRECTORY);
        TMP_DIRECTORY.mkdir();
    }

    @AfterClass
    public static void afterClass()
    {
        // stop ftp
        fakeFtpServer.stop();
    }

    @Test
    public void testFtpConnect() throws Exception
    {
        final FtpClient ftp = new FtpClient();
        ftp.connect(HOST, PORT, USERNAME, PASSWORD);

        assertNotNull(ftp);
        assertTrue(ftp.isAvailable());
        assertTrue(ftp.isConnected());
    }

    @Test
    public void testFtpConnect_Failure()
    {
        final FtpClient ftp = new FtpClient();
        assertThrown(() -> ftp.connect("pas_glop", PORT, USERNAME, PASSWORD)).isInstanceOf(UnknownHostException.class);

        assertNotNull(ftp);
        assertFalse(ftp.isAvailable());
        assertFalse(ftp.isConnected());
    }

    @Test
    public void testFtpBadUser()
    {
        final FtpClient ftp = new FtpClient();
        assertThrown(() -> ftp.connect(HOST, PORT, "YOLO", "YOLO")).isInstanceOf(IOException.class);
    }

    @Test
    public void testFtpClose() throws Exception
    {
        final FtpClient ftp = new FtpClient();
        ftp.connect(HOST, PORT, USERNAME, PASSWORD);

        assertNotNull(ftp);
        assertTrue(ftp.isAvailable());
        assertTrue(ftp.isConnected());

        ftp.close();
        assertFalse(ftp.isAvailable());
        assertFalse(ftp.isConnected());
    }

    @Test
    public void testFtpCd() throws Exception
    {
        final FtpClient ftp = new FtpClient();
        ftp.connect(HOST, PORT, USERNAME, PASSWORD);

        assertEquals(REMOTE_ROOT_DIR, ftp.printWorkingDirectory());
        ftp.cd("/dir");
        assertEquals("/dir", ftp.printWorkingDirectory());
    }

    @Test
    public void testFtpCd_Failure() throws Exception
    {
        final FtpClient ftp = new FtpClient();
        ftp.connect(HOST, PORT, USERNAME, PASSWORD);

        assertEquals(REMOTE_ROOT_DIR, ftp.printWorkingDirectory());
        ftp.cd("/unknown");
        assertEquals(REMOTE_ROOT_DIR, ftp.printWorkingDirectory());
    }

    @Test
    public void testFtpCd_NotConnected()
    {
        final FtpClient ftp = new FtpClient();
        assertThrown(() -> ftp.cd("/dir")).isInstanceOf(IOException.class);
    }

    @Test
    public void testFtpListFiles() throws Exception
    {
        final FtpClient ftp = new FtpClient();
        ftp.connect(HOST, PORT, USERNAME, PASSWORD);

        List<FTPFile> files = ftp.ls(REMOTE_DIR);

        assertEquals(2, files.size());

        assertNotNull(files.get(0));
        assertEquals(FILE, files.get(0).getName());
        assertTrue(files.get(1).isFile());

        assertNotNull(files.get(1));
        assertEquals(FILE2, files.get(1).getName());
        assertTrue(files.get(1).isFile());

        // equivalent to list "/"
        files = ftp.ls(null);

        assertNotNull(files);
        assertEquals(1, files.size());

        assertEquals("dir", files.get(0).getName());
        assertTrue(files.get(0).isDirectory());
    }

    @Test
    public void testFtpListFiles_NotConnected()
    {
        final FtpClient ftp = new FtpClient();
        assertThrown(() -> ftp.listFiles(REMOTE_DIR)).isInstanceOf(IOException.class);
    }

    @Test
    public void testFtpListFiles_Failure() throws Exception
    {
        final FtpClient ftp = new FtpClient();
        ftp.connect(HOST, PORT, USERNAME, PASSWORD);

        final List<FTPFile> files = ftp.ls("/unknown");

        assertNotNull(files);
        assertEquals(0, files.size());
    }

    @Test
    public void testFtpListFilesWithFilter() throws Exception
    {
        final FtpClient ftp = new FtpClient();
        ftp.connect(HOST, PORT, USERNAME, PASSWORD);

        final List<FTPFile> files = ftp.ls(REMOTE_DIR, ".txt");

        assertEquals(1, files.size());
        assertNotNull(files.get(0));
        assertEquals(FILE2, files.get(0).getName());

        // equivalent to  ftp.list(REMOTE_DIR)
        final List<FTPFile> files2 = ftp.ls(REMOTE_DIR, "");
        assertEquals(2, files2.size());

        // also equivalent to  ftp.list(REMOTE_DIR)
        final List<FTPFile> files3 = ftp.ls(REMOTE_DIR, null);
        assertEquals(2, files3.size());

    }

    @Test
    public void testFtpListFilesWithFilter_NotConnected()
    {
        final FtpClient ftp = new FtpClient();
        assertThrown(() -> ftp.ls(REMOTE_DIR, ".txt")).isInstanceOf(IOException.class);
    }

    @Test
    public void testFtpListFilesWithFilterNull() throws Exception
    {
        final FtpClient ftp = new FtpClient();
        ftp.connect(HOST, PORT, USERNAME, PASSWORD);

        final List<FTPFile> files4 = ftp.ls(null, null);
        assertNotNull(files4);
        assertEquals(0, files4.size());
    }

    @Test
    public void testFtpGet() throws Exception
    {
        final FtpClient ftp = new FtpClient();
        ftp.connect(HOST, PORT, USERNAME, PASSWORD);

        final List<FTPFile> files = ftp.ls(REMOTE_DIR);

        assertEquals(2, files.size());

        assertNotNull(files.get(0));
        assertEquals(FILE, files.get(0).getName());
        assertTrue(files.get(1).isFile());

        assertNotNull(files.get(1));
        assertEquals(FILE2, files.get(1).getName());
        assertTrue(files.get(1).isFile());

        ftp.mget(files, REMOTE_DIR, LOCAL_DIR);

        final File[] localeFiles = TMP_DIRECTORY.listFiles();

        assertEquals(2, localeFiles.length);

        assertNotNull(localeFiles[0]);
        assertThat(localeFiles[0].getName(), anyOf(equalTo(FILE), equalTo(FILE2)));
        assertTrue(localeFiles[0].isFile());
        assertThat(files.get(0).getSize(), anyOf(equalTo(localeFiles[0].length()), equalTo(localeFiles[1].length())));

        assertNotNull(localeFiles[1]);
        assertThat(localeFiles[1].getName(), anyOf(equalTo(FILE), equalTo(FILE2)));
        assertTrue(localeFiles[1].isFile());
        assertThat(files.get(1).getSize(), anyOf(equalTo(localeFiles[0].length()), equalTo(localeFiles[1].length())));
    }

    @Test
    public void testFtpGetWithFilter() throws Exception
    {
        final FtpClient ftp = new FtpClient();
        ftp.connect(HOST, PORT, USERNAME, PASSWORD);
        ftp.mget(ftp.ls(REMOTE_DIR, ".log"), REMOTE_DIR, LOCAL_DIR, 2);

        final File[] localeFiles = TMP_DIRECTORY.listFiles();

        assertEquals(1, localeFiles.length);
        assertNotNull(localeFiles[0]);
        assertEquals(FILE, localeFiles[0].getName());
        assertTrue(localeFiles[0].isFile());
    }

    @Test
    public void testFtpGetWithFilterAndLimit() throws Exception
    {
        final FtpClient ftp = new FtpClient();
        ftp.connect(HOST, PORT, USERNAME, PASSWORD);
        ftp.mget(ftp.ls(REMOTE_DIR), REMOTE_DIR, LOCAL_DIR, 1);

        final File[] localeFiles = TMP_DIRECTORY.listFiles();

        assertEquals(1, localeFiles.length);
        assertNotNull(localeFiles[0]);
        assertEquals(FILE, localeFiles[0].getName());
        assertTrue(localeFiles[0].isFile());
    }

    @Test
    public void testFtpGet_AlreadyExists() throws Exception
    {
        final FtpClient ftp = new FtpClient();
        ftp.connect(HOST, PORT, USERNAME, PASSWORD);

        final List<FTPFile> files = ftp.ls(REMOTE_DIR);

        ftp.mget(files, REMOTE_DIR, LOCAL_DIR);
        File[] localeFiles = TMP_DIRECTORY.listFiles();
        assertEquals(2, localeFiles.length);

        assertNotNull(localeFiles[0]);
        assertThat(localeFiles[0].getName(), anyOf(equalTo(FILE), equalTo(FILE2)));
        assertTrue(localeFiles[0].isFile());
        assertThat(files.get(0).getSize(), anyOf(equalTo(localeFiles[0].length()), equalTo(localeFiles[1].length())));

        assertNotNull(localeFiles[1]);
        assertThat(localeFiles[1].getName(), anyOf(equalTo(FILE), equalTo(FILE2)));
        assertTrue(localeFiles[1].isFile());
        assertThat(files.get(1).getSize(), anyOf(equalTo(localeFiles[0].length()), equalTo(localeFiles[1].length())));

        // download the same file again
        ftp.mget(files, REMOTE_DIR, LOCAL_DIR);
        localeFiles = TMP_DIRECTORY.listFiles();
        assertEquals(2, localeFiles.length);

        assertNotNull(localeFiles[0]);
        assertThat(localeFiles[0].getName(), anyOf(equalTo(FILE), equalTo(FILE2)));
        assertTrue(localeFiles[0].isFile());
        assertThat(files.get(0).getSize(), anyOf(equalTo(localeFiles[0].length()), equalTo(localeFiles[1].length())));

        assertNotNull(localeFiles[1]);
        assertThat(localeFiles[1].getName(), anyOf(equalTo(FILE), equalTo(FILE2)));
        assertTrue(localeFiles[1].isFile());
        assertThat(files.get(1).getSize(), anyOf(equalTo(localeFiles[0].length()), equalTo(localeFiles[1].length())));
    }

    @Test
    public void testFtpGet_fail() throws Exception
    {
        final FtpClient ftp = new FtpClient();
        ftp.connect(HOST, PORT, USERNAME, PASSWORD);

        assertThrown(() -> ftp.mget(ftp.ls(REMOTE_DIR), REMOTE_DIR, "/CA_EXIST_PAS")).isInstanceOf(FileNotFoundException.class);

        assertThrown(() -> ftp.mget(ftp.ls(REMOTE_DIR), REMOTE_DIR, null)).isInstanceOf(FileNotFoundException.class);
    }

    @Test
    public void testFtpGet_NotConnected()
    {
        final FtpClient ftp = new FtpClient();
        assertThrown(() -> ftp.mget(new ArrayList<>(), REMOTE_DIR, LOCAL_DIR)).isInstanceOf(IOException.class);
    }
}
