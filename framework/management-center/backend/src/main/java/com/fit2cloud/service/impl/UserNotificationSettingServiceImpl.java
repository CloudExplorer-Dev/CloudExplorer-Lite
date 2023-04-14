package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.dao.entity.UserNotificationSetting;
import com.fit2cloud.dao.mapper.UserNotificationSettingMapper;
import com.fit2cloud.service.IUserNotificationSettingService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class UserNotificationSettingServiceImpl extends ServiceImpl<UserNotificationSettingMapper, UserNotificationSetting> implements IUserNotificationSettingService {

}
