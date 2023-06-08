package com.fit2cloud.autoconfigure;

import com.fit2cloud.common.charging.setting.BillSetting;
import com.fit2cloud.common.utils.ClassScanUtil;
import com.fit2cloud.dto.charging.ChargingModuleInfo;
import com.fit2cloud.dto.module.ModuleInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/30  19:00}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class ChargingConfig implements ApplicationContextAware {

    private static final ChargingModuleInfo chargingModuleInfo = new ChargingModuleInfo();

    @Resource
    private ServerInfo serverInfo;

    /**
     * 获取系统定时任务
     *
     * @return 返回当前系统的定时任务设置
     */
    private static List<BillSetting> getModuleBillingSetting() {
        List<Class<?>> classList = ClassScanUtil.getClassList("com.fit2cloud");
        return classList.stream().filter(ChargingConfig.Config.class::isAssignableFrom).flatMap(clazz -> {
            try {
                return ((ChargingConfig.Config) clazz.getConstructor().newInstance()).listBillingSetting().stream();
            } catch (Exception e) {
                return null;
            }
        }).filter(Objects::nonNull).toList();

    }

    /**
     * @return 模块定时任务
     */
    public static ChargingModuleInfo getBillSettings() {
        return chargingModuleInfo;
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        ModuleInfo moduleInfo = serverInfo.getModuleInfo();
        BeanUtils.copyProperties(moduleInfo, chargingModuleInfo);
        chargingModuleInfo.setBillSettings(getModuleBillingSetting());
    }


    public interface Config {
        /**
         * 获取所有的任务详情
         *
         * @return 任务详情
         */
        List<BillSetting> listBillingSetting();
    }
}
