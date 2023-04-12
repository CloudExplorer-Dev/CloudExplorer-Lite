package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudDiskMapper;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.constants.DiskTypeConstants;
import com.fit2cloud.constants.SpecialAttributesConstants;
import com.fit2cloud.controller.request.disk.PageDiskRequest;
import com.fit2cloud.controller.request.disk.ResourceAnalysisRequest;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.controller.response.TreeNode;
import com.fit2cloud.dao.entity.OrgWorkspace;
import com.fit2cloud.dao.mapper.OrgWorkspaceMapper;
import com.fit2cloud.dto.AnalysisDiskDTO;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.service.IDiskAnalysisService;
import com.fit2cloud.service.IPermissionService;
import com.fit2cloud.service.IServerAnalysisService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author jianneng
 * @date 2022/12/24 11:29
 **/
@Service
public class DiskAnalysisServiceImpl implements IDiskAnalysisService {

    @Resource
    private BaseVmCloudDiskMapper baseVmCloudDiskMapper;
    @Resource
    private IServerAnalysisService iServerAnalysisService;
    @Resource
    private IPermissionService permissionService;
    @Resource
    private OrgWorkspaceMapper orgWorkspaceMapper;
    @Resource
    private CurrentUserResourceService currentUserResourceService;

    @Resource
    private BaseVmCloudServerMapper baseVmCloudServerMapper;

    /**
     * 分页查询磁盘明细
     * @param request 分页查询磁盘参数
     */
    @Override
    public IPage<AnalysisDiskDTO> pageDisk(PageDiskRequest request) {
        Page<AnalysisDiskDTO> page = PageUtil.of(request, AnalysisDiskDTO.class, null,  true);
        // 构建查询参数
        MPJLambdaWrapper<VmCloudDisk> wrapper = addDiskPageQuery(request);
        IPage<AnalysisDiskDTO> result = baseVmCloudDiskMapper.selectJoinPage(page,AnalysisDiskDTO.class, wrapper);
        // 设置云主机名称
        List<String> vmCloudUuids = result.getRecords().stream().map(AnalysisDiskDTO::getInstanceUuid).toList();
        if(CollectionUtils.isNotEmpty(vmCloudUuids)){
            MPJLambdaWrapper<VmCloudServer> vmCloudServerMPJLambdaWrapper =  new MPJLambdaWrapper<>();
            vmCloudServerMPJLambdaWrapper.in(true,VmCloudServer::getInstanceUuid,vmCloudUuids);
            List<VmCloudServer> list = baseVmCloudServerMapper.selectList(vmCloudServerMPJLambdaWrapper);
            if(CollectionUtils.isNotEmpty(list)){
                result.getRecords().forEach(disk->{
                    List<VmCloudServer> has = list.stream().filter(v->StringUtils.equalsIgnoreCase(v.getAccountId(),disk.getAccountId())&& StringUtils.equalsIgnoreCase(v.getInstanceUuid(),disk.getInstanceUuid())).toList();
                    if(CollectionUtils.isNotEmpty(has)){
                        disk.setVmInstanceName(has.get(0).getInstanceName());
                    }
                });
            }
        }
        return result;
    }

    /**
     * 分页查询参数
     */
    private MPJLambdaWrapper<VmCloudDisk> addDiskPageQuery(PageDiskRequest request) {
        List<String> sourceIds = permissionService.getSourceIds();
        if (!CurrentUserUtils.isAdmin() && CollectionUtils.isNotEmpty(sourceIds)) {
            request.setSourceIds(sourceIds);
        }
        if(CollectionUtils.isEmpty(request.getAccountIds())){
            request.setAccountIds(currentUserResourceService.currentUserCloudAccountList().stream().map(CloudAccount::getId).toList());
        }
        MPJLambdaWrapper<VmCloudDisk> wrapper = defaultDiskQuery(new MPJLambdaWrapper<>(),request.getAccountIds());
        wrapper.orderByDesc(VmCloudDisk::getCreateTime);
        wrapper.selectAs(CloudAccount::getName, AnalysisDiskDTO::getAccountName);
        wrapper.selectAs(CloudAccount::getPlatform,AnalysisDiskDTO::getPlatform);
        wrapper.like(StringUtils.isNotBlank(request.getName()), VmCloudDisk::getDiskName, request.getName());
        wrapper.in(CollectionUtils.isNotEmpty(request.getSourceIds()), VmCloudDisk::getSourceId, request.getSourceIds());
        return wrapper;
    }

