package com.fit2cloud.service;

import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.base.entity.JobRecordResourceMapping;
import com.fit2cloud.base.service.IBaseJobRecordResourceMappingService;
import com.fit2cloud.base.service.IBaseJobRecordService;
import com.fit2cloud.dto.InitJobRecordDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jianneng
 * @date 2022/10/11 17:52
 **/
@Service
public class JobRecordCommonService {

    @Resource
    private IBaseJobRecordService baseJobRecordService;
    @Resource
    private IBaseJobRecordResourceMappingService jobRecordResourceMappingService;


    /**
     * 初始化任务
     * @param initJobRecordDTO 任务初始化
     * @return
     */
    public JobRecord initJobRecord(InitJobRecordDTO initJobRecordDTO) {
        JobRecord jobRecord = new JobRecord();
        jobRecord.setDescription(initJobRecordDTO.getJobDescription());
        jobRecord.setStatus(initJobRecordDTO.getJobStatus());
        jobRecord.setParams(new HashMap<>());
        jobRecord.setType(initJobRecordDTO.getJobType());
        jobRecord.setCreateTime(initJobRecordDTO.getCreateTime());
        // 插入任务数据
        baseJobRecordService.save(jobRecord);
        // 插入关联关系
        JobRecordResourceMapping jobRecordResourceMapping = new JobRecordResourceMapping();
        jobRecordResourceMapping.setResourceId(initJobRecordDTO.getResourceId());
        jobRecordResourceMapping.setJobType(initJobRecordDTO.getJobType());
        jobRecordResourceMapping.setCreateTime(initJobRecordDTO.getCreateTime());
        jobRecordResourceMapping.setJobRecordId(jobRecord.getId());
        jobRecordResourceMappingService.save(jobRecordResourceMapping);
        return jobRecord;
    }

    public void modifyJobRecord(JobRecord jobRecord) {
        baseJobRecordService.saveOrUpdate(jobRecord);
    }
}
