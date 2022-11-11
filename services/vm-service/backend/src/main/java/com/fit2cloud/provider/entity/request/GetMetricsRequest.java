package com.fit2cloud.provider.entity.request;

import com.fit2cloud.common.provider.entity.F2CEntityType;
import lombok.Data;

/**
 * @author jianneng
 * @date 2022/10/27 14:36
 **/
@Data
public class GetMetricsRequest extends BaseRequest{

    /**
     * 监控数据间隔 s
     */
    private int period = 60;
    /**
     * 时间区间 h
     */
    private int interval = 1;
    /**
     * 开始时间 通过interval计算
     */
    private String startTime;
    /**
     * 结束时间 通过interval计算
     */
    private String endTime;
    /**
     * 资源类型
     */
    private F2CEntityType entityTypes;
}
