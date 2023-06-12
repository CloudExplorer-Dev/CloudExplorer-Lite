package com.fit2cloud.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fit2cloud.base.entity.BillPolicy;
import com.fit2cloud.base.entity.BillPolicyAccount;
import com.fit2cloud.base.entity.CloudAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface BaseBillPolicyMapper extends BaseMapper<BillPolicy> {

    /**
     * 根据云账号id 查询账单策略
     *
     * @param cloudAccountIdList 云账号id
     * @return 账单策略
     */
    List<BillPolicyAccount> listBillPolicy(@Param("cloudAccountIdList") List<String> cloudAccountIdList);

    /**
     * 查询最后一条计费策略
     *
     * @param wrapper 查询条件
     * @return 计费策略
     */
    List<BillPolicy> listLastBillPolicy(@Param(Constants.WRAPPER) Wrapper<BillPolicy> wrapper);

    /**
     * 查询到当前策略key关联的云账号
     *
     * @param billingPolicyId 策略id
     * @return 可以关联的云账号列表
     */
    List<CloudAccount> listCloudAccountByPolicy(@Param("billingPolicyId") String billingPolicyId);

}
