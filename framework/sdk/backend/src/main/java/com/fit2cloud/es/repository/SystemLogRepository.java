package com.fit2cloud.es.repository;

import com.fit2cloud.es.entity.SystemLog;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author:张少虎
 * @Date: 2022/9/28  9:06 AM
 * @Version 1.0
 * @注释:
 */
@Lazy
public interface SystemLogRepository extends ElasticsearchRepository<SystemLog, String> {
}
