package com.bluelock.fileloader.rest;

import org.apache.log4j.Logger;
import com.bluelock.fileloader.service.Document;
import com.bluelock.fileloader.service.IArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * REST web service for archive service {@link IArchiveService}.
 *
 * 
 * All service calls are delegated to instances of {@link IArchiveService}.
 * 
 * @author Daniel Murygin <daniel.murygin[at]gmail[dot]com>
 */
@Controller
@RequestMapping(value = "/bluelock")
public class ArchiveController {

    private static final Logger LOG = Logger.getLogger(ArchiveController.class);
    
    @Autowired
    IArchiveService archiveService;

    /**
     * Adds a document to the archive.
     * 
     * Url: /bluelock/upload/{clientId}?file={file} [POST]
     * 
     * @return The meta data of the added document
     */
    @RequestMapping(value = "/upload/{clientId}", method = RequestMethod.POST)
    public @ResponseBody String handleFileUpload(
            @PathVariable String clientId,
            @RequestParam(value="file", required=true) MultipartFile file) {
        
        try {
            String fileName = UUID.randomUUID().toString();
            Document document = new Document(file.getBytes(), clientId, fileName);
            return getArchiveService().save(document).getFileName();
        } catch (RuntimeException e) {
            LOG.error("Error while uploading.", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Error while uploading.", e);
            throw new RuntimeException(e);
        }      
    }
    
    /**
     * Returns the document file from the archive with the given UUID.
     * 
     * Url: /bluelock/document/{clientId}/{fileName} [GET]
     *
     * @return The document file
     */
    @RequestMapping(value = "/document/{clientId}/{fileName:.+}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getDocument(@PathVariable String clientId, @PathVariable String fileName) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<byte[]>(getArchiveService().getDocumentFile(fileName, clientId), httpHeaders, HttpStatus.OK);
        } catch (RuntimeException e) {
            LOG.error("Error while downloading.", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Error while downloading.", e);
            throw new RuntimeException(e);
        }
}

    public IArchiveService getArchiveService() {
        return archiveService;
    }

    public void setArchiveService(IArchiveService archiveService) {
        this.archiveService = archiveService;
    }

}
