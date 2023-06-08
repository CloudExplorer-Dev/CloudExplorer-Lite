package com.fit2cloud.dto.charging;

import com.fit2cloud.common.charging.setting.BillSetting;
import com.fit2cloud.dto.module.ModuleInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/30  19:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ChargingModuleInfo extends ModuleInfo implements Serializable {
    /**
     * 模块名称
     */
    private String module;
    /**
     * 模块定时任务
     */
    private List<BillSetting> billSettings;
}
