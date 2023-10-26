package com.fit2cloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/10/26  15:57}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
@NoArgsConstructor
public class VmCloudServerDownloadDTO {
    @ExcelProperty("名称")
    private String instanceName;
    @ExcelProperty("备注")
    private String remark;
    @ExcelProperty("IP地址")
    private String ipArray;

    @ExcelProperty("状态")
    private String instanceStatus;

    @ExcelProperty("云账号")
    private String accountName;

    @ExcelProperty("区域/数据中心")
    private String region;

    @ExcelProperty("可用区/数据集")
    private String zone;
    @ExcelProperty("组织")
    private String organizationName;
    @ExcelProperty("工作空间")
    private String workspaceName;
    @ExcelProperty("实例规格")
    private String instanceTypeDescription;
    @ExcelProperty("操作系统版本")
    private String osVersion;
    @ExcelProperty("付费类型")
    private String instanceChargeType;
    @ExcelProperty("宿主机")
    private String host;
    @ExcelProperty("VMTools状态")
    private String vmToolsStatus;
    @ExcelProperty("到期时间")
    private LocalDateTime expiredTime;
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
    @ExcelProperty("创建人")
    private String applyUser;
    @ExcelProperty("自动续费")
    private Boolean autoRenew;

    public VmCloudServerDownloadDTO(VmCloudServerDTO vmCloudServerDTO) {
        BeanUtils.copyProperties(vmCloudServerDTO, this);
    }
}
