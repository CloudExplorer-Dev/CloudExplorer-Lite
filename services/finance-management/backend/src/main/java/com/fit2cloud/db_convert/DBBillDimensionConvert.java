package com.fit2cloud.db_convert;

import com.fit2cloud.common.db_convert.DBConvert;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.dao.entity.BillDimensionSetting;
import com.fit2cloud.dao.jentity.BillAuthorizeRule;
import com.fit2cloud.dao.jentity.BillAuthorizeRuleCondition;
import com.fit2cloud.dao.mapper.BillDimensionSettingMapper;
import com.fit2cloud.db_convert.entity.OldBillDimensionSetting;
import com.fit2cloud.db_convert.entity.json_entity.OldBillAuthorizeRule;
import com.fit2cloud.service.IBillDimensionSettingService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/17  10:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class DBBillDimensionConvert implements DBConvert {
    @Override
    public void execute() {
        IBillDimensionSettingService billDimensionSettingService = SpringUtil.getBean(IBillDimensionSettingService.class);
        BillDimensionSettingMapper billDimensionSettingMapper = SpringUtil.getBean(BillDimensionSettingMapper.class);
        // 查询到历史数据
        List<OldBillDimensionSetting> oldBillDimensionSettings = billDimensionSettingMapper.listOldData();
        // 转换数据
        List<BillDimensionSetting> billDimensionSettings = oldBillDimensionSettings.stream().filter(this::isConvert).map(this::convert).toList();
        // 更新数据
        billDimensionSettingService.updateBatchById(billDimensionSettings);
    }

    /**
     * 判断当前数据是否是旧结构数据
     * 如果是旧结构数据 那么则需要转换 反正则不需要转换
     * 这里判断数据里面有没有children这个字段从而判断是否是旧数据
     *
     * @param billDimensionSetting 当前数据
     * @return 是否需要需要转换
     */
    private boolean isConvert(OldBillDimensionSetting billDimensionSetting) {
        return Objects.nonNull(billDimensionSetting)
                && Objects.nonNull(billDimensionSetting.getCurrentAuthorizeRule())
                && !billDimensionSetting.getCurrentAuthorizeRule().containsKey("children");

    }

    /**
     * 转换函数
     *
     * @param oldBillDimensionSetting 旧数据
     * @return 转换后的数据
     */
    private BillDimensionSetting convert(OldBillDimensionSetting oldBillDimensionSetting) {
        BillDimensionSetting newBillDimensionSetting = new BillDimensionSetting();
        // 基本数据赋值
        BeanUtils.copyProperties(oldBillDimensionSetting, newBillDimensionSetting);
        // 认证数据转换
        OldBillAuthorizeRule oldAuthorizeRule = oldBillDimensionSetting.getAuthorizeRule();
        // 新的认证数据对象
        BillAuthorizeRule newBillAuthorizeRule = new BillAuthorizeRule();
        // 设置比较类型
        newBillAuthorizeRule.setConditionType(oldAuthorizeRule.getConditionType());
        // 子条件转换
        List<BillAuthorizeRule> newBillAuthorizeRules =
                CollectionUtils.isEmpty(oldAuthorizeRule.getBillAuthorizeRuleSettingGroups()) ? new ArrayList<>() : oldAuthorizeRule.getBillAuthorizeRuleSettingGroups()
                        .stream()
                        .map(this::convertBillAuthorizeRule)
                        .toList();
        // 设置子条件
        newBillAuthorizeRule.setChildren(newBillAuthorizeRules);
        // 设置转换后的数据
        newBillDimensionSetting.setAuthorizeRule(newBillAuthorizeRule);
        // 转换完成
        return newBillDimensionSetting;
    }

    /**
     * 转换 授权规则 将授权规则组 转换为children数据
     *
     * @param oldBillAuthorizeRuleGroup 授权规则组
     * @return 转换后的数据
     */
    public BillAuthorizeRule convertBillAuthorizeRule(OldBillAuthorizeRule.OldBillAuthorizeRuleGroup oldBillAuthorizeRuleGroup) {
        BillAuthorizeRule newBillAuthorizeRule = new BillAuthorizeRule();
        List<BillAuthorizeRuleCondition> newBillAuthorizeRuleConditions = oldBillAuthorizeRuleGroup.getBillAuthorizeRules()
                .stream().map(this::convertBillAuthorizeRuleCondition).toList();
        newBillAuthorizeRule.setConditions(newBillAuthorizeRuleConditions);
        newBillAuthorizeRule.setConditionType(oldBillAuthorizeRuleGroup.getConditionType());
        return newBillAuthorizeRule;
    }

    /**
     * 转换条件对象
     *
     * @param oldBillAuthorizeRuleCondition 旧的条件数据
     * @return 转换后的数据
     */
    private BillAuthorizeRuleCondition convertBillAuthorizeRuleCondition(OldBillAuthorizeRule.BillAuthorizeRuleCondition oldBillAuthorizeRuleCondition) {
        BillAuthorizeRuleCondition newBillAuthorizeRuleCondition = new BillAuthorizeRuleCondition();
        BeanUtils.copyProperties(oldBillAuthorizeRuleCondition, newBillAuthorizeRuleCondition);
        return newBillAuthorizeRuleCondition;
    }

}
