package com.fit2cloud.dto;

import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 初始化任务实体
 * @author jianneng
 * @date 2022/10/13 11:20
 **/
@Data
@Builder
public class InitJobRecordDTO {

    /**
     * 任务描述
     */
    private String jobDescription;
    /**
     * 任务创建时间
     */
    private LocalDateTime createTime;
    /**
     * 资源ID
     */
    private String resourceId;
    /**
     * 资源类型
     */
    private ResourceTypeEnum resourceType;
    /**
     * 任务状态
     */
    private JobStatusConstants jobStatus;
    /**
     * 任务类型
     */
    private JobTypeConstants jobType;
}
