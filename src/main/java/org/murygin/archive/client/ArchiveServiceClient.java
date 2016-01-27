package org.murygin.archive.client;

import org.apache.log4j.Logger;
import org.murygin.archive.service.Document;
import org.murygin.archive.service.IArchiveService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
public class ArchiveServiceClient implements IArchiveService {

    private static final Logger LOG = Logger.getLogger(ArchiveServiceClient.class);

    String protocol = "http";
    @Value("${hostname:localhost}")
    String hostname;
    @Value("${port:8080}")
    Integer port;
    @Value("${url:bluelock}")
    String baseUrl;
    
    RestTemplate restTemplate;
    
    @Override
    public Document save(Document document) {
        try {          
            return doSave(document);
        } catch (RuntimeException e) {
            LOG.error("Error while uploading file", e);
            throw e;
        } catch (IOException e) {
            LOG.error("Error while uploading file", e);
            throw new RuntimeException("Error while uploading file", e);
        }

    }

    private Document doSave(Document document) throws IOException {
        String tempFilePath = writeDocumentToTempFile(document);
        MultiValueMap<String, Object> parts = createMultipartFileParam(tempFilePath);
        return getRestTemplate().postForObject(getServiceUrl() + "/upload/" + document.getClientId(),
                parts,
                Document.class);
    }

    @Override
    public byte[] getDocumentFile(String fileName, String clientId) {
        return getRestTemplate().getForObject(getServiceUrl() +  "/document/{clientId}/{fileName}", byte[].class, clientId, fileName);
    }

    private MultiValueMap<String, Object> createMultipartFileParam(String tempFilePath) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();           
        parts.add(Document.PROP_FILE_DATA, new FileSystemResource(tempFilePath));
        return parts;
    }

    private String writeDocumentToTempFile(Document document) throws IOException {
        Path path;       
        path = Files.createTempDirectory(document.getFileName());
        String tempDirPath = path.toString();
        File file = new File(tempDirPath,document.getFileName());
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(document.getFileData());    
        fo.close();
        return file.getPath();
    }
    
    public String getServiceUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(getProtocol()).append("://");
        sb.append(getHostname());
        if(getPort()!=null) {
            sb.append(":").append(getPort());
        }
        sb.append("/").append(getBaseUrl()).append("/");
        return sb.toString();
    }

    public String getProtocol() {
        return protocol;
    }

    @SuppressWarnings("unused")
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHostname() {
        return hostname;
    }

    @SuppressWarnings("unused")
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    @SuppressWarnings("unused")
    public void setPort(Integer port) {
        this.port = port;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    @SuppressWarnings("unused")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public RestTemplate getRestTemplate() {
        if(restTemplate==null) {
            restTemplate = createRestTemplate(); 
        }
        return restTemplate;
    }

    @SuppressWarnings("unused")
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private RestTemplate createRestTemplate() {
        restTemplate = new RestTemplate();
        return restTemplate;
    }

}
