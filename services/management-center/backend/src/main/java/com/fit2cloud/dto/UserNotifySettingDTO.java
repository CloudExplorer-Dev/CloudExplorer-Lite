package com.fit2cloud.dto;

import com.fit2cloud.base.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: LiuDi
 * Date: 2022/9/7 8:29 PM
 */
@Getter
@Setter
public class UserNotifySettingDTO extends User {

    private String wechatAccount;
}
