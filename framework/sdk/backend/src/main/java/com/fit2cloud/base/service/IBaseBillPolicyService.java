package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.BillPolicy;
import com.fit2cloud.base.entity.BillPolicyDetailsAccount;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IBaseBillPolicyService extends IService<BillPolicy> {

    /**
     * 获取账单策略
     *
     * @param cloudAccountIdList 云账号
     * @param resourceTypes      资源类型
     * @return 账单策略
     */
    List<BillPolicyDetailsAccount> listBillPolicy(List<String> cloudAccountIdList, List<String> resourceTypes);
}
