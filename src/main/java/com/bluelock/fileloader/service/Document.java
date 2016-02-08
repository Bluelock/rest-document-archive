package com.bluelock.fileloader.service;

import java.io.Serializable;

/**
 * A document from an archive managed by {@link IArchiveService}.
 * 
 * @author Daniel Murygin <daniel.murygin[at]gmail[dot]com>
 */
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String PROP_FILE_DATA = "file";

    private String clientId;
    protected String fileName;
    private byte[] fileData;

    public Document(byte[] fileData, String clientId, String fileName) {
        this.fileData = fileData;
        this.clientId = clientId;
        this.fileName = fileName;
    }

    public String getClientId() {
        return clientId;
    }

    @SuppressWarnings("unused")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getFileName() {
        return fileName;
    }

    @SuppressWarnings("unused")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
