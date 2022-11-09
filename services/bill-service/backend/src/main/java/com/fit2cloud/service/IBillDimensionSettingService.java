package com.fit2cloud.service;

import com.fit2cloud.dao.entity.BillDimensionSetting;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.dao.jentity.BillAuthorizeRule;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IBillDimensionSettingService extends IService<BillDimensionSetting> {

    /**
     * @param groupField 字段分组
     * @return 获取字段分组
     */
    List<DefaultKeyValue<String, String>> authorizeValues(String groupField);

    /**
     * 所有可以授权的字段
     *
     * @return 返回所有可以授权的字段
     */
    List<DefaultKeyValue<String, String>> authorizeKeys();

    /**
     * 获取账单授权规则
     *
     * @param authorizeId 授权对象id
     * @param type        授权对象类型
     * @return
     */
    BillDimensionSetting getBillDimensionSetting(String authorizeId, String type);


    /**
     * 插入或者修改授权对象
     *
     * @param authorizeId   授权对象id
     * @param type          授权对象类型
     * @param authorizeRule 授权规则
     * @return
     */
    BillDimensionSetting saveOrUpdate(String authorizeId, String type, BillAuthorizeRule authorizeRule);

    /**
     * 根据授权规则授权数据
     */
    void authorize();

    /**
     * 授权
     *
     * @param billDimensionSetting 授权规则设置对象
     */
    void authorize(BillDimensionSetting billDimensionSetting);
}
