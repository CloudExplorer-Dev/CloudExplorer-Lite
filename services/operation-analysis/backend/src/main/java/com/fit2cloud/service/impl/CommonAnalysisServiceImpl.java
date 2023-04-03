package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudDiskMapper;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseVmCloudServerService;
import com.fit2cloud.common.page.PageImpl;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.constants.SpecialAttributesConstants;
import com.fit2cloud.dao.mapper.ExtCloudAccountMapper;
import com.fit2cloud.dto.AnalysisCloudAccountDTO;
import com.fit2cloud.service.ICommonAnalysisService;
import com.fit2cloud.service.IPermissionService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jianneng
 * @date 2023/3/20 14:50
 **/

@Service
public class CommonAnalysisServiceImpl implements ICommonAnalysisService {
    @Resource
    private IPermissionService permissionService;
    @Resource
    private ExtCloudAccountMapper extCloudAccountMapper;
    @Resource
    private BaseVmCloudServerMapper baseVmCloudServerMapper;
    @Resource
    private BaseVmCloudDiskMapper baseVmCloudDiskMapper;
    @Resource
    private CurrentUserResourceService currentUserResourceService;

    /**
     * 统计云账号数量
     * @param cloudAccountId 云账号ID
     * @return 数量
     */
    @Override
    public long countCloudAccount(String cloudAccountId) {
        if(StringUtils.isNotEmpty(cloudAccountId)){
            return 1L;
        }
        List<String> cloudAccountIds = currentUserResourceService.currentUserCloudAccountList().stream().map(CloudAccount::getId).toList();
        if(CollectionUtils.isEmpty(cloudAccountIds)){
            return 0L;
        }
        return cloudAccountIds.stream().distinct().toList().size();
    }

    /**
     * 云账号明细
     * @param currentPage 当前页
     * @param limit 大小
     * @param cloudAccountId 云账号ID
     * @return 云账号明细数据
     */
    @Override
    public IPage<AnalysisCloudAccountDTO> cloudAccountDetailed(Integer currentPage, Integer limit, String cloudAccountId) {
        IPage<AnalysisCloudAccountDTO> resultList = new Page<>();
        List<String> cloudAccountIds = new ArrayList<>();
        if(StringUtils.isNotEmpty(cloudAccountId)){
            cloudAccountIds.add(cloudAccountId);
        }else{
            cloudAccountIds.addAll(currentUserResourceService.currentUserCloudAccountList().stream().map(CloudAccount::getId).toList());
        }
        if(CollectionUtils.isEmpty(cloudAccountIds)){
            return resultList;
        }
        Page<AnalysisCloudAccountDTO> page = PageImpl.of(currentPage, limit);
        MPJLambdaWrapper<AnalysisCloudAccountDTO> wrapper = new MPJLambdaWrapper<>();
        wrapper.in(true,AnalysisCloudAccountDTO::getId,cloudAccountIds.stream().distinct().toList());
        wrapper.groupBy(true,CloudAccount::getId);
        resultList = extCloudAccountMapper.pageAccountSummary(page,wrapper);;
        convertResponse(resultList);
        return resultList;
    }

    /**
     * 转换返回对象
     * 统计云账号资源数量
     */
    private void convertResponse(IPage<AnalysisCloudAccountDTO> resultList){
        List<String> sourceIds = permissionService.getSourceIds();
        Map<String,Long> vm = countCloudServerByCloudAccount(sourceIds);
        Map<String,Long> disk = countDiskByCloudAccount(sourceIds);
        resultList.getRecords().forEach(cloudAccount -> {
            if(vm.containsKey(cloudAccount.getId())){
                cloudAccount.setVmCount(vm.get(cloudAccount.getId()));
            }
            if(disk.containsKey(cloudAccount.getId())){
                cloudAccount.setDiskCount(disk.get(cloudAccount.getId()));
            }
            if(!CurrentUserUtils.isAdmin()){
                cloudAccount.setHostCount(0L);
                cloudAccount.setDatastoreCount(0L);
            }
        });
    }


    private Map<String,Long> countCloudServerByCloudAccount(List<String> sourceIds) {
        MPJLambdaWrapper<VmCloudServer> wrapper = new MPJLambdaWrapper<>();
        wrapper.isNotNull(true,VmCloudServer::getAccountId);
        wrapper.in(!CurrentUserUtils.isAdmin() && CollectionUtils.isNotEmpty(sourceIds),VmCloudServer::getSourceId,sourceIds);
        wrapper.notIn(true, VmCloudServer::getInstanceStatus, List.of(SpecialAttributesConstants.StatusField.VM_DELETE,SpecialAttributesConstants.StatusField.FAILED));
        List<VmCloudServer> list = baseVmCloudServerMapper.selectList(wrapper);
        if(CollectionUtils.isNotEmpty(list)){
            return list.stream().collect(Collectors.groupingBy(VmCloudServer::getAccountId,Collectors.counting()));
        }
        return new HashMap<>();
    }

    private Map<String,Long> countDiskByCloudAccount(List<String> sourceIds) {
        MPJLambdaWrapper<VmCloudDisk> wrapper = new MPJLambdaWrapper<>();
        wrapper.isNotNull(true,VmCloudDisk::getAccountId);
        wrapper.in(!CurrentUserUtils.isAdmin() && CollectionUtils.isNotEmpty(sourceIds),VmCloudServer::getSourceId,sourceIds);
        wrapper.notIn(true, VmCloudDisk::getStatus, List.of(SpecialAttributesConstants.StatusField.DISK_DELETE));
        List<VmCloudDisk> list = baseVmCloudDiskMapper.selectList(wrapper);
        if(CollectionUtils.isNotEmpty(list)){
            return list.stream().collect(Collectors.groupingBy(VmCloudDisk::getAccountId,Collectors.counting()));
        }
        return new HashMap<>();
    }

}
