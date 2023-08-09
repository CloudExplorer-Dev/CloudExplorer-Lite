package com.fit2cloud.db_convert;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.*;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetMappingRequest;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.elasticsearch.indices.get_mapping.IndexMappingRecord;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.db_convert.DBConvert;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.common.util.MonthUtil;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.service.SyncService;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/7  16:00}
 * {@code @Version 1.0}
 * {@code @注释: }
 */

public class CloudBillConvert implements DBConvert {
    @Override
    public void execute() {
        ElasticsearchClient elasticsearchClient = SpringUtil.getBean(ElasticsearchClient.class);
        try {
            String indexName = CloudBill.class.getAnnotation(Document.class).indexName();
            Map<String, IndexMappingRecord> result = elasticsearchClient.indices().getMapping(new GetMappingRequest.Builder().index(indexName).build())
                    .result();
            // 如果索引存在 cost字段, 则不需要删除索引
            if (result.get(indexName).mappings().properties().containsKey("cost")) {
                return;
            }
            // 删除索引
            elasticsearchClient.indices().delete(new DeleteIndexRequest.Builder()
                    .index(indexName)
                    .build());
            // 构建新索引字段
            Map<String, Property> stringPropertyMap = toMappingProperty(CloudBill.class);
            CreateIndexRequest createIndexRequest = new CreateIndexRequest
                    .Builder()
                    .index(indexName)
                    .settings(new IndexSettings.Builder()
                            .maxTermsCount(Integer.MAX_VALUE)
                            .maxResultWindow(Integer.MAX_VALUE)
                            .numberOfShards("10")
                            .numberOfReplicas("1").build())
                    .mappings(new TypeMapping.Builder()
                            .properties(stringPropertyMap)
                            .dateDetection(false)
                            .build()).build();
            // 创建索引
            elasticsearchClient.indices().create(createIndexRequest);
            // 创建完成后,直接发送同步任务,同步12个月账单
            SyncService syncService = SpringUtil.getBean(SyncService.class);
            // 发送同步任务
            IBaseCloudAccountService cloudAccountService = SpringUtil.getBean(IBaseCloudAccountService.class);
            for (CloudAccount cloudAccount : cloudAccountService.list()) {
                AsyncJob.run(() -> syncService.syncBill(cloudAccount.getId(), MonthUtil.getHistoryMonth(12)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 将一个Class 转换为 Mapping
     *
     * @param clazz 需要装换的对象
     * @return es mapping
     */
    private Map<String, Property> toMappingProperty(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields).filter(field -> field.isAnnotationPresent(org.springframework.data.elasticsearch.annotations.Field.class) ||
                        field.isAnnotationPresent(MultiField.class))
                .map(this::toMappingProperty)
                .collect(Collectors.toMap(KeyValue::getKey, KeyValue::getValue));

    }

    /**
     * 将一个字段 转换为一个Property
     *
     * @param field 字段
     * @return Property
     */
    private KeyValue<String, Property> toMappingProperty(Field field) {
        Property.Builder builder = new Property.Builder();
        if (field.isAnnotationPresent(org.springframework.data.elasticsearch.annotations.Field.class)) {
            org.springframework.data.elasticsearch.annotations.Field annotation = field.getAnnotation(org.springframework.data.elasticsearch.annotations.Field.class);
            appendField(annotation, builder);
        }
        if (field.isAnnotationPresent(MultiField.class)) {
            MultiField annotation = field.getAnnotation(MultiField.class);
            appendField(annotation.mainField(), annotation.otherFields(), builder);
        }
        return new DefaultKeyValue<>(field.getName(), builder.build());
    }

    /**
     * 添加一个字段
     *
     * @param annotation 注解
     * @param builder    Property构造
     */
    private void appendField(org.springframework.data.elasticsearch.annotations.Field annotation, Property.Builder builder) {
        FieldType type = annotation.type();
        switch (type) {
            case Text -> builder.text(new TextProperty.Builder().build());
            case Keyword -> builder.keyword(new KeywordProperty.Builder().build());
            case Date -> builder.date(new DateProperty.Builder().build());
            case Object -> builder.object(new ObjectProperty.Builder().build());
        }
    }

    /**
     * 添加一个字段
     *
     * @param annotation  注解
     * @param innerFields 子字段
     * @param builder     构造
     */
    private void appendField(org.springframework.data.elasticsearch.annotations.Field annotation, InnerField[] innerFields, Property.Builder builder) {
        FieldType type = annotation.type();
        Map<String, Property> innerField = mapInnerField(innerFields);
        switch (type) {
            case Text -> builder.text(new TextProperty.Builder().fields(innerField).build());
            case Keyword ->
                    builder.keyword(new KeywordProperty.Builder().fields(innerField).ignoreAbove(annotation.ignoreAbove()).build());
            case Date ->
                    builder.date(new DateProperty.Builder().fields(innerField).format(annotation.format().length > 0 ? annotation.format()[0].getPattern() : DateFormat.date_hour_minute_second.getPattern()).build());
            case Object -> builder.object(new ObjectProperty.Builder().fields(innerField).build());
        }
    }

    private Map<String, Property> mapInnerField(InnerField[] innerFields) {
        return Arrays.stream(innerFields).map(this::mapInnerField).collect(Collectors.toMap(KeyValue::getKey, KeyValue::getValue));
    }

    private KeyValue<String, Property> mapInnerField(InnerField innerField) {
        FieldType type = innerField.type();
        Property property;
        switch (type) {
            case Text -> property = new TextProperty.Builder().build()._toProperty();
            case Date -> property = new DateProperty.Builder().build()._toProperty();
            case Object -> property = new ObjectProperty.Builder().build()._toProperty();
            default -> property = new KeywordProperty.Builder().build()._toProperty();
        }
        return new DefaultKeyValue<>(innerField.suffix(), property);

    }
}
