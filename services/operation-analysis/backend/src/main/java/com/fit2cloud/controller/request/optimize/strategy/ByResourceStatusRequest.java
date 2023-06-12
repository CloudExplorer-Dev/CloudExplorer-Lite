package com.fit2cloud.controller.request.optimize.strategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 按资源状态进行优化建议
 *
 * @author jianneng
 * @date 2023/4/6 15:20
 **/
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ByResourceStatusRequest {

    @Schema(title = "包年包月")
    @JsonDeserialize(contentUsing = BaseStatusRequestDeserializer.class)
    private BaseStatusRequest cycle;

    @Schema(title = "按需按量")
    @JsonDeserialize(contentUsing = BaseStatusRequestDeserializer.class)
    private BaseStatusRequest volume;

    @Schema(title = "回收")
    @JsonDeserialize(contentUsing = BaseStatusRequestDeserializer.class)
    private BaseStatusRequest recovery;

}