    /**
     * 查询磁盘默认参数
     * 云账号、排除已删除状态的
     */
    private MPJLambdaWrapper<VmCloudDisk> defaultDiskQuery(MPJLambdaWrapper<VmCloudDisk> wrapper,List<String> accountIds){
        wrapper.selectAll(VmCloudDisk.class);
        wrapper.in(CollectionUtils.isNotEmpty(accountIds), VmCloudDisk::getAccountId, accountIds);
        wrapper.leftJoin(CloudAccount.class,CloudAccount::getId, VmCloudDisk::getAccountId);
        wrapper.notIn(true, VmCloudDisk::getStatus, List.of(SpecialAttributesConstants.StatusField.DISK_DELETE));
        return wrapper;
    }

    /**
     * 获取所有云账号
     */
    @Override
    public List<CloudAccount> getAllCloudAccount() {
        return currentUserResourceService.currentUserCloudAccountList();
    }

    /**
     * 磁盘分析参数
     * @param request 磁盘分析参数
     */
    private MPJLambdaWrapper<VmCloudDisk> addDiskAnalysisQuery(ResourceAnalysisRequest request) {
        List<String> sourceIds = permissionService.getSourceIds();
        if (!CurrentUserUtils.isAdmin() && CollectionUtils.isNotEmpty(sourceIds)) {
            request.setSourceIds(sourceIds);
        }
        if(CollectionUtils.isEmpty(request.getAccountIds())){
            request.setAccountIds(currentUserResourceService.currentUserCloudAccountList().stream().map(CloudAccount::getId).toList());
        }
        MPJLambdaWrapper<VmCloudDisk> wrapper = defaultDiskQuery(new MPJLambdaWrapper<>(),request.getAccountIds());
        wrapper.selectAs(CloudAccount::getName, AnalysisDiskDTO::getAccountName);
        wrapper.selectAs(CloudAccount::getPlatform,AnalysisDiskDTO::getPlatform);
        wrapper.in(CollectionUtils.isNotEmpty(request.getSourceIds()), VmCloudDisk::getSourceId, request.getSourceIds());
        return wrapper;
    }

