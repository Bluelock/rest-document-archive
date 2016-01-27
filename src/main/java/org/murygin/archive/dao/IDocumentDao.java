package org.murygin.archive.dao;

import java.util.Date;
import java.util.List;

import org.murygin.archive.service.Document;

/**
 * Data access object to insert, find and load {@link Document}s
 * 
 * @author Daniel Murygin <daniel.murygin[at]gmail[dot]com>
 */
public interface IDocumentDao {

    /**
     * Inserts a document in the data store.
     * 
     * @param document A Document
     */
    void insert(Document document);
    
    /**
     * Returns the document from the data store with the given id.
     * The document file and meta data is returned.
     * Returns null if no document was found.
     * 
     * @param uuid The id of the document
     * @return A document incl. file and meta data
     */
    Document load(String uuid, String clientId);
    
}
