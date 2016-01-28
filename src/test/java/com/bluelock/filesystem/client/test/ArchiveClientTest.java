package com.bluelock.filesystem.client.test;

import com.bluelock.filesystem.Application;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.bluelock.filesystem.client.ArchiveServiceClient;
import com.bluelock.filesystem.service.Document;
import com.bluelock.filesystem.service.IArchiveService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ArchiveClientTest {

    private static final Logger LOG = Logger.getLogger(ArchiveClientTest.class);
    
    private static final String TEST_FILE_DIR = "test-images";

    @Value("${file.base.dir:/var/local/bluelock}")
    private String baseDir;

    IArchiveService client;

    @Before
    public void setUp() throws IOException {
        baseDir += "/" + getClientId();
        client = new ArchiveServiceClient();
    }

    @After
    public void tearDown() {
        deleteDirectory(new File(baseDir));
    }

    @Ignore
    @Test
    public void testUpload() throws IOException {
        List<String> fileList = getFileList();
        for (String fileName : fileList) {
            uploadFile(fileName);
        }
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private void uploadFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(TEST_FILE_DIR).append(File.separator).append(fileName);
        Path path = Paths.get(sb.toString());
        byte[] fileData = Files.readAllBytes(path);
        String clientId = getClientId();
        Document saved = client.save(new Document(fileData, clientId, fileName));
        byte[] retrieved = client.getDocumentFile(saved.getFileName(), saved.getClientId());
        Assert.isTrue(Arrays.equals(saved.getFileData(), retrieved));
        if (LOG.isDebugEnabled()) {
            LOG.debug("Document saved, file: " + saved.getFileName());
        }
    }

    private String getClientId() {
        return Integer.toString(this.getClass().getSimpleName().length());
    }

    private List<String> getFileList() {
        File file = new File(TEST_FILE_DIR);
        String[] files = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isFile();
            }
        });
        return Arrays.asList(files);
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return (path.delete());
    }
}
