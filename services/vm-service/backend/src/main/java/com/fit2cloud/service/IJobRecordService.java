package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.request.jobrecord.PageJobRecordRequest;
import com.fit2cloud.dto.JobRecordDTO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IJobRecordService {

    IPage<JobRecordDTO> pageJobRecord(PageJobRecordRequest request);

    JobRecordDTO getRecord(String id);
}
