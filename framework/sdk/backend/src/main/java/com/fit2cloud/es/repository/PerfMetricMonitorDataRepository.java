package com.fit2cloud.es.repository;

import com.fit2cloud.es.entity.PerfMetricMonitorData;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author jianneng
 * @date 2022/10/30 11:24
 **/
@Lazy
public interface PerfMetricMonitorDataRepository extends ElasticsearchRepository<PerfMetricMonitorData, String> {
}