    /**
     * 磁盘分布查询
     * 包括按云账号、状态、类型分布
     */
    @Override
    public Map<String, List<KeyValue>> spread(ResourceAnalysisRequest request) {
        Map<String,List<KeyValue>> result = new HashMap<>(1);
        Map<String,CloudAccount> accountMap = iServerAnalysisService.getAllAccountIdMap();
        if(accountMap.size()==0){
            return result;
        }
        MPJLambdaWrapper<VmCloudDisk> queryWrapper = addDiskAnalysisQuery(request);
        List<AnalysisDiskDTO> diskList = baseVmCloudDiskMapper.selectJoinList(AnalysisDiskDTO.class, queryWrapper);
        //把除空闲与挂载状态设置为其他状态
        diskList = diskList.stream().filter(v->accountMap.containsKey(v.getAccountId())).toList();
        diskList.forEach(v->{
            if(!StringUtils.equalsIgnoreCase(v.getStatus(),SpecialAttributesConstants.StatusField.AVAILABLE) && !StringUtils.equalsIgnoreCase(v.getStatus(),SpecialAttributesConstants.StatusField.IN_USE)){
                v.setStatus(SpecialAttributesConstants.StatusField.OTHER);
            }
        });
        diskList = diskList.stream().filter(v->StringUtils.isNotEmpty(v.getAccountId())).toList();
        Map<String,String> statusMap = new HashMap<>(3);
        statusMap.put(SpecialAttributesConstants.StatusField.AVAILABLE,"空闲");
        statusMap.put(SpecialAttributesConstants.StatusField.IN_USE,"已挂载");
        statusMap.put(SpecialAttributesConstants.StatusField.OTHER,"其他");
        if(request.isStatisticalBlock()){
            Map<String,Long> byAccountMap = diskList.stream().collect(Collectors.groupingBy(AnalysisDiskDTO::getAccountId, Collectors.counting()));
            result.put("byAccount",byAccountMap.entrySet().stream().map(c -> new KeyValue(StringUtils.isEmpty(accountMap.get(c.getKey()).getName())?c.getKey():accountMap.get(c.getKey()).getName(), c.getValue())).toList());
            Map<String,Long> byStatusMap = diskList.stream().collect(Collectors.groupingBy(AnalysisDiskDTO::getStatus, Collectors.counting()));
            result.put("byStatus",byStatusMap.entrySet().stream().map(c -> new KeyValue(statusMap.get(c.getKey()), c.getValue())).toList());
            Map<String,Long> byTypeMap = diskList.stream().collect(Collectors.groupingBy(AnalysisDiskDTO::getDiskType, Collectors.counting()));
            result.put("byType",byTypeMap.entrySet().stream().map(c -> new KeyValue(DiskTypeConstants.getName(c.getKey()), c.getValue())).toList());
        }else{
            Map<String,LongSummaryStatistics> byAccountMap = diskList.stream().filter(v->StringUtils.isNotEmpty(v.getAccountId())).collect(Collectors.groupingBy(AnalysisDiskDTO::getAccountId, Collectors.summarizingLong(AnalysisDiskDTO::getSize)));
            result.put("byAccount",byAccountMap.entrySet().stream().map(c -> new KeyValue(StringUtils.isEmpty(accountMap.get(c.getKey()).getName())?c.getKey():accountMap.get(c.getKey()).getName(), c.getValue().getSum())).toList());
            Map<String,LongSummaryStatistics> byStatusMap = diskList.stream().collect(Collectors.groupingBy(AnalysisDiskDTO::getStatus, Collectors.summarizingLong(AnalysisDiskDTO::getSize)));
            result.put("byStatus",byStatusMap.entrySet().stream().map(c -> new KeyValue(statusMap.get(c.getKey()), c.getValue().getSum())).toList());
            Map<String,LongSummaryStatistics> byTypeMap = diskList.stream().collect(Collectors.groupingBy(AnalysisDiskDTO::getDiskType, Collectors.summarizingLong(AnalysisDiskDTO::getSize)));
            result.put("byType",byTypeMap.entrySet().stream().map(c -> new KeyValue(DiskTypeConstants.getName(c.getKey()), c.getValue().getSum())).toList());
        }
        return result;
    }

