package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fit2cloud.common.es.ElasticsearchProvide;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.log.entity.OperatedLogVO;
import com.fit2cloud.common.log.entity.SystemLogVO;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.controller.request.es.PageOperatedLogRequest;
import com.fit2cloud.controller.request.es.PageSystemLogRequest;
import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.service.ILogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author jianneng
 * @date 2022/9/23 17:08
 **/
@Service
public class LogServiceImpl implements ILogService {

    private static final String CE_FILE_SYSTEM_LOGS = "ce-file-system-logs";
    private static final String CE_FILE_API_LOGS = "ce-file-api-logs";

    @Resource
    private ElasticsearchProvide provide;

    @Override
    public IPage<SystemLogVO> systemLogs(PageSystemLogRequest request) {
        Page<SystemLogVO> page = new Page<>(request.getCurrentPage(), request.getPageSize(), false);
        SearchRequest.Builder builder = provide.requestBuilder(CE_FILE_SYSTEM_LOGS);
        //分页
        builder.from(request.getCurrentPage()).size(request.getPageSize());
        //条件
        Query.Builder query = addQuery(JsonUtil.toJSONString(request));
        builder.query(query.build());
        //排序
        if(Optional.ofNullable(request.getOrder()).isPresent()){
            SortOptions.Builder sort = addSort(request.getOrder());
            builder.sort(sort.build());
        }
        return provide.searchByQuery(CE_FILE_SYSTEM_LOGS,builder, SystemLogVO.class,page);
    }

    /**
     * TODO 暂时都按照es排序，其他自定义字段无法排序
     * @param orderRequest
     * @return
     */
    private SortOptions.Builder addSort(OrderRequest orderRequest) {
        SortOptions.Builder sort = new SortOptions.Builder();
        String columnName = orderRequest.getColumn();
        String orderValue = orderRequest.isAsc()?"Asc":"Desc";
        SortOrder s = SortOrder.valueOf(orderValue);
        //排序
        sort.field(k->k.field("@timestamp").order(s));
        return sort;
    }

    private Query.Builder addQuery(String paramsJson) {
        Query.Builder query = new Query.Builder();
        //query.matchAll(m->m);
        ObjectNode params = JsonUtil.parseObject(paramsJson);
        Iterator<String> fieldNames = params.fieldNames();
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        //只接收tags是multiline的数据
        //boolQuery.must(m->m.match(k->k.field("tags").query(v->v.stringValue("multiline"))));
        while (fieldNames.hasNext()){
            String name = fieldNames.next();
            if(StringUtils.equalsIgnoreCase("currentPage",name) || StringUtils.equalsIgnoreCase("pageSize",name)){
                continue;
            }
            JsonNode jsonNode = params.get(name);
            String value = jsonNode.asText();
            if(StringUtils.isNotEmpty(value) && !StringUtils.equalsIgnoreCase("null",value)){
                boolQuery.must(m -> m.match(u -> u.field(name).query(v->v.stringValue(value))));
            }
        }
        query.bool(boolQuery.build());
        return  query;
    }

    @Override
    public IPage<OperatedLogVO> operatedLogs(PageOperatedLogRequest request) {
        Page<SystemLogVO> page = new Page<>(request.getCurrentPage(), request.getPageSize(), false);
        SearchRequest.Builder builder = provide.requestBuilder(CE_FILE_API_LOGS);
        //分页
        builder.from(request.getCurrentPage()).size(request.getPageSize());
        if(StringUtils.equalsIgnoreCase("loginLog",request.getType())){
            request.setType(null);
            request.setResourceType(ResourceTypeEnum.SYSTEM.getDescription());
        }
        if(StringUtils.equalsIgnoreCase("vmOperateLog",request.getType())){
            request.setType(null);
            request.setResourceType(ResourceTypeEnum.VIRTUAL_MACHINE.getDescription());
        }
        if(StringUtils.equalsIgnoreCase("diskOperateLog",request.getType())){
            request.setType(null);
            request.setResourceType(ResourceTypeEnum.DISK.getDescription());
        }
        if(StringUtils.equalsIgnoreCase("allLog",request.getType())){
            request.setType(null);
            //request.setResourceType(ResourceTypeEnum.DISK.getDescription());
        }
        //条件
        Query.Builder query = addQuery(JsonUtil.toJSONString(request));
        //排序
        if(Optional.ofNullable(request.getOrder()).isPresent()){
            SortOptions.Builder sort = addSort(request.getOrder());
            builder.sort(sort.build());
        }
        builder.query(query.build());
       return provide.searchByQuery(CE_FILE_API_LOGS,builder, OperatedLogVO.class,page);
    }

}
