package com.fit2cloud.dto;

import com.fit2cloud.base.entity.VmCloudImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;

/**
 * @author jianneng
 * @date 2022/9/27 14:39
 **/
@Data
public class VmCloudImageDTO extends VmCloudImage {

    @Serial
    private static final long serialVersionUID = 5306701579549617726L;
    @ApiModelProperty("云账号名称")
    private String accountName;
    @ApiModelProperty("所属云主机")
    private String instanceName;
}