    /**
     * 磁盘趋势
     * 统计一段时间内所有新增的磁盘数量生产趋势数据
     */
    @Override
    public List<ChartData> diskIncreaseTrend(ResourceAnalysisRequest request) {
        List<ChartData> tempChartDataList = new ArrayList<>();
        List<CloudAccount> accountList = getAllCloudAccount();
        Map<String,CloudAccount> accountMap = accountList.stream().collect(Collectors.toMap(CloudAccount::getId,v->v,(k1,k2)->k1));
        if(accountMap.size()==0){
            return tempChartDataList;
        }
        MPJLambdaWrapper<VmCloudDisk>  queryWrapper = addDiskAnalysisQuery(request);
        List<AnalysisDiskDTO> diskList = baseVmCloudDiskMapper.selectJoinList(AnalysisDiskDTO.class, queryWrapper);
        if(CollectionUtils.isNotEmpty(diskList)){
            //格式化创建时间,删除时间
            diskList = diskList.stream().filter(v->accountMap.containsKey(v.getAccountId())).filter(v->Objects.nonNull(v.getCreateTime())).toList();
            diskList.forEach(v->{
                v.setCreateMonth(v.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                if(Objects.nonNull(v.getUpdateTime()) && StringUtils.equalsIgnoreCase(v.getStatus(),SpecialAttributesConstants.StatusField.DISK_DELETE)){
                    v.setDeleteMonth(v.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
            });
            Map<String,List<AnalysisDiskDTO>> accountGroup = diskList.stream().filter(v->StringUtils.isNotEmpty(v.getAccountId())).collect(Collectors.groupingBy(AnalysisDiskDTO::getAccountId));
            setDiskIncreaseTrendData(accountGroup, request.isStatisticalBlock(), request.getDayNumber(), tempChartDataList);
        }
        return tempChartDataList;
    }

    private void setDiskIncreaseTrendData(Map<String,List<AnalysisDiskDTO>> accountGroup,boolean isStatisticalBlock,Long days,List<ChartData> tempChartDataList){
        List<String> dateRangeList = iServerAnalysisService.getRangeDateStrList(days);
        for (Map.Entry<String,List<AnalysisDiskDTO>> entry : accountGroup.entrySet()) {
            String accountId = entry.getKey();
            Map<String, Long> month;
            Map<String, Long> delMonth;
            //容量
            if (!isStatisticalBlock) {
                month = accountGroup.get(accountId).stream().collect(Collectors.groupingBy(AnalysisDiskDTO::getCreateMonth, Collectors.summingLong(AnalysisDiskDTO::getSize)));
                delMonth = accountGroup.get(accountId).stream().filter(v -> StringUtils.equalsIgnoreCase(v.getStatus(), SpecialAttributesConstants.StatusField.DISK_DELETE) && StringUtils.isNotEmpty(v.getDeleteMonth())).collect(Collectors.groupingBy(AnalysisDiskDTO::getDeleteMonth, Collectors.summingLong(AnalysisDiskDTO::getSize)));
            } else {
                month = accountGroup.get(accountId).stream().collect(Collectors.groupingBy(AnalysisDiskDTO::getCreateMonth, Collectors.counting()));
                delMonth = accountGroup.get(accountId).stream().filter(v -> StringUtils.equalsIgnoreCase(v.getStatus(), SpecialAttributesConstants.StatusField.DISK_DELETE) && StringUtils.isNotEmpty(v.getDeleteMonth())).collect(Collectors.groupingBy(AnalysisDiskDTO::getDeleteMonth, Collectors.counting()));
            }
            Map<String, Long> finalMonth = month;
            Map<String, Long> finalDelMonth = delMonth;
            dateRangeList.forEach(dateStr -> {
                ChartData chartData = new ChartData();
                chartData.setXAxis(dateStr);
                chartData.setGroupName(accountGroup.get(accountId).get(0).getAccountName());
                chartData.setYAxis(new BigDecimal(iServerAnalysisService.getResourceTotalDateBefore(finalMonth, dateStr) - iServerAnalysisService.getResourceTotalDateBefore(finalDelMonth, dateStr)));
                tempChartDataList.add(chartData);
            });
        }
    }

    /**
     * 磁盘在组织工作空间上的分布
     */
    @Override
    public Map<String,List<TreeNode>> analysisCloudDiskByOrgWorkspace(ResourceAnalysisRequest request){
        Map<String,List<TreeNode>> result = new HashMap<>(2);
        // 当前用户所拥有的磁盘所属云账号集合
        if(CollectionUtils.isEmpty(request.getAccountIds())){
            request.setAccountIds(currentUserResourceService.currentUserCloudDiskList().stream()
                    .filter(v->StringUtils.isNotEmpty(v.getAccountId()))
                    .map(VmCloudDisk::getAccountId).distinct().toList());
        }
        List<TreeNode> workspaceList =  workspaceSpread(request);
        if(request.isAnalysisWorkspace()){
            result.put("all",workspaceList);
            result.put("tree",workspaceList);
        }else{
            result = orgSpread(request,workspaceList);

        }
        return result;
    }

    /**
     * 组织上的分布
     */
    private Map<String,List<TreeNode>> orgSpread(ResourceAnalysisRequest request, List<TreeNode> workspaceList){
        Map<String,List<TreeNode>> result = new HashMap<>(2);
        result.put("all",new ArrayList<>());
        result.put("tree",new ArrayList<>());
        //工作空间添加标识
        workspaceList.forEach(v-> v.setName(v.getName()+"(工作空间)"));
        //工作空间按照父级ID分组
        Map<String,List<TreeNode>> workspaceGroupByPid = workspaceList.stream().collect(Collectors.groupingBy(TreeNode::getPid));
        //查询所有组织，初始化为chart数据
        List<TreeNode> orgList = iServerAnalysisService.orgToTreeNode();
        MPJLambdaWrapper<VmCloudDisk> queryWrapper = addDiskAnalysisQuery(request);
        if(request.isStatisticalBlock()){
            queryWrapper.selectCount(VmCloudDisk::getId,AnalysisDiskDTO::getValue);
        }else{
            queryWrapper.selectSum(VmCloudDisk::getSize,AnalysisDiskDTO::getValue);
        }
        List<AnalysisDiskDTO> diskList = baseVmCloudDiskMapper.selectJoinList(AnalysisDiskDTO.class, queryWrapper);
        int unauthorizedNumber = 0;
        if(CollectionUtils.isNotEmpty(diskList)){
            unauthorizedNumber = diskList.stream().filter(Objects::nonNull).mapToInt(AnalysisDiskDTO::getValue).sum();
        }
        TreeNode unauthorizedNode = new TreeNode();
        if(unauthorizedNumber>0){
            unauthorizedNode.setName("未授权");
            unauthorizedNode.setId("unauthorized");
            unauthorizedNode.setGroupName("org");
            unauthorizedNode.setValue(unauthorizedNumber);
            result.put("all", List.of(unauthorizedNode));
            result.put("tree", List.of(unauthorizedNode));
        }
        if(CollectionUtils.isEmpty(orgList)){
            return result;
        }
        //id集合
        List<String> orgIds = orgList.stream().map(TreeNode::getId).toList();
        //设置资源统计数量
        List<TreeNode> chartDateWorkspaceList = getTreeNodeValueDataForType(request, orgIds,"org");
        if(CollectionUtils.isEmpty(chartDateWorkspaceList)){
            return result;
        }
        Map<String,TreeNode> dataMap = chartDateWorkspaceList.stream().collect(Collectors.toMap(TreeNode::getId,o->o));
        orgList.forEach(v->{
            if(dataMap.containsKey(v.getId())){
                v.setValue(dataMap.get(v.getId()).getValue());
            }
        });
        Map<String,TreeNode> childrenMap = orgList.stream().collect(Collectors.toMap(TreeNode::getId,o->o));
        List<TreeNode> childrenList = new ArrayList<>();
        //设置子级并累加值
        for(TreeNode v:orgList){
            childrenList.clear();
            // 自己没有资源的就不要
            childrenList.add(new TreeNode(v.getId(), v.getName() + "(未授权)", v.getValue(), v.getGroupName(), v.getPid()));
            // 父组织工作空间下资源数量
            int workspaceResourceNumber = getWorkspaceResourceNumber(workspaceGroupByPid, childrenList, v);
            v.setValue(v.getValue()+workspaceResourceNumber);
            for(String key:childrenMap.keySet()){
                TreeNode node = childrenMap.get(key);
                if(StringUtils.equalsIgnoreCase(node.getPid(),v.getId())){
                    // 子组织工作空间下资源数量
                    workspaceResourceNumber = getWorkspaceResourceNumber(workspaceGroupByPid, childrenList, node);
                    v.setValue(v.getValue()+node.getValue()+workspaceResourceNumber);
                    childrenList.add(node);
                }
            }
            // 只显示有资源的
            v.setChildren(childrenList.stream().filter(c->c.getValue()>0).collect(Collectors.toList()));
        }
        //所有-已授权=未授权
        unauthorizedNode.setValue(( unauthorizedNumber - Integer.parseInt(String.valueOf(orgList.stream().mapToLong(TreeNode::getValue).sum()))));
        orgList.add(unauthorizedNode);
        // 扁平数据
        result.put("all",orgList);
        // 获取所有父节点,pid为空表示顶级，或者pid不在当前集合中，则也作为顶级
        List<TreeNode> parentNodeList = orgList.stream().filter(v->StringUtils.isEmpty(v.getPid()) || !orgIds.contains(v.getPid())).collect(Collectors.toList());
        //组织管理员的话，只有一个跟节点，然后只返回他的子集
        if (CurrentUserUtils.isOrgAdmin()) {
            result.put("tree",parentNodeList.get(0).getChildren().stream().filter(v->v.getValue()>0).collect(Collectors.toList()));
        }else{
            result.put("tree", parentNodeList.stream().filter(v->v.getValue()>0).collect(Collectors.toList()));
        }
        return result;
    }

    private static int getWorkspaceResourceNumber(Map<String, List<TreeNode>> workspaceGroupByPid, List<TreeNode> childrenList, TreeNode v) {
        int workspaceResourceNumber = 0;
        if(workspaceGroupByPid.containsKey(v.getId())){
            childrenList.addAll(workspaceGroupByPid.get(v.getId()));
            workspaceResourceNumber = workspaceGroupByPid.get(v.getId()).stream().mapToInt(TreeNode::getValue).sum();
        }
        return workspaceResourceNumber;
    }

    /**
     * 工作空间上分布
     * @param request 磁盘分析参数
     * @return List<BarTreeChartData>
     */
    private List<TreeNode> workspaceSpread(ResourceAnalysisRequest request){
        List<String> workspaceIds = iServerAnalysisService.workspaceToTreeNode().stream().map(TreeNode::getId).toList();
        if(!CurrentUserUtils.isAdmin() && CollectionUtils.isEmpty(workspaceIds)){
            return new ArrayList<>();
        }
        List<TreeNode> result = getTreeNodeValueDataForType(request,workspaceIds,"workspace");
        return result;
    }

    private List<TreeNode> getTreeNodeValueDataForType(ResourceAnalysisRequest request, List<String> orgWorkspaceIds , String type){
        // 查询组织或工作空间下磁盘统计数
        MPJLambdaWrapper<OrgWorkspace> wrapper = new MPJLambdaWrapper<>();
        // 返回内容
        wrapper.selectAs(OrgWorkspace::getId,TreeNode::getId);
        wrapper.selectAs(OrgWorkspace::getName,TreeNode::getName);
        wrapper.selectAs(OrgWorkspace::getPid,TreeNode::getPid);
        wrapper.selectAs(OrgWorkspace::getType,TreeNode::getGroupName);
        if(request.isStatisticalBlock()){
            wrapper.selectCount(VmCloudDisk::getId,TreeNode::getValue);
        }else{
            wrapper.selectSum(VmCloudDisk::getSize,TreeNode::getValue);
        }
        // 查询条件
        wrapper.eq(true, OrgWorkspace::getType,type);
        boolean filterOrgWorkspace = !CurrentUserUtils.isAdmin() && CollectionUtils.isNotEmpty(orgWorkspaceIds);
        wrapper.in(filterOrgWorkspace,OrgWorkspace::getId, orgWorkspaceIds);
        boolean filterAccount = CollectionUtils.isNotEmpty(request.getAccountIds());
        wrapper.in(filterAccount,VmCloudDisk::getAccountId, request.getAccountIds());
        wrapper.notIn(true,VmCloudDisk::getStatus, List.of(SpecialAttributesConstants.StatusField.DISK_DELETE,SpecialAttributesConstants.StatusField.FAILED));
        wrapper.leftJoin(VmCloudDisk.class,VmCloudDisk::getSourceId,OrgWorkspace::getId);
        wrapper.groupBy(true,OrgWorkspace::getId,OrgWorkspace::getName);
        return orgWorkspaceMapper.selectJoinList(TreeNode.class, wrapper);
    }

    @Override
    public long countDiskByCloudAccount(String cloudAccountId) {
        List<String> sourceIds = permissionService.getSourceIds();
        MPJLambdaWrapper<VmCloudDisk> wrapper = new MPJLambdaWrapper<>();
        wrapper.isNotNull(true,VmCloudDisk::getAccountId);
        wrapper.eq(StringUtils.isNotEmpty(cloudAccountId),VmCloudDisk::getAccountId,cloudAccountId);
        wrapper.in(!CurrentUserUtils.isAdmin() && CollectionUtils.isNotEmpty(sourceIds),VmCloudServer::getSourceId,sourceIds);
        wrapper.notIn(true, VmCloudDisk::getStatus, List.of(SpecialAttributesConstants.StatusField.DISK_DELETE));
        return baseVmCloudDiskMapper.selectCount(wrapper);
    }


}
