package com.fit2cloud.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.io.Serial;


/**
 * <p>
 * 
 * </p>
 *
 *
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_notification_setting")
public class UserNotificationSetting implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    private String userId;

    @TableField("wechat_account")
    private String wechatAccount;
}
