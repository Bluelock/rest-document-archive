package com.bluelock.fileloader.service;

import java.io.Serializable;

import com.bluelock.fileloader.dao.IDocumentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service to save, find and get documents from an archive. 
 * 
 * @author Daniel Murygin <daniel.murygin[at]gmail[dot]com>
 */
@Service("archiveService")
public class ArchiveService implements IArchiveService, Serializable {

    private static final long serialVersionUID = 8119784722798361327L;
    
    @Autowired
    private IDocumentDao DocumentDao;

    /**
     * Saves a document in the archive.
     * @see IArchiveService#save(Document)
     */
    @Override
    public Document save(Document document) {
        getDocumentDao().insert(document); 
        return document;
    }

    /**
     * Returns the document file from the archive
     */
    @Override
    public byte[] getDocumentFile(String fileName, String clientId) {
        Document document = getDocumentDao().load(fileName, clientId);
        if(document!=null) {
            return document.getFileData();
        } else {
            return null;
        }
    }


    public IDocumentDao getDocumentDao() {
        return DocumentDao;
    }

    public void setDocumentDao(IDocumentDao documentDao) {
        DocumentDao = documentDao;
    }


}
