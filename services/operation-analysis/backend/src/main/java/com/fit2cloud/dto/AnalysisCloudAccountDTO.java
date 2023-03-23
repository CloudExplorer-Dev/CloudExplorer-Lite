package com.fit2cloud.dto;

import com.fit2cloud.base.entity.CloudAccount;
import lombok.Data;

/**
 * @author jianneng
 * @date 2023/3/20 13:59
 **/
@Data
public class AnalysisCloudAccountDTO extends CloudAccount {
    private long hostCount;
    private long datastoreCount;
    private long vmCount;
    private long diskCount;
}
