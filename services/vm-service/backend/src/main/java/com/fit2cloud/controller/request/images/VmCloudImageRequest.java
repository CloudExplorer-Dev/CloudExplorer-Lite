package com.fit2cloud.controller.request.images;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


import java.io.Serial;
import java.io.Serializable;


@Accessors(chain = true)
@Data
public class VmCloudImageRequest  implements Serializable {

    @Serial
    private static final long serialVersionUID = 6121940016880656998L;

    @ApiModelProperty("云账号ID")
    private String accountId;

    @ApiModelProperty("数据中心/区域")
    private String region;



}
