package com.fit2cloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/10/26  16:37}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
@NoArgsConstructor
public class VmCloudDiskDownloadDTO {
    @ExcelProperty(value = "名称")
    private String diskName;

    @ExcelProperty("云账号")
    private String accountName;

    @ExcelProperty(value = "数据中心/区域")
    private String region;

    @ExcelProperty(value = "集群/可用区")
    private String zone;

    @ExcelProperty(value = "组织")
    private String organizationName;

    @ExcelProperty(value = "工作空间")
    private String workspaceName;


    @ExcelProperty("所属云主机")
    private String vmInstanceName;

    @ExcelProperty("磁盘属性")
    private String bootableText;

    @ExcelProperty("磁盘类型")
    private String diskType;

    @ExcelProperty(value = "大小(GB)")
    private Long size;
    @ExcelProperty(value = "状态")
    private String status;

    @ExcelProperty(value = "随实例删除")
    private String deleteWithInstance;

    @ExcelProperty(value = "挂载信息")
    private String device;

    @ExcelProperty(value = "创建时间")
    private LocalDateTime createTime;

    public VmCloudDiskDownloadDTO(VmCloudDiskDTO vmCloudDiskDTO) {
        BeanUtils.copyProperties(vmCloudDiskDTO, this);
    }
}
