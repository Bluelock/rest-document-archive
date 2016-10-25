package com.bluelock.fileloader.dao;

import com.bluelock.fileloader.service.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Data access object to insert, find and load {@link Document}s.
 * 
 * FileSystemDocumentDao saves documents in the file system. No database in involved.
 * For each document a folder is created. The folder contains the document
 * Each document in the archive has a Universally Unique Identifier (UUID).
 * 
 * @author Daniel Murygin <daniel.murygin[at]gmail[dot]com>
 */
@Service("documentDao")
public class FileSystemDocumentDao implements IDocumentDao {

    private static final Log LOG = LogFactory.getLog(FileSystemDocumentDao.class);

    @Value("${file.base.dir:/var/local/bluelock}")
    private String baseDir;
    @Value("${file.num.nested.dir:1}")
    private String numberOfNestedDirectories;

    @PostConstruct
    public void init() {
        createDirectory(baseDir);
    }

    /**
     * Inserts a document to the archive by creating a folder with the UUID
     * of the document. In the folder the document is saved and a properties file
     * with the meta data of the document. 
     * 
     * @see IDocumentDao#insert(Document)
     */
    @Override
    public void insert(Document document) {
        try {
            if(LOG.isDebugEnabled()) {
                LOG.debug("STARTED -> uploading file. [filename: " + document.getFileName() + " | clientId: " + document.getClientId() + "]");
            }
            createDirectory(document);
            saveFileData(document);
            if(LOG.isDebugEnabled()) {
                LOG.debug("FINISHED -> uploading file. [filename: " + document.getFileName() + " | clientId: " + document.getClientId() + "]");
            }
        } catch (IOException e) {
            String message = "Error while inserting document";
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
    
    /**
     * Returns the document from the data store with the given UUID.
     * 
     */
    @Override
    public Document load(String fileName, String clientId) {
        try {
            if(LOG.isDebugEnabled()) {
                LOG.debug("STARTED -> downloading file. [filename: " + fileName + " | clientId: " + clientId + "]");
            }
            return loadFromFileSystem(fileName, clientId);
        } catch (IOException e) {
            String message = "Error while loading document with id: " + fileName;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        } finally {
            LOG.debug("FINISHED -> downloading file. [filename: " + fileName + " | clientId: " + clientId + "]");
        }
        
    }
    
    private Document loadFromFileSystem(String fileName, String clientId) throws IOException {
       Path path = Paths.get(getDirectoryPath(fileName, clientId), fileName);
       Document document = new Document(null, clientId, fileName);
       document.setFileData(Files.readAllBytes(path));
       return document;
    }

    private void saveFileData(Document document) throws IOException {
        String path = getDirectoryPath(document);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(new File(path), document.getFileName())));
        stream.write(document.getFileData());
        stream.close();
    }
    
    private String createDirectory(Document document) {
        String path = getDirectoryPath(document);
        createDirectory(path);
        return path;
    }

    private String getDirectoryPath(Document document) {
       return getDirectoryPath(document.getFileName(), document.getClientId());
    }
    
    private String getDirectoryPath(String fileName, String clientId) {
        String baseFilePath = baseDir + File.separator + clientId;
        for (int i = 0; i < getNumberOfNestedDirectories(); i++) {
            baseFilePath += File.separator + fileName.toCharArray()[i];
        }
        if(LOG.isDebugEnabled()) {
            LOG.debug("Base File Path " + baseFilePath + " for file " + fileName);
        }
        return baseFilePath;
    }


    public Integer getNumberOfNestedDirectories() {
        return Integer.valueOf(numberOfNestedDirectories);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createDirectory(String path) {
        File file = new File(path);
        boolean madeDirectory = file.mkdirs();
        if(!madeDirectory) {
            LOG.error("Unable to make directory.");
        }
    }

}
