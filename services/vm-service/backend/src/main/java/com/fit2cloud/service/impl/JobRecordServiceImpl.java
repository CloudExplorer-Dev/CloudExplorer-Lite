package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.base.entity.JobRecordResourceMapping;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseJobRecordMapper;
import com.fit2cloud.base.mapper.BaseJobRecordResourceMappingMapper;
import com.fit2cloud.base.mapper.BaseVmCloudDiskMapper;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.controller.request.jobrecord.PageJobRecordRequest;
import com.fit2cloud.dto.JobRecordDTO;
import com.fit2cloud.service.IJobRecordService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class JobRecordServiceImpl implements IJobRecordService {

    @Resource
    private BaseJobRecordMapper baseJobRecordMapper;
    @Resource
    private BaseJobRecordResourceMappingMapper baseJobRecordResourceMappingMapper;
    @Resource
    private BaseVmCloudServerMapper baseVmCloudServerMapper;
    @Resource
    private BaseVmCloudDiskMapper baseVmCloudDiskMapper;

    @Override
    public IPage<JobRecordDTO> pageJobRecord(PageJobRecordRequest request) {

        Page<JobRecordDTO> page = PageUtil.of(request, JobRecordDTO.class, new OrderItem(ColumnNameUtil.getColumnName(JobRecordDTO::getCreateTime, false), false), false);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //这里不联表，直接借用联表查询的返回DTO
        MPJLambdaWrapper<JobRecord> wrapper = new MPJLambdaWrapper<JobRecord>()
                .selectAll(JobRecord.class)
                .like(StringUtils.isNotBlank(request.getId()), JobRecord::getId, request.getId())
                .like(StringUtils.isNotBlank(request.getDescription()), JobRecord::getDescription, request.getDescription())
                .in(JobRecord::getType, Arrays.asList(
                        JobTypeConstants.CLOUD_SERVER_OPERATE_JOB,
                        JobTypeConstants.CLOUD_SERVER_CREATE_JOB,
                        JobTypeConstants.CLOUD_SERVER_DELETE_JOB,
                        JobTypeConstants.CLOUD_SERVER_START_JOB,
                        JobTypeConstants.CLOUD_SERVER_STOP_JOB,
                        JobTypeConstants.CLOUD_SERVER_RESTART_JOB,
                        JobTypeConstants.CLOUD_SERVER_CONFIG_CHANGE_JOB,
                        JobTypeConstants.CLOUD_DISK_OPERATE_JOB,
                        JobTypeConstants.CLOUD_DISK_CREATE_JOB,
                        JobTypeConstants.CLOUD_DISK_DELETE_JOB,
                        JobTypeConstants.CLOUD_DISK_ATTACH_JOB,
                        JobTypeConstants.CLOUD_DISK_DETACH_JOB,
                        JobTypeConstants.CLOUD_DISK_ENLARGE_JOB
                ))
                .in(CollectionUtils.isNotEmpty(request.getType()), JobRecord::getType, request.getType())
                .in(CollectionUtils.isNotEmpty(request.getStatus()), JobRecord::getStatus, request.getStatus());

        if (CollectionUtils.isNotEmpty(request.getCreateTime())) {
            wrapper.between(JobRecord::getCreateTime, simpleDateFormat.format(request.getCreateTime().get(0)), simpleDateFormat.format(request.getCreateTime().get(1)));
        }
        if (CollectionUtils.isNotEmpty(request.getFinishTime())) {
            wrapper.between(JobRecord::getFinishTime, simpleDateFormat.format(request.getFinishTime().get(0)), simpleDateFormat.format(request.getFinishTime().get(1)));
        }

        IPage<JobRecordDTO> result = baseJobRecordMapper.selectJoinPage(page, JobRecordDTO.class, wrapper);

        List<String> jobRecordIds = result.getRecords().stream().map(JobRecord::getId).toList();

        List<JobRecordResourceMapping> jobRecordResourceMappings = CollectionUtils.isNotEmpty(jobRecordIds) ? baseJobRecordResourceMappingMapper.selectList(new LambdaQueryWrapper<JobRecordResourceMapping>().in(JobRecordResourceMapping::getJobRecordId, jobRecordIds)) : new ArrayList<>();

        Set<String> serverIds = new HashSet<>();
        Map<String, List<String>> serverIdMaps = new HashMap<>();
        Set<String> diskIds = new HashSet<>();
        Map<String, List<String>> diskIdMaps = new HashMap<>();

        jobRecordResourceMappings.forEach(mapping -> {
            if (StringUtils.isNotBlank(mapping.getResourceId())) {
                if (ResourceTypeEnum.CLOUD_SERVER.getCode().equals(mapping.getResourceType())) {
                    serverIds.add(mapping.getResourceId());
                    serverIdMaps.computeIfAbsent(mapping.getJobRecordId(), k -> new ArrayList<>());
                    serverIdMaps.get(mapping.getJobRecordId()).add(mapping.getResourceId());
                } else if (ResourceTypeEnum.CLOUD_DISK.getCode().equals(mapping.getResourceType())) {
                    diskIds.add(mapping.getResourceId());
                    diskIdMaps.computeIfAbsent(mapping.getJobRecordId(), k -> new ArrayList<>());
                    diskIdMaps.get(mapping.getJobRecordId()).add(mapping.getResourceId());
                }
            }
        });

        Map<String, VmCloudServer> servers = CollectionUtils.isNotEmpty(serverIds) ? baseVmCloudServerMapper.selectBatchIds(serverIds).stream().collect(Collectors.toMap(VmCloudServer::getId, server -> server)) : new HashMap<>();

        Map<String, VmCloudDisk> disks = CollectionUtils.isNotEmpty(diskIds) ? baseVmCloudDiskMapper.selectBatchIds(diskIds).stream().collect(Collectors.toMap(VmCloudDisk::getId, disk -> disk)) : new HashMap<>();

        result.getRecords().forEach(record -> {
            if (record.getServers() == null) {
                record.setServers(new ArrayList<>());
            }
            if (record.getDisks() == null) {
                record.setDisks(new ArrayList<>());
            }
            List<String> serverIdList = serverIdMaps.get(record.getId());
            if (CollectionUtils.isNotEmpty(serverIdList)) {
                for (String s : serverIdList) {
                    if (servers.get(s) != null) {
                        record.getServers().add(servers.get(s));
                    }
                }
            }
            List<String> diskIdList = diskIdMaps.get(record.getId());
            if (CollectionUtils.isNotEmpty(diskIdList)) {
                for (String s : diskIdList) {
                    if (disks.get(s) != null) {
                        record.getDisks().add(disks.get(s));
                    }
                }
            }
        });

        return result;
    }

    @Override
    public JobRecordDTO getRecord(String id) {
        JobRecord record = baseJobRecordMapper.selectById(id);
        if (record == null) {
            return null;
        }
        List<JobRecordResourceMapping> jobRecordResourceMappings = baseJobRecordResourceMappingMapper.selectList(new LambdaQueryWrapper<JobRecordResourceMapping>().eq(JobRecordResourceMapping::getJobRecordId, id));

        JobRecordDTO dto = new JobRecordDTO();
        BeanUtils.copyProperties(record, dto);
        dto.setServers(new ArrayList<>());
        dto.setDisks(new ArrayList<>());

        Set<String> serverIds = new HashSet<>();
        Set<String> diskIds = new HashSet<>();
        jobRecordResourceMappings.forEach(mapping -> {
            if (StringUtils.isNotBlank(mapping.getResourceId())) {
                if (ResourceTypeEnum.CLOUD_SERVER.getCode().equals(mapping.getResourceType())) {
                    serverIds.add(mapping.getResourceId());
                } else if (ResourceTypeEnum.CLOUD_DISK.getCode().equals(mapping.getResourceType())) {
                    diskIds.add(mapping.getResourceId());
                }
            }
        });

        if (CollectionUtils.isNotEmpty(serverIds)) {
            dto.getServers().addAll(baseVmCloudServerMapper.selectBatchIds(serverIds));
        }
        if (CollectionUtils.isNotEmpty(diskIds)) {
            dto.getDisks().addAll(baseVmCloudDiskMapper.selectBatchIds(diskIds));
        }

        return dto;
    }
}
