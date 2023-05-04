package com.elasticdemo.service;

import com.elasticdemo.model.FileContent;
import com.elasticdemo.model.Keywords;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class FileContentIndexer {

    @Autowired
    private ElasticSearchService elasticSearchService;

    public void indexFile(File file, String indexName, Keywords keywords) throws IOException {
        String content = pdfContent(file);
        // create FileContent object
        FileContent fileContent = new FileContent();
        fileContent.setId(file.getName());
        fileContent.setName(file.getName());
        fileContent.setContent(content);
        fileContent.setDownloadUrl(keywords.getDownloadUrl());
        fileContent.setBusinessProcess(keywords.getBusinessProcess());

        // create index
        elasticSearchService.createIndex(fileContent, indexName);
    }

    public void indexFiles(String directoryPath, String indexName) throws IOException {
        // iterate over files in directory
        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        // read file content
                        // String content = new String(Files.readAllBytes(file));
                        File file1 = new File(String.valueOf(file));

                        // create FileContent object
                        FileContent fileContent = new FileContent();
                        fileContent.setId(file.getFileName().toString());
                        fileContent.setName(file.getFileName().toString());
                        fileContent.setContent(pdfContent(file1));

                        // create index
                        elasticSearchService.createIndex(fileContent, indexName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private String pdfContent(File file) {
        String text = null;
        try {
            PDDocument document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            text = stripper.getText(document);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return text;
    }
}

