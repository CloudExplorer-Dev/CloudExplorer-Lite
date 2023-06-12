package com.fit2cloud.controller.request.images;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;


@Accessors(chain = true)
@Data
public class VmCloudImageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 6121940016880656998L;

    @Schema(title = "云账号ID")
    private String accountId;

    @Schema(title = "数据中心/区域")
    private String region;

    @Schema(title = "区域")
    private String regionId;

    @Schema(title = "操作系统")
    private String os;

}
