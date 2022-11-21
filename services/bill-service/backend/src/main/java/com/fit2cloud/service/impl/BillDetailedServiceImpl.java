package com.fit2cloud.service.impl;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.ValueCountAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.ValueCountAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.ScriptQuery;
import com.aliyun.tea.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.cache.CloudAccountCache;
import com.fit2cloud.common.cache.OrganizationCache;
import com.fit2cloud.common.cache.WorkSpaceCache;
import com.fit2cloud.common.query.convert.QueryFieldValueConvert;
import com.fit2cloud.common.util.EsScriptUtil;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.controller.request.PageBillDetailedRequest;
import com.fit2cloud.controller.response.BillDetailResponse;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.service.IBillDetailedService;
import jodd.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BillDetailedServiceImpl implements IBillDetailedService {
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private IBaseCloudAccountService cloudAccountService;

    @Override
    public IPage<BillDetailResponse> page(Integer currentPage, Integer pageSize, PageBillDetailedRequest request) {
        List<Query> queries = QueryUtil.getQuery(request, QueryFieldValueConvert.builder().field("cloudAccountName").convert((cloudAccountName) -> {
            List<CloudAccount> list = cloudAccountService.list(new LambdaQueryWrapper<CloudAccount>().like(true, CloudAccount::getName, cloudAccountName));
            return CollectionUtils.isEmpty(list) ? null : list.stream().map(CloudAccount::getId).toList();
        }).build());
        ArrayList<Query> boolQueryList = new ArrayList<>(queries);
        if (StringUtil.isNotEmpty(request.getMonth())) {
            Query monthQuery = new Query.Builder().script(new ScriptQuery.Builder().script(s -> EsScriptUtil.getMonthOrYearScript(s, "MONTH", request.getMonth())).build()).build();
            boolQueryList.add(monthQuery);
        }
       if (Objects.isNull(request.getOrder())||StringUtil.isEmpty(request.getOrder().getColumn())){
           request.setOrder(new OrderRequest(){{setAsc(false);setColumn("realTotalCost");}});
       }
        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withSort(request.getOrder().isAsc() ? Sort.by(request.getOrder().getColumn()).ascending() : Sort.by(request.getOrder().getColumn()).descending())
                .withQuery(new Query.Builder().bool(new BoolQuery.Builder().must(boolQueryList).build()).build())
                .withAggregation("total", new Aggregation.Builder().valueCount(new ValueCountAggregation.Builder().field("id.keyword").build()).build())
                .withPageable(Pageable.ofSize(pageSize).withPage((currentPage.equals(0) ? 1 : currentPage) - 1)).build();
        SearchHits<CloudBill> search = elasticsearchTemplate.search(nativeQuery, CloudBill.class);
        Page<BillDetailResponse> page = Page.of(currentPage, pageSize);
        if (search.hasAggregations()) {
            ElasticsearchAggregations aggregations = (ElasticsearchAggregations) search.getAggregations();
            assert aggregations != null;
            ValueCountAggregate valueCountAggregate = aggregations.aggregations().get(0).aggregation().getAggregate().valueCount();
            page.setTotal(Math.round(valueCountAggregate.value()));
        }
        page.setRecords(search.getSearchHits().stream().map(SearchHit::getContent).map(this::mapToBillDetail).toList());
        return page;
    }


    /**
     * 将账单对象转换为账单详情对象
     *
     * @param cloudBill 账单对象
     * @return 账单详情对象
     */
    private BillDetailResponse mapToBillDetail(CloudBill cloudBill) {
        BillDetailResponse billDetailResponse = new BillDetailResponse();
        BeanUtils.copyProperties(cloudBill, billDetailResponse);
        billDetailResponse.setWorkspaceName(StringUtils.isEmpty(cloudBill.getWorkspaceId()) ? null : WorkSpaceCache.getCacheOrUpdate(cloudBill.getWorkspaceId()));
        billDetailResponse.setOrganizationName(StringUtil.isEmpty(cloudBill.getOrganizationId()) ? null : OrganizationCache.getCacheOrUpdate(cloudBill.getOrganizationId()));
        billDetailResponse.setCloudAccountName(StringUtil.isEmpty(cloudBill.getCloudAccountId()) ? null : CloudAccountCache.getCacheOrUpdate(cloudBill.getCloudAccountId()));
        return billDetailResponse;
    }
}
