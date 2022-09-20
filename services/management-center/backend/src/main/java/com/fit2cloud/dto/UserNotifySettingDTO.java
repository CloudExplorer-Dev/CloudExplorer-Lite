package com.fit2cloud.dto;

import com.fit2cloud.base.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * Author: LiuDi
 * Date: 2022/9/7 8:29 PM
 */

@Accessors(chain = true)
@Getter
@Setter
public class UserNotifySettingDTO extends User {

    @Serial
    private static final long serialVersionUID = -9161365005997349801L;

    private String wechatAccount;
}
