package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fit2cloud.dao.entity.ComplianceRuleInsuranceStatuteMapping;
import com.fit2cloud.dao.mapper.ComplianceRuleInsuranceStatuteMappingMapper;
import com.fit2cloud.service.IComplianceRuleInsuranceStatuteMappingService;
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
public class ComplianceRuleInsuranceStatuteMappingServiceImpl extends ServiceImpl<ComplianceRuleInsuranceStatuteMappingMapper, ComplianceRuleInsuranceStatuteMapping> implements IComplianceRuleInsuranceStatuteMappingService {

    @Override
    public void save(String complianceRuleId, List<Integer> insuranceStatuteIds) {
        List<ComplianceRuleInsuranceStatuteMapping> complianceRuleInsuranceStatuteMappings = insuranceStatuteIds.stream().map(insuranceStatuteId -> {
            ComplianceRuleInsuranceStatuteMapping complianceRuleInsuranceStatuteMapping = new ComplianceRuleInsuranceStatuteMapping();
            complianceRuleInsuranceStatuteMapping.setComplianceRuleId(complianceRuleId);
            complianceRuleInsuranceStatuteMapping.setComplianceInsuranceStatuteId(insuranceStatuteId);
            return complianceRuleInsuranceStatuteMapping;
        }).toList();
        saveBatch(complianceRuleInsuranceStatuteMappings);
    }

    @Override
    public void update(String complianceRuleId, List<Integer> insuranceStatuteIds) {
        remove(new LambdaQueryWrapper<ComplianceRuleInsuranceStatuteMapping>().eq(ComplianceRuleInsuranceStatuteMapping::getComplianceRuleId, complianceRuleId));
        save(complianceRuleId, insuranceStatuteIds);
    }
}
