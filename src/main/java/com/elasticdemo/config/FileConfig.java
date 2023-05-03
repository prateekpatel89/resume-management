package com.elasticdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfig {

    @Value("${file.upload.dir}")
    private  String fileUploadDir;

    public String getFileUploadDir() {
        return fileUploadDir;
    }

    public void setFileUploadDir(String fileUploadDir) {
        this.fileUploadDir = fileUploadDir;
    }
}
