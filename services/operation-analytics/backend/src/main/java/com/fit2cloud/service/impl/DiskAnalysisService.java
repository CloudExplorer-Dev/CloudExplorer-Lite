package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseWorkspaceService;
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
    private AnalyticsDiskMapper analyticsDiskMapper;
    @Resource
    private IBaseCloudAccountService iBaseCloudAccountService;
    @Resource
    private IBaseWorkspaceService iBaseWorkspaceService;
    @Resource
    private IBaseOrganizationService iBaseOrganizationService;

    @Override
    public IPage<AnalyticsDiskDTO> pageDisk(PageDiskRequest request) {
        Page<AnalyticsDiskDTO> page = PageUtil.of(request, AnalyticsDiskDTO.class, new OrderItem(ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getCreateTime, true), false), true);
        // 构建查询参数
        QueryWrapper<AnalyticsDiskDTO> wrapper = addServerQuery(request);
        IPage<AnalyticsDiskDTO> result = analyticsDiskMapper.pageList(page, wrapper);
        return result;
    }

    private QueryWrapper<AnalyticsDiskDTO> addServerQuery(PageDiskRequest request) {
        QueryWrapper<AnalyticsDiskDTO> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(request.getName()), ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getDiskName,true), request.getName());
        wrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getAccountId, true), request.getAccountIds());
        wrapper.notIn(true, ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getStatus,true), Arrays.asList("deleted"));
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
        QueryWrapper<AnalyticsDiskDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()),ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getAccountId,true),request.getAccountIds());
        //只统计已挂在与可用的
        queryWrapper.in(true,ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getStatus,true), Arrays.asList("available","in_use"));
        List<AnalyticsDiskDTO> vmList = analyticsDiskMapper.list(queryWrapper);
        Map<String,String> statusMap = new HashMap<>();
        statusMap.put("available","空闲");
        statusMap.put("in_use","已挂载");
        if(request.isStatisticalBlock()){
            Map<String,Long> byAccountMap = vmList.stream().filter(v->StringUtils.isNotEmpty(v.getAccountId())).collect(Collectors.groupingBy(AnalyticsDiskDTO::getAccountId, Collectors.counting()));
            result.put("byAccount",byAccountMap.entrySet().stream().map(c -> new KeyValue(StringUtils.isEmpty(accountMap.get(c.getKey()).getName())?c.getKey():accountMap.get(c.getKey()).getName(), c.getValue()) {}).collect(Collectors.toList()));
            Map<String,Long> byStatusMap = vmList.stream().collect(Collectors.groupingBy(AnalyticsDiskDTO::getStatus, Collectors.counting()));
            result.put("byStatus",byStatusMap.entrySet().stream().map(c -> new KeyValue(statusMap.get(c.getKey()), c.getValue()) {}).collect(Collectors.toList()));
            Map<String,Long> byTypeMap = vmList.stream().collect(Collectors.groupingBy(AnalyticsDiskDTO::getDiskType, Collectors.counting()));
            result.put("byType",byTypeMap.entrySet().stream().map(c -> new KeyValue(DiskTypeConstants.getName(c.getKey()), c.getValue()) {}).collect(Collectors.toList()));
        }else{
            Map<String,LongSummaryStatistics> byAccountMap = vmList.stream().filter(v->StringUtils.isNotEmpty(v.getAccountId())).collect(Collectors.groupingBy(AnalyticsDiskDTO::getAccountId, Collectors.summarizingLong(AnalyticsDiskDTO::getSize)));
            result.put("byAccount",byAccountMap.entrySet().stream().map(c -> new KeyValue(StringUtils.isEmpty(accountMap.get(c.getKey()).getName())?c.getKey():accountMap.get(c.getKey()).getName(), c.getValue().getSum()) {}).collect(Collectors.toList()));
            Map<String,LongSummaryStatistics> byStatusMap = vmList.stream().collect(Collectors.groupingBy(AnalyticsDiskDTO::getStatus, Collectors.summarizingLong(AnalyticsDiskDTO::getSize)));
            result.put("byStatus",byStatusMap.entrySet().stream().map(c -> new KeyValue(statusMap.get(c.getKey()), c.getValue().getSum()) {}).collect(Collectors.toList()));
            Map<String,LongSummaryStatistics> byTypeMap = vmList.stream().collect(Collectors.groupingBy(AnalyticsDiskDTO::getDiskType, Collectors.summarizingLong(AnalyticsDiskDTO::getSize)));
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

    public Map<String,List<BarTreeChartData>> analyticsVmCloudServerByOrgWorkspace(ResourceAnalysisRequest request){
        Map<String,List<BarTreeChartData>> result = new HashMap<>();
        QueryWrapper<AnalyticsDiskDTO> wrapper = new QueryWrapper<>();
        wrapper.eq("org_workspace.type","workspace");
        wrapper.ne(ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getStatus,true),"Deleted");
        wrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getAccountId, true), request.getAccountIds());
        wrapper.groupBy(true,Arrays.asList("org_workspace.id","org_workspace.name"));
        //所有工作空间
        List<BarTreeChartData> workspaceList = iBaseWorkspaceService.list().stream().map(v->{
            BarTreeChartData barTreeChartData = new BarTreeChartData();
            barTreeChartData.setId(v.getId());
            barTreeChartData.setName(v.getName());
            barTreeChartData.setValue(0L);
            barTreeChartData.setPId(v.getOrganizationId());
            return barTreeChartData;
        }).collect(Collectors.toList());
        if(request.isStatisticalBlock()){
            List<BarTreeChartData> list = analyticsDiskMapper.analyticsVmCloudDiskByOrgWorkspace(wrapper);
            setAnalyticsData(workspaceList, list);
        }else{
            List<BarTreeChartData> list = analyticsDiskMapper.analyticsVmCloudDiskByOrgWorkspaceFromSize(wrapper);
            setAnalyticsData(workspaceList, list);
        }
        if(request.isAnalysisWorkspace()){
            workspaceList.sort((o1,o2)->o2.getValue().compareTo(o1.getValue()));
            result.put("all",workspaceList);
            result.put("tree",workspaceList);
            return result;
        }
        Map<String,List<BarTreeChartData>> workspaceMap = workspaceList.stream().collect(Collectors.groupingBy(BarTreeChartData::getPId));
        QueryWrapper<AnalyticsDiskDTO> orgWrapper =new QueryWrapper<>();
        orgWrapper.eq("org_workspace.type","org");
        orgWrapper.ne(ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getStatus,true),"Deleted");
        orgWrapper.in(CollectionUtils.isNotEmpty(request.getAccountIds()), ColumnNameUtil.getColumnName(AnalyticsDiskDTO::getAccountId, true), request.getAccountIds());
        orgWrapper.groupBy(true,Arrays.asList("org_workspace.id","org_workspace.name"));
        //所有组织
        List<BarTreeChartData> orgList = iBaseOrganizationService.list().stream().map(v->{
            BarTreeChartData barTreeChartData = new BarTreeChartData();
            barTreeChartData.setId(v.getId());
            barTreeChartData.setName(v.getName());
            barTreeChartData.setValue(0L);
            barTreeChartData.setPId(v.getPid());
            return barTreeChartData;
        }).collect(Collectors.toList());
        if(request.isStatisticalBlock()){
            List<BarTreeChartData> list = analyticsDiskMapper.analyticsVmCloudDiskByOrgWorkspace(orgWrapper);
            setAnalyticsData(orgList, list);
        }else{
            List<BarTreeChartData> list = analyticsDiskMapper.analyticsVmCloudDiskByOrgWorkspaceFromSize(orgWrapper);
            setAnalyticsData(orgList, list);
        }
        result.put("all",orgList);
        result.put("tree",new ArrayList<>());
        if(CollectionUtils.isNotEmpty(orgList)){
            orgList.stream().forEach(v->{
                if(workspaceMap.get(v.getId())!=null){
                    // 设置工作空间
                    List<BarTreeChartData> wk = workspaceMap.get(v.getId()).stream().peek(workspace->workspace.setName(workspace.getName()+"(工作空间)")).collect(Collectors.toList());
                    Long wkVmCount = wk.stream().mapToLong(BarTreeChartData::getValue).sum();
                    v.setValue(v.getValue()+wkVmCount);
                    v.setChildren(wk);
                }else{
                    v.setChildren(new ArrayList<>());
                }
            });
            orgList.sort((o1,o2)->o2.getValue().compareTo(o1.getValue()));
            List<BarTreeChartData> chartDataList = new ArrayList<>();
            orgList.stream().filter(o->StringUtils.isEmpty(o.getPId())).forEach(v->{
                v.setGroupName("org");
                v.setName(v.getName()+"(组织)");
                v.getChildren().addAll(getChildren(v,orgList,workspaceMap));
                List<BarTreeChartData> childrenList = v.getChildren();
                childrenList.sort((o1,o2)->o2.getValue().compareTo(o1.getValue()));
                v.setChildren(childrenList);
                chartDataList.add(v);
            });
            chartDataList.sort((o1,o2)->o2.getValue().compareTo(o1.getValue()));
            result.put("tree",chartDataList);
        }
        return result;
    }

    private static void setAnalyticsData(List<BarTreeChartData> orgList, List<BarTreeChartData> list) {
        orgList.forEach(v->{
            List<BarTreeChartData> tmp = list.stream().filter(c->StringUtils.equalsIgnoreCase(v.getId(),c.getId())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(tmp)){
                v.setValue(tmp.get(0).getValue());
            }
        });
    }

    /**
     * 递归获取子级
     * @param barTreeChartData
     * @param list
     * @return
     */
    public List<BarTreeChartData> getChildren(BarTreeChartData barTreeChartData,List<BarTreeChartData> list,Map<String,List<BarTreeChartData>> workspaceMap) {
        List<BarTreeChartData> childrenList = list.stream().filter(u -> Objects.equals(u.getPId(), barTreeChartData.getId())).map(
                u -> {
                    u.setName(u.getName()+"(子组织)");
                    u.setGroupName("org");
                    u.setChildren(getChildren(u, list,workspaceMap));
                    if(workspaceMap.get(u.getId())!=null){
                        // 设置工作空间
                        List<BarTreeChartData> wk = workspaceMap.get(u.getId());
                        Long wkVmCount = wk.stream().mapToLong(BarTreeChartData::getValue).sum();
                        u.setValue(u.getValue()+wkVmCount);
                        u.getChildren().addAll(wk);
                    }
                    barTreeChartData.setValue(barTreeChartData.getValue()+u.getValue());
                    return u;
                }
        ).collect(Collectors.toList());
        childrenList.sort((o1,o2)->o2.getValue().compareTo(o1.getValue()));
        return childrenList;
    }
}
