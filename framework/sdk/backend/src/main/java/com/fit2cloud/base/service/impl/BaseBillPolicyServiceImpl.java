package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.BillPolicy;
import com.fit2cloud.base.entity.BillPolicyAccount;
import com.fit2cloud.base.entity.BillPolicyDetails;
import com.fit2cloud.base.entity.BillPolicyDetailsAccount;
import com.fit2cloud.base.mapper.BaseBillPolicyMapper;
import com.fit2cloud.base.service.IBaseBillPolicyDetailsService;
import com.fit2cloud.base.service.IBaseBillPolicyService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class BaseBillPolicyServiceImpl extends ServiceImpl<BaseBillPolicyMapper, BillPolicy> implements IBaseBillPolicyService {
    @Resource
    private IBaseBillPolicyDetailsService baseBillPolicyDetailsService;

    @Override
    public List<BillPolicyDetailsAccount> listBillPolicy(List<String> cloudAccountIdList, List<String> resourceTypes) {
        List<BillPolicyAccount> billPolicyAccounts = this.baseMapper.listBillPolicy(cloudAccountIdList);
        if (CollectionUtils.isEmpty(billPolicyAccounts)) {
            return List.of();
        }
        List<BillPolicyDetails> billPolicyDetailsList = baseBillPolicyDetailsService.list(new LambdaQueryWrapper<BillPolicyDetails>()
                .in(CollectionUtils.isNotEmpty(billPolicyAccounts), BillPolicyDetails::getBillPolicyId, billPolicyAccounts.stream().map(BillPolicy::getId).toList())
                .in(CollectionUtils.isNotEmpty(resourceTypes), BillPolicyDetails::getResourceType, resourceTypes));
        return billPolicyAccounts.stream().map(billPolicyAccount -> {
            BillPolicyDetailsAccount billPolicyDetailsAccount = new BillPolicyDetailsAccount();
            BeanUtils.copyProperties(billPolicyAccount, billPolicyDetailsAccount);
            billPolicyDetailsAccount.setBillPolicyDetailsList(billPolicyDetailsList.stream().filter(billPolicyDetails ->
                    StringUtils.equals(billPolicyDetails.getBillPolicyId(), billPolicyAccount.getId())).toList());
            return billPolicyDetailsAccount;
        }).toList();

    }
}
