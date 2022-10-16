package com.fit2cloud.controller.request.vm;

import lombok.Data;

import java.util.List;

/**
 * @author jianneng
 * @date 2022/10/9 15:32
 **/
@Data
public class BatchOperateVmRequest {
    private List<String> instanceIds;
    /**
     * 与操作类型对应
     * OperatedTypeEnum
     */
    private String operate;
}
