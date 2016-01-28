package com.bluelock.filesystem.service;


/**
 * A service to save, find and get documents from an archive. 
 * 
 * @author Daniel Murygin <daniel.murygin[at]gmail[dot]com>
 */
public interface IArchiveService {
    
    /**
     * Saves a document in the archive.
     * 
     * @param document A document
     * @return DocumentMetadata The meta data of the saved document
     */
    Document save(Document document);

    /**
     * Returns the document file from the archive with the given fileName.
     * Returns null if no document was found.
     * 
     * @param fileName The fileName of a document
     * @return A document file
     */
    byte[] getDocumentFile(String fileName, String clientId);
}
