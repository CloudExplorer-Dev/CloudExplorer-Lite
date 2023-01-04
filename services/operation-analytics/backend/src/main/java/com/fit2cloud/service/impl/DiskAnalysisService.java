package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.constants.DiskTypeConstants;
import com.fit2cloud.controller.request.disk.PageDiskRequest;
import com.fit2cloud.controller.request.disk.ResourceAnalysisRequest;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.dao.mapper.VmCloudDiskMapper;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.service.IDiskAnalysisService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author jianneng
 * @date 2022/12/24 11:29
 **/
@Service
public class DiskAnalysisService implements IDiskAnalysisService {

    @Resource
    private VmCloudDiskMapper vmCloudDiskMapper;
    @Resource
    private IBaseCloudAccountService iBaseCloudAccountService;

    @Override
    public IPage<VmCloudDiskDTO> pageDisk(PageDiskRequest request) {
        Page<VmCloudDiskDTO> page = PageUtil.of(request, VmCloudDiskDTO.class, new OrderItem(ColumnNameUtil.getColumnName(VmCloudDiskDTO::getCreateTime, true), false), true);
        // 构建查询参数
        QueryWrapper<VmCloudDiskDTO> wrapper = addServerQuery(request);
        IPage<VmCloudDiskDTO> result = vmCloudDiskMapper.pageList(page, wrapper);
        return result;
    }

    private QueryWrapper<VmCloudDiskDTO> addServerQuery(PageDiskRequest request) {
        QueryWrapper<VmCloudDiskDTO> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(request.getName()), ColumnNameUtil.getColumnName(VmCloudDiskDTO::getDiskName,true), request.getName());
        return wrapper;
    }

    @Override
    public List<CloudAccount> getAllCloudAccount() {
        QueryWrapper<CloudAccount> queryWrapper = new QueryWrapper<>();
        List<CloudAccount> accountList = iBaseCloudAccountService.list(queryWrapper);
        return accountList;
    }

    @Override
    public Map<String, List<KeyValue>> spread(ResourceAnalysisRequest request) {
        Map<String,List<KeyValue>> result = new HashMap<>();
        List<CloudAccount> accountList = getAllCloudAccount();
        Map<String,CloudAccount> accountMap = accountList.stream().collect(Collectors.toMap(CloudAccount::getId,v->v,(k1,k2)->k1));
        if(accountMap.size()==0){
            return result;
        }
        QueryWrapper<VmCloudDiskDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()),ColumnNameUtil.getColumnName(VmCloudDiskDTO::getAccountId,true),request.getAccountIds());
        //只统计已挂在与可用的
        queryWrapper.in(true,ColumnNameUtil.getColumnName(VmCloudDiskDTO::getStatus,true), Arrays.asList("available","in_use"));
        List<VmCloudDiskDTO> vmList = vmCloudDiskMapper.list(queryWrapper);
        Map<String,String> statusMap = new HashMap<>();
        statusMap.put("available","空闲");
        statusMap.put("in_use","已挂载");
        if(request.isStatisticalBlock()){
            Map<String,Long> byAccountMap = vmList.stream().filter(v->StringUtils.isNotEmpty(v.getAccountId())).collect(Collectors.groupingBy(VmCloudDiskDTO::getAccountId, Collectors.counting()));
            result.put("byAccount",byAccountMap.entrySet().stream().map(c -> new KeyValue(StringUtils.isEmpty(accountMap.get(c.getKey()).getName())?c.getKey():accountMap.get(c.getKey()).getName(), c.getValue()) {}).collect(Collectors.toList()));
            Map<String,Long> byStatusMap = vmList.stream().collect(Collectors.groupingBy(VmCloudDiskDTO::getStatus, Collectors.counting()));
            result.put("byStatus",byStatusMap.entrySet().stream().map(c -> new KeyValue(statusMap.get(c.getKey()), c.getValue()) {}).collect(Collectors.toList()));
            Map<String,Long> byTypeMap = vmList.stream().collect(Collectors.groupingBy(VmCloudDiskDTO::getDiskType, Collectors.counting()));
            result.put("byType",byTypeMap.entrySet().stream().map(c -> new KeyValue(DiskTypeConstants.getName(c.getKey()), c.getValue()) {}).collect(Collectors.toList()));
        }else{
            Map<String,LongSummaryStatistics> byAccountMap = vmList.stream().filter(v->StringUtils.isNotEmpty(v.getAccountId())).collect(Collectors.groupingBy(VmCloudDiskDTO::getAccountId, Collectors.summarizingLong(VmCloudDiskDTO::getSize)));
            result.put("byAccount",byAccountMap.entrySet().stream().map(c -> new KeyValue(StringUtils.isEmpty(accountMap.get(c.getKey()).getName())?c.getKey():accountMap.get(c.getKey()).getName(), c.getValue().getSum()) {}).collect(Collectors.toList()));
            Map<String,LongSummaryStatistics> byStatusMap = vmList.stream().collect(Collectors.groupingBy(VmCloudDiskDTO::getStatus, Collectors.summarizingLong(VmCloudDiskDTO::getSize)));
            result.put("byStatus",byStatusMap.entrySet().stream().map(c -> new KeyValue(statusMap.get(c.getKey()), c.getValue().getSum()) {}).collect(Collectors.toList()));
            Map<String,LongSummaryStatistics> byTypeMap = vmList.stream().collect(Collectors.groupingBy(VmCloudDiskDTO::getDiskType, Collectors.summarizingLong(VmCloudDiskDTO::getSize)));
            result.put("byType",byTypeMap.entrySet().stream().map(c -> new KeyValue(DiskTypeConstants.getName(c.getKey()), c.getValue().getSum()) {}).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public List<ChartData> diskIncreaseTrend(ResourceAnalysisRequest request) {
        List<ChartData> tempChartDataList = new ArrayList<>();
        QueryWrapper<VmCloudDiskDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()),ColumnNameUtil.getColumnName(VmCloudDiskDTO::getAccountId,true),request.getAccountIds());
        queryWrapper.ge(true,ColumnNameUtil.getColumnName(VmCloudDiskDTO::getCreateTime,true), LocalDateTime.now().minusMonths(request.getMonthNumber()));
        List<VmCloudDiskDTO> vmList = vmCloudDiskMapper.list(queryWrapper);
        if(CollectionUtils.isNotEmpty(vmList)){
            //块
            if(request.isStatisticalBlock()){
                Map<String,Long> hostSpread = vmList.stream().collect(Collectors.groupingBy(VmCloudDiskDTO::getCreateMonth, Collectors.counting()));
                hostSpread.keySet().forEach(k->{
                    ChartData chartData = new ChartData();
                    chartData.setXAxis(k);
                    chartData.setYAxis(new BigDecimal(hostSpread.get(k)));
                    tempChartDataList.add(chartData);
                });
            }else{
                //容量
                Map<String,LongSummaryStatistics> statisticsSizeMap = vmList.stream().collect(Collectors.groupingBy(VmCloudDiskDTO::getCreateMonth,Collectors.summarizingLong(VmCloudDiskDTO::getSize)));
                statisticsSizeMap.keySet().forEach(k->{
                    ChartData chartData = new ChartData();
                    chartData.setXAxis(k);
                    chartData.setYAxis(new BigDecimal(statisticsSizeMap.get(k).getSum()));
                    tempChartDataList.add(chartData);
                });
            }
        }
        return tempChartDataList;
    }
}
