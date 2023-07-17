package com.fit2cloud.controller.request.vm;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * @author : LiuDi
 * @date : 2022/12/21 15:29
 */
@Getter
@Setter
public class ChangeServerConfigRequest extends HashMap<String, Object> {
    public String getId() {
        return get("id").toString();
    }

    public String getNewInstanceType() {
        return get("newInstanceType").toString();
    }

    public String getInstanceChargeType() {
        return get("instanceChargeType").toString();
    }
}
