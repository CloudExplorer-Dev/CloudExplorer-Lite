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
import com.fit2cloud.controller.response.BarTreeChartData;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.dao.mapper.AnalyticsDiskMapper;
import com.fit2cloud.dto.AnalyticsDiskDTO;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.service.IDiskAnalysisService;
import com.fit2cloud.service.IServerAnalysisService;
import com.fit2cloud.utils.OperationUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.list.TreeList;
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
public class DiskAnalysisServiceImpl implements IDiskAnalysisService {

    @Resource
    private AnalyticsDiskMapper analyticsDiskMapper;
    @Resource
    private IBaseCloudAccountService iBaseCloudAccountService;
    @Resource
    private IServerAnalysisService iServerAnalysisService;

    @Override
    public IPage<AnalyticsDiskDTO> pageDisk(PageDiskRequest request) {
        Page<AnalyticsDiskDTO> page = PageUtil.of(request, AnalyticsDiskDTO.class, new OrderItem(ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getCreateTime, true), false), true);
        // 构建查询参数
        QueryWrapper<AnalyticsDiskDTO> wrapper = addServerQuery(request);
        return analyticsDiskMapper.pageList(page, wrapper);
    }

    private QueryWrapper<AnalyticsDiskDTO> addServerQuery(PageDiskRequest request) {
        QueryWrapper<AnalyticsDiskDTO> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(request.getName()), ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getDiskName,true), request.getName());
        wrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getAccountId, true), request.getAccountIds());
        wrapper.notIn(true, ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getStatus,true), List.of("deleted"));
        return wrapper;
    }

    @Override
    public List<CloudAccount> getAllCloudAccount() {
        QueryWrapper<CloudAccount> queryWrapper = new QueryWrapper<>();
        return iBaseCloudAccountService.list(queryWrapper);
    }

    @Override
    public Map<String, List<KeyValue>> spread(ResourceAnalysisRequest request) {
        Map<String,List<KeyValue>> result = new HashMap<>();
        Map<String,CloudAccount> accountMap = iServerAnalysisService.getAllAccountIdMap();
        if(accountMap.size()==0){
            return result;
        }
        QueryWrapper<AnalyticsDiskDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()),ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getAccountId,true),request.getAccountIds());
        //只统计已挂在与可用的
        queryWrapper.in(true,ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getStatus,true), Arrays.asList("available","in_use"));
        List<AnalyticsDiskDTO> diskList = analyticsDiskMapper.list(queryWrapper);
        Map<String,String> statusMap = new HashMap<>();
        statusMap.put("available","空闲");
        statusMap.put("in_use","已挂载");
        if(request.isStatisticalBlock()){
            Map<String,Long> byAccountMap = diskList.stream().filter(v->StringUtils.isNotEmpty(v.getAccountId())).collect(Collectors.groupingBy(AnalyticsDiskDTO::getAccountId, Collectors.counting()));
            result.put("byAccount",byAccountMap.entrySet().stream().map(c -> new KeyValue(StringUtils.isEmpty(accountMap.get(c.getKey()).getName())?c.getKey():accountMap.get(c.getKey()).getName(), c.getValue()) {}).collect(Collectors.toList()));
            Map<String,Long> byStatusMap = diskList.stream().collect(Collectors.groupingBy(AnalyticsDiskDTO::getStatus, Collectors.counting()));
            result.put("byStatus",byStatusMap.entrySet().stream().map(c -> new KeyValue(statusMap.get(c.getKey()), c.getValue()) {}).collect(Collectors.toList()));
            Map<String,Long> byTypeMap = diskList.stream().collect(Collectors.groupingBy(AnalyticsDiskDTO::getDiskType, Collectors.counting()));
            result.put("byType",byTypeMap.entrySet().stream().map(c -> new KeyValue(DiskTypeConstants.getName(c.getKey()), c.getValue()) {}).collect(Collectors.toList()));
        }else{
            Map<String,LongSummaryStatistics> byAccountMap = diskList.stream().filter(v->StringUtils.isNotEmpty(v.getAccountId())).collect(Collectors.groupingBy(AnalyticsDiskDTO::getAccountId, Collectors.summarizingLong(AnalyticsDiskDTO::getSize)));
            result.put("byAccount",byAccountMap.entrySet().stream().map(c -> new KeyValue(StringUtils.isEmpty(accountMap.get(c.getKey()).getName())?c.getKey():accountMap.get(c.getKey()).getName(), c.getValue().getSum()) {}).collect(Collectors.toList()));
            Map<String,LongSummaryStatistics> byStatusMap = diskList.stream().collect(Collectors.groupingBy(AnalyticsDiskDTO::getStatus, Collectors.summarizingLong(AnalyticsDiskDTO::getSize)));
            result.put("byStatus",byStatusMap.entrySet().stream().map(c -> new KeyValue(statusMap.get(c.getKey()), c.getValue().getSum()) {}).collect(Collectors.toList()));
            Map<String,LongSummaryStatistics> byTypeMap = diskList.stream().collect(Collectors.groupingBy(AnalyticsDiskDTO::getDiskType, Collectors.summarizingLong(AnalyticsDiskDTO::getSize)));
            result.put("byType",byTypeMap.entrySet().stream().map(c -> new KeyValue(DiskTypeConstants.getName(c.getKey()), c.getValue().getSum()) {}).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public List<ChartData> diskIncreaseTrend(ResourceAnalysisRequest request) {
        List<ChartData> tempChartDataList = new ArrayList<>();
        List<CloudAccount> accountList = getAllCloudAccount();
        Map<String,CloudAccount> accountMap = accountList.stream().collect(Collectors.toMap(CloudAccount::getId,v->v,(k1,k2)->k1));
        if(accountMap.size()==0){
            return tempChartDataList;
        }
        QueryWrapper<AnalyticsDiskDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()),ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getAccountId,true),request.getAccountIds());
        queryWrapper.ge(true,ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getCreateTime,true), LocalDateTime.now().minusDays(request.getDayNumber()));
        List<AnalyticsDiskDTO> vmList = analyticsDiskMapper.list(queryWrapper);
        if(CollectionUtils.isNotEmpty(vmList)){
            Map<String,List<AnalyticsDiskDTO>> accountGroup = vmList.stream().filter(v->StringUtils.isNotEmpty(v.getAccountId())).collect(Collectors.groupingBy(AnalyticsDiskDTO::getAccountId));
            //块
            if(request.isStatisticalBlock()){
                accountGroup.keySet().forEach(accountId->{
                    Map<String,Long> hostSpread = accountGroup.get(accountId).stream().collect(Collectors.groupingBy(AnalyticsDiskDTO::getCreateMonth, Collectors.counting()));
                    hostSpread.keySet().forEach(k->{
                        ChartData chartData = new ChartData();
                        chartData.setXAxis(k);
                        chartData.setGroupName(accountGroup.get(accountId).get(0).getAccountName());
                        chartData.setYAxis(new BigDecimal(hostSpread.get(k)));
                        tempChartDataList.add(chartData);
                    });
                });
            }else{
                accountGroup.keySet().forEach(accountId->{
                    //容量
                    Map<String,LongSummaryStatistics> statisticsSizeMap = vmList.stream().collect(Collectors.groupingBy(AnalyticsDiskDTO::getCreateMonth,Collectors.summarizingLong(AnalyticsDiskDTO::getSize)));
                    statisticsSizeMap.keySet().forEach(k->{
                        ChartData chartData = new ChartData();
                        chartData.setXAxis(k);
                        chartData.setGroupName(accountGroup.get(accountId).get(0).getAccountName());
                        chartData.setYAxis(new BigDecimal(statisticsSizeMap.get(k).getSum()));
                        tempChartDataList.add(chartData);
                    });
                });
            }
        }
        return tempChartDataList;
    }

    public Map<String,List<BarTreeChartData>> analyticsVmCloudDiskByOrgWorkspace(ResourceAnalysisRequest request){
        Map<String,List<BarTreeChartData>> result = new HashMap<>();
        List<BarTreeChartData> workspaceList =  workspaceSpread(request);
        if(request.isAnalysisWorkspace()){
            result.put("tree",workspaceList);
            return result;
        }else{
            orgSpread(request,workspaceList,result);
        }
        return result;
    }

    private void orgSpread(ResourceAnalysisRequest request, List<BarTreeChartData> workspaceList, Map<String,List<BarTreeChartData>> result){
        //组织下工作空间添加标识
        workspaceList = workspaceList.stream().peek(v-> v.setName(v.getName()+"(工作空间)")).collect(Collectors.toList());
        //工作空间按组织ID分组
        Map<String,List<BarTreeChartData>> workspaceMap = workspaceList.stream().collect(Collectors.groupingBy(BarTreeChartData::getPId));
        //查询所有组织初始化为chart数据
        List<BarTreeChartData> orgList = iServerAnalysisService.initOrgChartData();
        //查询所有有授权磁盘的组织
        QueryWrapper<AnalyticsDiskDTO> orgWrapper =new QueryWrapper<>();
        orgWrapper.eq("org_workspace.type","org");
        orgWrapper.ne(ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getStatus,true),"deleted");
        orgWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getAccountId, true), request.getAccountIds());
        orgWrapper.groupBy(true,Arrays.asList("org_workspace.id","org_workspace.name"));
        List<BarTreeChartData> list;
        if (request.isStatisticalBlock()){
            list = analyticsDiskMapper.analyticsVmCloudDiskByOrgWorkspace(orgWrapper);
        }else{
            list = analyticsDiskMapper.analyticsVmCloudDiskByOrgWorkspaceFromSize(orgWrapper);
        }
        OperationUtils.initOrgWorkspaceAnalyticsData(orgList, list);
        //初始化子级
        orgList.forEach(OperationUtils::setSelfToChildren);
        orgList.sort((o1,o2)->o2.getValue().compareTo(o1.getValue()));
        //扁平数据
        result.put("all",orgList);
        //树结构
        List<BarTreeChartData> chartDataList = new TreeList<>();
        orgList.stream().filter(o->StringUtils.isEmpty(o.getPId())).forEach(v->{
            v.setGroupName("org");
            v.setName(v.getName()+"(组织)");
            v.getChildren().addAll(iServerAnalysisService.getChildren(v,orgList,workspaceMap));
            chartDataList.add(v);
        });
        chartDataList.sort((o1,o2)->o2.getValue().compareTo(o1.getValue()));
        result.put("tree",chartDataList);

    }

    /**
     * 工作空间上分布
     * @param request 云磁盘分析参数
     * @return List<BarTreeChartData>
     */
    private List<BarTreeChartData> workspaceSpread(ResourceAnalysisRequest request){
        QueryWrapper<AnalyticsDiskDTO> wrapper = new QueryWrapper<>();
        wrapper.eq("org_workspace.type","workspace");
        wrapper.ne(ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getStatus,true),"deleted");
        wrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getAccountId, true), request.getAccountIds());
        wrapper.groupBy(true,Arrays.asList("org_workspace.id","org_workspace.name"));
        List<BarTreeChartData> workspaceList = iServerAnalysisService.initWorkspaceChartData();
        List<BarTreeChartData> list;
        if(request.isStatisticalBlock()){
            list = analyticsDiskMapper.analyticsVmCloudDiskByOrgWorkspace(wrapper);
        }else{
            list = analyticsDiskMapper.analyticsVmCloudDiskByOrgWorkspaceFromSize(wrapper);
        }
        OperationUtils.initOrgWorkspaceAnalyticsData(workspaceList, list);
        workspaceList.sort((o1,o2)->o2.getValue().compareTo(o1.getValue()));
        return workspaceList;
    }


}
