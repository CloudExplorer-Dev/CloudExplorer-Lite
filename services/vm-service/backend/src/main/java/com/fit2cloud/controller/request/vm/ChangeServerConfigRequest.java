package com.fit2cloud.controller.request.vm;

import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2022/12/21 15:29
 */
@Data
public class ChangeServerConfigRequest {
    private String id;
    private String newInstanceType;
    private int cpu;
    private int memory;
}
