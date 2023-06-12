package com.fit2cloud.dto.operation;

import com.fit2cloud.base.entity.VmCloudServerStatusTiming;
import lombok.Data;

/**
 * @author jianneng
 * @date 2023/6/7 10:52
 **/
@Data
public class VmCloudServerStatusTimingDTO extends VmCloudServerStatusTiming {

    private Long lastRunningDuration;
    private Long lastShutdownDuration;
}
