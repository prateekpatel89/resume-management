package com.elasticdemo.repository;

import com.elasticdemo.model.FileModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface FileRepository extends ElasticsearchRepository<FileModel, String> {

    List<FileModel> findByNameContaining(String name);

}

