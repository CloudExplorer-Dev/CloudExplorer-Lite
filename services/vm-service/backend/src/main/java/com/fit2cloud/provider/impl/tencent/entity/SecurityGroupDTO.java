package com.fit2cloud.provider.impl.tencent.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : LiuDi
 * @date : 2022/11/25 17:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecurityGroupDTO {

    /**
     * ID
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String desc;

}
