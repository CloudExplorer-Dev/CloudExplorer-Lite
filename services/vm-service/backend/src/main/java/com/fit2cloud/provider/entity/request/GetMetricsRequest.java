package com.fit2cloud.provider.entity.request;

import com.fit2cloud.common.provider.entity.F2CEntityType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jianneng
 * @date 2022/10/27 14:36
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class GetMetricsRequest extends BaseRequest {

    /**
     * 监控数据间隔 s
     */
    private int period = 60;
    /**
     * 时间区间 h
     */
    private int interval = 1;
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 开始同步的时间
     */
    private String syncTimeStampStr;
    /**
     * 资源类型
     */
    private F2CEntityType entityTypes;
}
