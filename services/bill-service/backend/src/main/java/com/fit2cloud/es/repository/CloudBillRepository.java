package com.fit2cloud.es.repository;

import com.fit2cloud.es.entity.CloudBill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/14  5:32 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface CloudBillRepository extends ElasticsearchRepository<CloudBill, String> {
}
