package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fit2cloud.dao.entity.ComplianceScanResourceResult;
import com.fit2cloud.dao.mapper.ComplianceScanResourceResultMapper;
import com.fit2cloud.service.IComplianceScanResourceResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class ComplianceScanResourceResultServiceImpl extends ServiceImpl<ComplianceScanResourceResultMapper, ComplianceScanResourceResult> implements IComplianceScanResourceResultService {

    @Override
    public void saveOrUpdate(List<ComplianceScanResourceResult> complianceScanResourceResults) {
        for (ComplianceScanResourceResult complianceScanResourceResult : complianceScanResourceResults) {
            saveOrUpdate(complianceScanResourceResult, new LambdaUpdateWrapper<ComplianceScanResourceResult>()
                    .eq(ComplianceScanResourceResult::getResourceInstanceId, complianceScanResourceResult.getResourceInstanceId())
                    .eq(ComplianceScanResourceResult::getComplianceRuleId, complianceScanResourceResult.getComplianceRuleId())
                    .eq(ComplianceScanResourceResult::getCloudAccountId, complianceScanResourceResult.getCloudAccountId()));
        }
    }
}
