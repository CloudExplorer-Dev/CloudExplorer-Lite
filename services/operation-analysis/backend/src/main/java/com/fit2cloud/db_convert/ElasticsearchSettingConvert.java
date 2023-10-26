package com.fit2cloud.db_convert;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cluster.PutClusterSettingsRequest;
import co.elastic.clients.json.JsonData;
import com.fit2cloud.common.db_convert.DBConvert;
import com.fit2cloud.common.utils.SpringUtil;
import lombok.SneakyThrows;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/10/26  10:35}
 * {@code @Version 1.0}
 * {@code @注释:  设置elasticsearch 最大桶聚合数量}
 */
public class ElasticsearchSettingConvert implements DBConvert {
    @SneakyThrows
    @Override
    public void execute() {
        ElasticsearchClient elasticsearchClient = SpringUtil.getBean(ElasticsearchClient.class);
        elasticsearchClient
                .cluster()
                .putSettings(new PutClusterSettingsRequest.Builder().persistent("search.max_buckets", JsonData.of(50000)).build());

    }
}
