package com.fit2cloud.common.job_record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/31  14:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobLink {
    /**
     * 任务环节描述
     */
    private String description;
    /**
     * 任务环节类型
     */
    private JobLinkTypeConstants type;
}
