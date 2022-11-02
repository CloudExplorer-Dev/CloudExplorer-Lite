package com.fit2cloud.controller.request;

import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/26 10:02 AM
 */
@Data
@Builder
public class CreateJobRecordRequest {
    /**
     * 资源ID（云主机、磁盘等一些资源的ID）
     */
    private String resourceId;
    /**
     * 资源的操作类型
     */
    private OperatedTypeEnum resourceOperateType;
    /**
     * 资源类型
     */
    private ResourceTypeEnum resourceType;
    /**
     * 任务类型
     */
    private JobTypeConstants jobType;
}
