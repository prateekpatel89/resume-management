package com.elasticdemo;

import com.elasticdemo.service.FileContentIndexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyWebApplication implements CommandLineRunner {
    @Autowired
    private FileContentIndexer fileContentIndexer;


    public static void main(String[] args) {
        SpringApplication.run(MyWebApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String directoryPath = "/Users/prateek/DockerCompose/";
        String indexName = "files";

       // fileContentIndexer.indexFiles(directoryPath, indexName);
    }

}