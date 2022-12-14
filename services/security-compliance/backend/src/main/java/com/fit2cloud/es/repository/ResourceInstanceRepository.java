package com.fit2cloud.es.repository;

import com.fit2cloud.es.entity.ResourceInstance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  14:01}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface ResourceInstanceRepository extends ElasticsearchRepository<ResourceInstance, String> {
}
