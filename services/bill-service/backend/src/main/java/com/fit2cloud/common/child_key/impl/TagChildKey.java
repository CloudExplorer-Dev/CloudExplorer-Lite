package com.fit2cloud.common.child_key.impl;

import com.fit2cloud.common.child_key.ChildKey;
import com.fit2cloud.common.util.EsFieldUtil;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.es.entity.CloudBill;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;

import java.util.List;
import java.util.Map;

public class TagChildKey implements ChildKey {
    @Override
    public List<DefaultKeyValue<String, String>> childKeys() {
        ElasticsearchTemplate elasticsearchTemplate = SpringUtil.getBean(ElasticsearchTemplate.class);
        Map<String, Object> mapping = elasticsearchTemplate.indexOps(CloudBill.class).getMapping();
        return EsFieldUtil.getChildEsField(mapping, "tags");
    }
}
